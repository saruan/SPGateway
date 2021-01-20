package com.kbds.auth.apps.saml.service;

import com.kbds.auth.apps.cluster.dto.GatewayClusterDTO;
import com.kbds.auth.apps.cluster.service.GatewayClusterService;
import com.kbds.auth.apps.saml.dto.SamlDTO;
import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.common.code.ConstantsCode;
import com.kbds.auth.common.exception.BizException;
import com.onelogin.saml2.authn.SamlResponse;
import com.onelogin.saml2.exception.ValidationError;
import com.onelogin.saml2.http.HttpRequest;
import com.onelogin.saml2.settings.Saml2Settings;
import com.onelogin.saml2.util.Constants;
import com.onelogin.saml2.util.Util;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.AssertionMarshaller;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

/**
 * <pre>
 *  File  Name     : SamlService
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-01-14          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Slf4j
@Service
public class SamlService {

  final GatewayClusterService gatewayClusterService;
  /* SAML 유효시간 체크를 위한 HttpRequest 쿼리명 */
  final String SAML_RESPONSE = "SAMLResponse";
  /* SAML Response 루트 태그명 */
  final String SIGNATURE_ASSERTION_ROOT = "/saml2:Assertion/ds:Signature";
  /* 인증서 기본 타입 */
  final String KEYSTORE_PKCS12 = "PKCS12";

  XMLObjectBuilderFactory builderFactory;
  X509Certificate cert = null;
  PrivateKey key = null;
  List<X509Certificate> subCerts;

  /**
   * Constructor
   *
   * @param gatewayClusterService GatewayClusterService
   */
  public SamlService(GatewayClusterService gatewayClusterService) {

    this.gatewayClusterService = gatewayClusterService;

    try {

      /* OpenSAML 관련 초기 설정 */
      DefaultBootstrap.bootstrap();
      builderFactory = Configuration.getBuilderFactory();

      /* 인증서 정보 DB 에서 추출 */
      List<GatewayClusterDTO> gatewayClusters = gatewayClusterService.selectAllClusters();

      subCerts = new ArrayList<>();

      for (GatewayClusterDTO gatewayClusterDTO : gatewayClusters) {

        /* KeyStore Load From Database */
        KeyStore keystore = KeyStore.getInstance(KEYSTORE_PKCS12);
        keystore.load(new ByteArrayInputStream(gatewayClusterDTO.getCertificateFile()),
            gatewayClusterDTO.getCertificatePassword().toCharArray());

        /*
         * crt, .key 파일 추출
         * Main 인증서는 생성, 검증에 모두 이용하고 Sub 인증서는 검증에만 이용한다.
         */
        Enumeration enumeration = keystore.aliases();

        while (enumeration.hasMoreElements()) {

          String alias = (String) enumeration.nextElement();

          if (ConstantsCode.Y.getCode().equals(gatewayClusterDTO.getMainYn())) {

            cert = (X509Certificate) keystore.getCertificate(alias);
            key = (PrivateKey) keystore
                .getKey(alias, gatewayClusterDTO.getCertificatePassword().toCharArray());
          }
          setAllCertificate(keystore, alias);
        }
      }
    }catch(Exception e){

      log.error(e.toString());
    }
  }

  /**
   * SAML Assertion 생성
   *
   * @return BASE64 Encoded SAML Assertion String
   */
  public String generateSamlAssertion(SamlDTO samlDTO) {

    try {

      SAMLObjectBuilder assertionBuilder = (SAMLObjectBuilder) builderFactory
          .getBuilder(Assertion.DEFAULT_ELEMENT_NAME);

      /* Issuer, Assertion 생성 */
      Assertion assertion = (Assertion) assertionBuilder.buildObject();
      assertion.setIssuer(buildIssuer());
      assertion.setIssueInstant(new DateTime());
      assertion.setVersion(SAMLVersion.VERSION_20);

      /* Sub Tree 등록 */
      assertion.setSubject(buildSubjectAssertion(samlDTO));
      assertion.getAuthnStatements().add(buildAuthnStatementAssertion());
      assertion.setConditions(buildConditionAssertion());

      AssertionMarshaller marshaller = new AssertionMarshaller();

      String plainText = XMLHelper.nodeToString(marshaller.marshall(assertion));

      /* Signature 추가 */
      plainText = Util
          .addSign(Util.convertStringToDocument(plainText), key, cert, Constants.RSA_SHA1);

      return new String(Base64.getEncoder().encode(plainText.getBytes()),
          StandardCharsets.UTF_8.name());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * Saml Assertion 검증
   *
   * @return 검증 결과
   */
  public boolean validateSamlAssertion(String samlAssertion) {

    try {

      /* 인증서 서명 체크 */
      String decodedAssertion = new String(Base64.getDecoder().decode(samlAssertion),
          StandardCharsets.UTF_8.name());
      Document doc = Util.loadXML(decodedAssertion);

      if (!Util.validateSign(doc, subCerts, null, null, SIGNATURE_ASSERTION_ROOT)) {

        throw new BizException(BizExceptionCode.SAML002);
      }

      /* 만료시간 체크 */
      HttpRequest request = new HttpRequest("", "").addParameter(SAML_RESPONSE, samlAssertion);
      SamlResponse samlResponse = new SamlResponse(new Saml2Settings(), request);

      return samlResponse.validateTimestamps();
    } catch (ValidationError e) {

      throw new BizException(BizExceptionCode.SAML001, e.toString());
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()), e.toString());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * Build Subject Level Assertion
   *
   * @return Subject
   */
  public Subject buildSubjectAssertion(SamlDTO samlDTO) {

    SAMLObjectBuilder subjectConfirmationBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
    SubjectConfirmation subjectConfirmation = (SubjectConfirmation) subjectConfirmationBuilder
        .buildObject();

    SAMLObjectBuilder subjectConfirmationMethodBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
    SubjectConfirmationData subjectConfirmationData =
        (SubjectConfirmationData) subjectConfirmationMethodBuilder.buildObject();

    /* SubjectConfirmation, SubjectConfirmationData 태그 설정 */
    subjectConfirmation.setMethod(SubjectConfirmation.METHOD_BEARER);
    subjectConfirmationData.setRecipient("https://10.255.60.42/");
    subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);

    /* NameID 태그 설정 */
    SAMLObjectBuilder nameIdBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(NameID.DEFAULT_ELEMENT_NAME);
    NameID nameId = (NameID) nameIdBuilder.buildObject();
    nameId.setValue(samlDTO.getCi());
    nameId.setNameQualifier("");
    nameId.setFormat(NameID.UNSPECIFIED);

    /* Subject 객체 생성 */
    SAMLObjectBuilder subjectBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(Subject.DEFAULT_ELEMENT_NAME);
    Subject subject = (Subject) subjectBuilder.buildObject();
    subject.setNameID(nameId);
    subject.getSubjectConfirmations().add(subjectConfirmation);

    return subject;
  }

  /**
   * Build Authn Level Assertion
   *
   * @return AuthnStatement
   */
  public AuthnStatement buildAuthnStatementAssertion() {

    /* AuthnContext, AuthnContextClassRef 태그 설정 */
    SAMLObjectBuilder authnContextBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(AuthnContext.DEFAULT_ELEMENT_NAME);
    AuthnContext authnContext = (AuthnContext) authnContextBuilder.buildObject();

    SAMLObjectBuilder authnContextClassRefBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
    AuthnContextClassRef authnContextClassRef = (AuthnContextClassRef) authnContextClassRefBuilder
        .buildObject();

    authnContextClassRef.setAuthnContextClassRef(AuthnContext.UNSPECIFIED_AUTHN_CTX);
    authnContext.setAuthnContextClassRef(authnContextClassRef);

    /* Root 태그 설정 */
    SAMLObjectBuilder AuthnStatementBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(AuthnStatement.DEFAULT_ELEMENT_NAME);

    AuthnStatement authnStatement = (AuthnStatement) AuthnStatementBuilder.buildObject();
    authnStatement.setAuthnInstant(new DateTime());
    authnStatement.setAuthnContext(authnContext);

    return authnStatement;
  }

  /**
   * Build Condition Level Assertion
   *
   * @return Conditions
   */
  public Conditions buildConditionAssertion() {

    SAMLObjectBuilder conditionsBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
    SAMLObjectBuilder audienceBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(Audience.DEFAULT_ELEMENT_NAME);
    SAMLObjectBuilder audienceRestrictionBuilder = (SAMLObjectBuilder) builderFactory
        .getBuilder(AudienceRestriction.DEFAULT_ELEMENT_NAME);

    /* Condition, Audience, Restriction 태그 생성 */
    Conditions conditions = (Conditions) conditionsBuilder.buildObject();
    Audience audience = (Audience) audienceBuilder.buildObject();
    AudienceRestriction audienceRestriction = (AudienceRestriction) audienceRestrictionBuilder
        .buildObject();

    DateTime dateTime = new DateTime();

    conditions.setNotBefore(dateTime.minus(20));
    conditions.setNotOnOrAfter(dateTime.plus(30));

    audience.setAudienceURI("https://10.255.60.42");

    audienceRestriction.getAudiences().add(audience);
    conditions.getAudienceRestrictions().add(audienceRestriction);

    return conditions;
  }

  /**
   * Build Issuer
   *
   * @return Issuer
   */
  public Issuer buildIssuer() {

    SAMLObjectBuilder issuerBuilder = (SAMLObjectBuilder) builderFactory.getBuilder(
        Issuer.DEFAULT_ELEMENT_NAME);
    Issuer issuer = (Issuer) issuerBuilder.buildObject();
    issuer.setValue(cert.getIssuerX500Principal().getName());

    return issuer;
  }

  /**
   * 모든 인증서 생성
   *
   * @param keystore KeyStore
   * @param alias    별칭
   * @throws KeyStoreException KeyStoreException
   */
  public void setAllCertificate(KeyStore keystore, String alias) throws KeyStoreException {

    subCerts.add((X509Certificate) keystore.getCertificate(alias));
  }
}

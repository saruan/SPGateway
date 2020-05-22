package com.kbds.auth.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import com.kbds.auth.code.AuthCode;
import com.kbds.auth.code.BizExceptionCode;
import com.kbds.auth.entity.OAuthAccessToken;
import com.kbds.auth.entity.OAuthRefreshToken;
import com.kbds.auth.exception.CustomOAuthException;
import com.kbds.auth.repository.OAuthAccessTokenRepository;
import com.kbds.auth.repository.OAuthRefreshTokenRepository;
import com.kbds.auth.service.GatewayClusterService;
import com.kbds.auth.utils.OAuthUtils;
import com.kbds.auth.utils.StringUtils;

/**
 *
 * <pre>
 *  Class Name     : CustomTokenStore.java
 *  Description    : TokenStore 커스터마이징 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-19    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
public class CustomTokenStore implements TokenStore {

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(CustomTokenStore.class);

  @Autowired
  OAuthAccessTokenRepository oAuthAccessTokenRepository;

  @Autowired
  OAuthRefreshTokenRepository oAuthRefreshTokenRepository;

  @Autowired
  GatewayClusterService gatewayClusterService;

  // DefaultAuthenticationKeyGenerator : Client / AccessToken가 1:1 일 경우
  // UniqueAuthenticationKeyGenerator : Client / AccessToken가 1:N 일 경우
  private AuthenticationKeyGenerator authenticationKeyGenerator =
      new DefaultAuthenticationKeyGenerator();
  // new UniqueAuthenticationKeyGenerator();

  @Override
  public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {

    return readAuthentication(token.getValue());
  }

  @Override
  public OAuth2Authentication readAuthentication(String token) {

    Optional<OAuthAccessToken> accessToken =
        oAuthAccessTokenRepository.findByTokenId(OAuthUtils.extractTokenKey(token));

    if (accessToken.isPresent()) {

      return (OAuth2Authentication) SerializationUtils
          .deserialize(accessToken.get().getAuthentication());
    }

    return null;
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken oAuth2AccessToken,
      OAuth2Authentication oAuth2Authentication) {

    String refreshToken = null;

    if (oAuth2AccessToken.getRefreshToken() != null) {

      refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
    }

    if (readAccessToken(oAuth2AccessToken.getValue()) != null) {

      this.removeAccessToken(oAuth2AccessToken);
    }

    OAuthAccessToken accessToken = new OAuthAccessToken();

    accessToken.setTokenId(OAuthUtils.extractTokenKey(oAuth2AccessToken.getValue()));
    accessToken.setToken(SerializationUtils.serialize(oAuth2AccessToken));
    accessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(oAuth2Authentication));
    accessToken.setUserName(
        oAuth2Authentication.isClientOnly() ? "Clients" : oAuth2Authentication.getName());
    accessToken.setClientId(oAuth2Authentication.getOAuth2Request().getClientId());
    accessToken.setAuthentication(SerializationUtils.serialize(oAuth2Authentication));
    accessToken.setRefreshToken(OAuthUtils.extractTokenKey(refreshToken));
    accessToken.setAdditionalInfo(
        oAuth2Authentication.getOAuth2Request().getRequestParameters().toString());

    oAuthAccessTokenRepository.save(accessToken);
  }

  @Override
  public OAuth2AccessToken readAccessToken(String tokenValue) {

    Optional<OAuthAccessToken> accessToken =
        oAuthAccessTokenRepository.findByTokenId(OAuthUtils.extractTokenKey(tokenValue));

    if (accessToken.isPresent()) {

      return (OAuth2AccessToken) SerializationUtils.deserialize(accessToken.get().getToken());
    }
    return null;
  }


  @Override
  public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {

    Optional<OAuthAccessToken> accessToken = oAuthAccessTokenRepository
        .findByTokenId(OAuthUtils.extractTokenKey(oAuth2AccessToken.getValue()));

    if (accessToken.isPresent()) {

      oAuthAccessTokenRepository.delete(accessToken.get());
    }
  }

  @Override
  public void storeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken,
      OAuth2Authentication authentication) {

    OAuthRefreshToken oAuthRefreshToken = new OAuthRefreshToken();

    oAuthRefreshToken.setTokenId(OAuthUtils.extractTokenKey(oAuth2RefreshToken.getValue()));
    oAuthRefreshToken.setToken(SerializationUtils.serialize(oAuth2RefreshToken));
    oAuthRefreshToken.setAuthentication(SerializationUtils.serialize(authentication));

    oAuthRefreshTokenRepository.save(oAuthRefreshToken);
  }

  @Override
  public OAuth2RefreshToken readRefreshToken(String tokenValue) {

    Optional<OAuthRefreshToken> refreshToken =
        oAuthRefreshTokenRepository.findByTokenId(OAuthUtils.extractTokenKey(tokenValue));

    return refreshToken.isPresent()
        ? (OAuth2RefreshToken) SerializationUtils.deserialize(refreshToken.get().getToken())
        : null;
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(
      OAuth2RefreshToken oAuth2RefreshToken) {

    Optional<OAuthRefreshToken> oAuthRefreshToken = oAuthRefreshTokenRepository
        .findByTokenId(OAuthUtils.extractTokenKey(oAuth2RefreshToken.getValue()));

    return oAuthRefreshToken.isPresent()
        ? (OAuth2Authentication) SerializationUtils
            .deserialize(oAuthRefreshToken.get().getAuthentication())
        : null;
  }

  @Override
  public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {

    Optional<OAuthRefreshToken> oAuthRefreshToken = oAuthRefreshTokenRepository
        .findByTokenId(OAuthUtils.extractTokenKey(oAuth2RefreshToken.getValue()));

    if (oAuthRefreshToken.isPresent()) {

      oAuthRefreshTokenRepository.delete(oAuthRefreshToken.get());
    }
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {

    Optional<OAuthAccessToken> oAuthAccessToken = oAuthAccessTokenRepository
        .findByRefreshToken(OAuthUtils.extractTokenKey(oAuth2RefreshToken.getValue()));

    if (oAuthAccessToken.isPresent()) {

      oAuthAccessTokenRepository.delete(oAuthAccessToken.get());
    }
  }

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuthAuthentication) {

    OAuth2AccessToken oAuth2AccessToken = null;

    String authenticationId = authenticationKeyGenerator.extractKey(oAuthAuthentication);

    // Access 토큰 발급 로직 전 SAML 체크
    String keySAML = oAuthAuthentication.getOAuth2Request().getRequestParameters()
        .get(AuthCode.PARAMTER_SAML.getCode());

    if (StringUtils.isEmptyParams(keySAML) || !gatewayClusterService.isValidSAML(keySAML)) {

      throw new CustomOAuthException(BizExceptionCode.SAML001);
    }

    Optional<OAuthAccessToken> oAuthAccessToken =
        oAuthAccessTokenRepository.findByAuthenticationId(authenticationId);

    if (oAuthAccessToken.isPresent()) {

      oAuth2AccessToken =
          (OAuth2AccessToken) SerializationUtils.deserialize(oAuthAccessToken.get().getToken());

      if (oAuth2AccessToken != null && !authenticationId.equals(
          this.authenticationKeyGenerator.extractKey(this.readAuthentication(oAuth2AccessToken)))) {

        this.removeAccessToken(oAuth2AccessToken);
        this.storeAccessToken(oAuth2AccessToken, oAuthAuthentication);
      }
    }
    return oAuth2AccessToken;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId,
      String userName) {

    Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();

    List<OAuthAccessToken> result =
        oAuthAccessTokenRepository.findByClientIdAndUserName(clientId, userName);

    result
        .forEach(e -> tokens.add((OAuth2AccessToken) SerializationUtils.deserialize(e.getToken())));

    return tokens;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {

    Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();

    List<OAuthAccessToken> result = oAuthAccessTokenRepository.findByClientId(clientId);

    result
        .forEach(e -> tokens.add((OAuth2AccessToken) SerializationUtils.deserialize(e.getToken())));

    return tokens;
  }
}

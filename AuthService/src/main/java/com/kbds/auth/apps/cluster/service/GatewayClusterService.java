package com.kbds.auth.apps.cluster.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kbds.auth.apps.cluster.dto.GatewayClusterDTO;
import com.kbds.auth.apps.cluster.entity.GatewayCluster;
import com.kbds.auth.apps.cluster.repository.GatewayClusterRepository;
import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.common.code.ConstantsCode;
import com.kbds.auth.common.exception.BizException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <pre>
 *  Class Name     : GatewayClusterService.java
 *  Description    : GatewayCluster간 인증 및 키 관리를 수행하는 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-21    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Service
public class GatewayClusterService {

  private final GatewayClusterRepository gatewayClusterRepository;
  private final ModelMapper modelMapper;

  public GatewayClusterService(GatewayClusterRepository gatewayClusterRepository,
      ModelMapper modelMapper) {
    this.gatewayClusterRepository = gatewayClusterRepository;
    this.modelMapper = modelMapper;
  }

  /**
   * 등록된 전체 클러스터 목록 조회
   *
   * @return 클러스터 전체 목록
   */
  public List<GatewayClusterDTO> selectAllClusters() {

    return modelMapper
        .map(gatewayClusterRepository.findAll(), new TypeReference<List<GatewayClusterDTO>>() {
        }.getType());
  }

  /**
   * GatewayCluster 신규 등록
   *
   * @param gatewayClusterDTO 등록 정보
   * @param multipartFile     인증서 파일
   */
  @CacheEvict(cacheNames = "clusterList", allEntries = true)
  public void registerCluster(GatewayClusterDTO gatewayClusterDTO, MultipartFile multipartFile) {

    try {

      X509Certificate cert;
      PrivateKey key = null;

      /* 인증서 파일 유효성 검증 */
      if (!isValidParams(gatewayClusterDTO, multipartFile)) {

        throw new BizException(BizExceptionCode.CLS001);
      }

      String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
      ByteArrayInputStream certFile = new ByteArrayInputStream(multipartFile.getBytes());
      GatewayCluster gatewayCluster = modelMapper.map(gatewayClusterDTO, GatewayCluster.class);

      /* 인증서 확장자에 따라 타입 지정
       *  MAIN 클러스터는 검증/생성을 위해 반드시 P12파일로 등록하여야 한다.
       *  SUB 클러스터는 검증만을 수행하며 CRT 파일 P12 파일 둘다 등록이 가능하다.
       */
      if ("p12".equals(extension)) {

        KeyStore keystore = KeyStore.getInstance(ConstantsCode.PKCS12.getCode());
        keystore.load(certFile, gatewayClusterDTO.getCertificatePassword().toCharArray());

        gatewayClusterDTO.setCertificateFile(multipartFile.getBytes());

        String alias = keystore.aliases().nextElement();

        cert = (X509Certificate) keystore.getCertificate(alias);
        key = (PrivateKey) keystore
            .getKey(alias, gatewayClusterDTO.getCertificatePassword().toCharArray());

        gatewayCluster.setPrivateKey(key.getEncoded());

      } else if ("crt".equals(extension) && ConstantsCode.N.getCode()
          .equals(gatewayClusterDTO.getMainYn())) {

        CertificateFactory certificateFactory = CertificateFactory.getInstance(ConstantsCode.X509
            .getCode());
        cert = (X509Certificate) certificateFactory.generateCertificate(certFile);

      } else {

        throw new BizException(BizExceptionCode.COM003);
      }

      gatewayCluster.setCert(cert.getEncoded());
      gatewayClusterRepository.save(gatewayCluster);

    } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {

      throw new BizException(BizExceptionCode.SSL001, e.toString());
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()), e.toString());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 기등록 게이트웨이 여부와 메인 클러스터로 등록된 정보가 있는지 확인
   *
   * @param gatewayClusterDTO 게이트웨이 정보
   * @return 기등록여부
   */
  public boolean isValidParams(GatewayClusterDTO gatewayClusterDTO, MultipartFile multipartFile) {

    if (multipartFile.isEmpty()) {

      return false;
    } else if (ConstantsCode.Y.getCode().equals(gatewayClusterDTO.getMainYn())) {

      return gatewayClusterRepository.countByGatewayIdOrMainYn(gatewayClusterDTO.getGatewayId(),
          ConstantsCode.Y.getCode()) == 0;
    } else {

      return gatewayClusterRepository.countByGatewayId(gatewayClusterDTO.getGatewayId()) == 0;
    }
  }
}

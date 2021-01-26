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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
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

      X509Certificate cert = null;
      PrivateKey key = null;

      /* 인증서 파일 유효성 검증 */
      if (multipartFile != null) {

        KeyStore keystore = KeyStore.getInstance(ConstantsCode.PKCS12.getCode());
        keystore.load(new ByteArrayInputStream(multipartFile.getBytes()),
            gatewayClusterDTO.getCertificatePassword().toCharArray());

        gatewayClusterDTO.setCertificateFile(multipartFile.getBytes());

        /*
         * crt, .key 파일 추출
         * Main 인증서는 생성, 검증에 모두 이용하고 Sub 인증서는 검증에만 이용한다.
         */
        String alias = keystore.aliases().nextElement();

        cert = (X509Certificate) keystore.getCertificate(alias);
        key = (PrivateKey) keystore
            .getKey(alias, gatewayClusterDTO.getCertificatePassword().toCharArray());
      }

      if (isRegisteredCluster(gatewayClusterDTO.getGatewayId())) {

        throw new BizException(BizExceptionCode.CLS001);
      }

      GatewayCluster gatewayCluster = modelMapper.map(gatewayClusterDTO, GatewayCluster.class);

      gatewayCluster.setCert(cert.getEncoded());
      gatewayCluster.setPrivateKey(key.getEncoded());

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
   * 기등록 게이트웨이인지 확인
   *
   * @param gatewayId 게이트웨이 ID
   * @return 기등록여부
   */
  public boolean isRegisteredCluster(String gatewayId) {

    return gatewayClusterRepository.countByGatewayId(gatewayId) > 0;
  }
}

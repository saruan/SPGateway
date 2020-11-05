package com.kbds.serviceapi.framework.repository.querydsl;

import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.Cacheable;

/**
 * <pre>
 *  File  Name     : SPApiRepository
 *  Description    : 포탈 서비스 관리 Repository
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-04             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public interface SPApiCustomRepository {

  /**
   * Security 최초 권한 정보 로딩을 위한 권한 별 Menu 정보 조회
   * @return
   */
  @Cacheable(value = "apiRoleList")
  Map<String, List<String>> selectListForSecurity();

}

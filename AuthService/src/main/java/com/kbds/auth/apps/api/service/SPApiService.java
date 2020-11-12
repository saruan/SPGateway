package com.kbds.auth.apps.api.service;

import com.kbds.auth.apps.api.repository.querydsl.SPApiCustomRepository;
import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.common.exception.BizException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : SPApiService
 *  Description    : API 권한 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-09          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class SPApiService {

  @Autowired
  SPApiCustomRepository spApiCustomRepository;

  /**
   * API URI 권한 목록 조회
   * @return 결과 목록
   */
  public Map<String, List<String>> selectListForSecurity() {

    try {

      return spApiCustomRepository.selectListForSecurity();
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }
}

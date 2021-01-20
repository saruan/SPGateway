package com.kbds.auth.apps.user.repository.querydsl;

import com.kbds.auth.apps.user.dto.UserDTO;
import java.util.List;

/**
 * <pre>
 *  File  Name     : SPUserCustomRepository
 *  Description    : 사용자 관련 커스텀 쿼리 인터페이스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-09          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public interface SPUserCustomRepository {

  /**
   * 사용자 로그인 ID로 상세 정보 조회
   * @param userLoginId 사용자 로그인 ID
   * @return  사용자 정보
   */
  UserDTO findByUserDetails(String userLoginId);

}

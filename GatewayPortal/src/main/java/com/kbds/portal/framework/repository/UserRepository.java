package com.kbds.portal.framework.repository;

import com.kbds.portal.framework.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 *  File  Name     : UserRepository
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-25          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  /**
   * 사용자 로그인 아이디로 검색
   *
   * @param userLoginId
   * @return
   */
  User findByUserLoginId(String userLoginId);

}

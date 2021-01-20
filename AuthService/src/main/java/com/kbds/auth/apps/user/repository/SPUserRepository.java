package com.kbds.auth.apps.user.repository;

import com.kbds.auth.apps.user.entity.SPUsers;
import com.kbds.auth.apps.user.repository.querydsl.SPUserCustomRepository;
import org.springframework.context.annotation.Primary;
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
@Primary
public interface SPUserRepository extends CrudRepository<SPUsers, Long>, SPUserCustomRepository {

  /**
   * 사용자 로그인 아이디로 검색
   *
   * @param userLoginId Login Id
   * @return  User Info Object
   */
  SPUsers findByUserLoginId(String userLoginId);

  /**
   * 사용자 ID 건수 조회
   * @param userLoginId Login Id
   * @return  count
   */
  int countByUserLoginId(String userLoginId);

  /**
   * 해당 Group Id를 사용하는 사용자 수 조회
   * @param groupId GroupId
   * @return  count
   */
  int countBySpGroupsGroupId(Long groupId);
}

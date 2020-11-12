package com.kbds.auth.apps.role.repository;

import com.kbds.auth.apps.role.entity.SPRoles;
import org.springframework.data.repository.CrudRepository;

/**
 * <pre>
 *  File  Name     : RoleRepository
 *  Description    : 권한 관련 기본 Repository
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-31          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public interface SPRoleRepository extends CrudRepository<SPRoles, Long> {

  /**
   * 권한 코드로 조회
   * @param roleCd  Role Code Value
   * @return  count value
   */
  int countByRoleCd(String roleCd);

}

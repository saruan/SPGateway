package com.kbds.serviceapi.framework.repository;

import com.kbds.serviceapi.framework.entity.Role;
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
public interface RoleRepository extends CrudRepository<Role, Long> {

  /**
   * 권한 코드로 조회
   * @param roleCd
   * @return
   */
  Role findByRoleCd(String roleCd);

}

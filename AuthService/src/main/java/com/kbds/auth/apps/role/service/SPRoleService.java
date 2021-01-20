package com.kbds.auth.apps.role.service;

import com.kbds.auth.apps.role.repository.SPRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : AuthorizationService
 *  Description    : 권한 관리 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-27             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class SPRoleService {

  @Autowired
  SPRoleRepository spRoleRepository;

  @Autowired
  ModelMapper modelMapper;

  /**
   * 이미 등록되어 있는 권한 코드인지 조회
   * @param roleId 권한 ID
   * @return 권한 등록 여부
   */
  public boolean isRegisteredRoleById(Long roleId){

    return spRoleRepository.countByRoleId(roleId) > 0;
  }
}

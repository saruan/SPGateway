package com.kbds.serviceapi.framework.service;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.dto.RoleDTO;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.entity.SPRoles;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.framework.repository.SPRoleRepository;
import com.kbds.serviceapi.framework.repository.querydsl.SPMenuCustomRepository;
import com.kbds.serviceapi.framework.repository.querydsl.SPRoleCustomRepository;
import java.util.List;
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
  SPMenuCustomRepository SPMenuCustomRepository;

  @Autowired
  SPRoleCustomRepository SPRoleCustomRepository;

  @Autowired
  SPRoleRepository SPRoleRepository;

  @Autowired
  ModelMapper modelMapper;

  /**
   * Role 조회
   *
   * @param searchDTO 객체
   * @return 권한 목록
   */
  public List<RoleDTO> findRoles(SearchDTO searchDTO) {

    try {

      return SPRoleCustomRepository.findByConditions(searchDTO);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * Role 신규 등록
   *
   * @param roleDTO 객체
   */
  public void registerRole(RoleDTO roleDTO) {

    try {

      if (isRegisteredRole(roleDTO.getRoleCd())) {

        throw new BizException(BizExceptionCode.COM003);
      }

      SPRoles SPRoles = modelMapper.map(roleDTO, SPRoles.class);

      SPRoleRepository.save(SPRoles);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 이미 등록되어 있는 권한 코드인지 조회
   *
   * @param roleCd 권한 코드
   * @return 권한 등록 여부
   */
  public boolean isRegisteredRole(String roleCd) {

    return SPRoleRepository.countByRoleCd(roleCd) > 0;
  }
}

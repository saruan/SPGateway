package com.kbds.serviceapi.framework.repository.querydsl.impl;

import static com.kbds.serviceapi.framework.entity.QRole.role;

import com.kbds.serviceapi.common.utils.StringUtils;
import com.kbds.serviceapi.framework.dto.QRoleDTO;
import com.kbds.serviceapi.framework.dto.RoleDTO;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.entity.Role;
import com.kbds.serviceapi.framework.repository.querydsl.RoleCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 *  File  Name     : RoleCustomRepositoryImpl
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-31          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Repository
public class RoleCustomRepositoryImpl extends QuerydslRepositorySupport implements
    RoleCustomRepository {

  /**
   * 생성자
   */
  public RoleCustomRepositoryImpl() {
    super(Role.class);
  }

  @Override
  public List<RoleDTO> findByConditions(SearchDTO searchDTO) {

    return from(role)
        .where(likeNm(searchDTO.getName()))
        .select(new QRoleDTO(role.roleId,
                             role.roleNm,
                             role.roleNm,
                             role.regUserNo,
                             role.uptUserNo,
                             role.regDt,
                             role.uptDt))
        .fetch();
  }

  /**
   * 이름 LIKE 검색
   * @param name
   * @return
   */
  private BooleanExpression likeNm(String name){

    return StringUtils.isEmptyParams(name) ? null : role.roleNm.like(name);
  }
}

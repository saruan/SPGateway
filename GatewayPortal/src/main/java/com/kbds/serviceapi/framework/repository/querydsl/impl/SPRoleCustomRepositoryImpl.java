package com.kbds.serviceapi.framework.repository.querydsl.impl;

import static com.kbds.serviceapi.framework.entity.QSPRoles.sPRoles;

import com.kbds.serviceapi.common.utils.StringUtils;
import com.kbds.serviceapi.framework.dto.QRoleDTO;
import com.kbds.serviceapi.framework.dto.RoleDTO;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.entity.SPRoles;
import com.kbds.serviceapi.framework.repository.querydsl.SPRoleCustomRepository;
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
public class SPRoleCustomRepositoryImpl extends QuerydslRepositorySupport implements
    SPRoleCustomRepository {

  /**
   * 생성자
   */
  public SPRoleCustomRepositoryImpl() {
    super(SPRoles.class);
  }

  @Override
  public List<RoleDTO> findByConditions(SearchDTO searchDTO) {

    return from(sPRoles)
        .where(likeNm(searchDTO.getName()))
        .select(new QRoleDTO(sPRoles.roleId,
            sPRoles.roleNm,
            sPRoles.roleNm,
            sPRoles.regUserNo,
            sPRoles.uptUserNo,
            sPRoles.regDt,
            sPRoles.uptDt))
        .fetch();
  }

  /**
   * 이름 LIKE 검색
   *
   * @param name
   * @return
   */
  private BooleanExpression likeNm(String name) {

    return StringUtils.isEmptyParams(name) ? null : sPRoles.roleNm.like(name);
  }
}

package com.kbds.serviceapi.framework.repository.querydsl.impl;

import static com.kbds.serviceapi.framework.entity.QSPApis.sPApis;
import static com.kbds.serviceapi.framework.entity.QSPRoleApiMapping.sPRoleApiMapping;
import static com.kbds.serviceapi.framework.entity.QSPRoles.sPRoles;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.kbds.serviceapi.framework.dto.QMenuDTO;
import com.kbds.serviceapi.framework.entity.SPApis;
import com.kbds.serviceapi.framework.repository.querydsl.SPApiCustomRepository;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 *  File  Name     : SPApiCustomRepositoryImpl
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-06          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Repository
public class SPApiCustomRepositoryImpl extends QuerydslRepositorySupport implements
    SPApiCustomRepository {

  /**
   * 생성자
   */
  public SPApiCustomRepositoryImpl() {

    super(SPApis.class);
  }

  @Override
  public Map<String, List<String>> selectListForSecurity() {

    return from(sPApis)
        .innerJoin(sPRoleApiMapping)
        .on(sPApis.apiId.eq(sPRoleApiMapping.spApis.apiId))
        .innerJoin(sPRoles)
        .on(sPRoleApiMapping.spRoles.roleId.eq(sPRoles.roleId))
        .select(new QMenuDTO(sPApis.apiUrl, sPRoles.roleNm))
        .transform(groupBy(sPApis.apiUrl).as(list(sPRoles.roleCd)));
  }
}

package com.kbds.auth.apps.api.repository.querydsl.impl;


import static com.kbds.auth.apps.api.entity.QSPApis.sPApis;
import static com.kbds.auth.apps.group.entity.QSPGroups.sPGroups;
import static com.kbds.auth.apps.role.entity.QSPRoleApiMapping.sPRoleApiMapping;
import static com.kbds.auth.apps.role.entity.QSPRoles.sPRoles;
import static com.kbds.auth.apps.user.entity.QSPUsers.sPUsers;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.kbds.auth.apps.api.entity.SPApis;
import com.kbds.auth.apps.api.repository.querydsl.SPApiCustomRepository;
import com.kbds.auth.apps.menu.dto.QMenuDTO;
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
        .innerJoin(sPUsers)
        .on(sPRoles.roleId.eq(sPUsers.spRoles.roleId))
        .innerJoin(sPGroups)
        .on(sPUsers.spGroups.groupId.eq(sPGroups.groupId))
        .select(new QMenuDTO(sPApis.apiUrl, sPRoles.roleNm))
        .transform(groupBy(sPApis.apiUrl).as(list(sPRoles.roleCd)));
  }
}

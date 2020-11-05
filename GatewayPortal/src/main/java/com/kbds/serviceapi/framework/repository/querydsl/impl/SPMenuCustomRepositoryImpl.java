package com.kbds.serviceapi.framework.repository.querydsl.impl;

import static com.kbds.serviceapi.framework.entity.QSPMenus.sPMenus;
import static com.kbds.serviceapi.framework.entity.QSPRoleMenuMapping.sPRoleMenuMapping;
import static com.kbds.serviceapi.framework.entity.QSPRoles.sPRoles;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.kbds.serviceapi.common.code.CommonCode;
import com.kbds.serviceapi.framework.dto.MenuDTO;
import com.kbds.serviceapi.framework.dto.QMenuDTO;
import com.kbds.serviceapi.framework.entity.SPMenus;
import com.kbds.serviceapi.framework.repository.querydsl.SPMenuCustomRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 *  File  Name     : MenuCustomRepositoryImpl
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-27          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Repository
public class SPMenuCustomRepositoryImpl extends QuerydslRepositorySupport implements
    SPMenuCustomRepository {

  /**
   * 생성자
   */
  public SPMenuCustomRepositoryImpl() {

    super(SPMenus.class);
  }

  @Override
  public Map<String, List<String>>  selectListForSecurity() {

    return from(sPMenus)
        .innerJoin(sPRoleMenuMapping)
        .on(sPMenus.menuId.eq(sPRoleMenuMapping.spMenus.menuId))
        .innerJoin(sPRoles)
        .on(sPRoleMenuMapping.spRoles.roleId.eq(sPRoles.roleId))
        .select(new QMenuDTO(sPMenus.menuUrl, sPRoles.roleNm))
        .transform(groupBy(sPMenus.menuUrl).as(list(sPRoles.roleCd)));
  }

  @Override
  public List<MenuDTO> selectAllMenuList() {

    List<SPMenus> SPMenus = from(sPMenus)
        .innerJoin(sPRoleMenuMapping)
        .on(sPMenus.menuId.eq(sPRoleMenuMapping.spMenus.menuId))
        .innerJoin(sPRoles)
        .on(sPRoleMenuMapping.spRoles.roleId.eq(sPRoles.roleId))
        .where(sPMenus.useYn.eq(CommonCode.Y.getCode()), sPMenus.parent.isNull()).fetch();

    return SPMenus.stream().map(menu ->
        new MenuDTO(menu.getMenuId(), menu.getMenuUrl(), menu.getMenuNm()
            , menu.getUseYn(), menu.getChild())).collect(Collectors.toList());
  }

}

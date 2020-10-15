package com.kbds.serviceapi.framework.repository.querydsl.impl;

import static com.kbds.serviceapi.framework.entity.QMenu.menu;
import static com.kbds.serviceapi.framework.entity.QRole.role;
import static com.kbds.serviceapi.framework.entity.QRoleMenuMapping.roleMenuMapping;

import com.kbds.serviceapi.common.code.CommonCode;
import com.kbds.serviceapi.framework.dto.MenuDTO;
import com.kbds.serviceapi.framework.dto.QMenuDTO;
import com.kbds.serviceapi.framework.entity.Menu;
import com.kbds.serviceapi.framework.repository.querydsl.MenuCustomRepository;
import java.util.List;
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
public class MenuCustomRepositoryImpl extends QuerydslRepositorySupport implements
    MenuCustomRepository {

  /**
   * 생성자
   */
  public MenuCustomRepositoryImpl() {

    super(Menu.class);
  }

  @Override
  public List<MenuDTO> selectListForSecurity() {

    return from(menu)
        .innerJoin(roleMenuMapping)
        .on(menu.menuId.eq(roleMenuMapping.menu.menuId))
        .innerJoin(role)
        .on(roleMenuMapping.role.roleId.eq(role.roleId))
        .select(new QMenuDTO(menu.menuUrl, role.roleNm)).fetch();
  }

  @Override
  public List<MenuDTO> selectAllMenuList() {

    List<Menu> menus = from(menu)
        .innerJoin(roleMenuMapping)
        .on(menu.menuId.eq(roleMenuMapping.menu.menuId))
        .innerJoin(role)
        .on(roleMenuMapping.role.roleId.eq(role.roleId))
        .where(menu.useYn.eq(CommonCode.Y.getCode()), menu.parent.isNull()).fetch();

    return menus.stream().map(menu ->
        new MenuDTO(menu.getMenuId(),menu.getMenuUrl(), menu.getMenuNm()
            , menu.getUseYn(), menu.getChild())).collect(Collectors.toList());
  }

}

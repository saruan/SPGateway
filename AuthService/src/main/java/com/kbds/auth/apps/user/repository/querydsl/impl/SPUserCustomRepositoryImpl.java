package com.kbds.auth.apps.user.repository.querydsl.impl;

import static com.kbds.auth.apps.group.entity.QSPGroups.sPGroups;
import static com.kbds.auth.apps.role.entity.QSPRoles.sPRoles;
import static com.kbds.auth.apps.user.entity.QSPUsers.sPUsers;

import com.kbds.auth.apps.user.dto.QUserDTO;
import com.kbds.auth.apps.user.dto.UserDTO;
import com.kbds.auth.apps.user.entity.SPUsers;
import com.kbds.auth.apps.user.repository.querydsl.SPUserCustomRepository;
import com.kbds.auth.common.utils.StringUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 *  File  Name     : SPUserCustomRepositoryImpl
 *  Description    : 사용자 커스텀 쿼리용 Repository 객체
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-09          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Repository
public class SPUserCustomRepositoryImpl extends QuerydslRepositorySupport implements
    SPUserCustomRepository {

  /**
   * 생성자
   */
  public SPUserCustomRepositoryImpl() {

    super(SPUsers.class);
  }

  @Override
  public UserDTO findByUserDetails(String userLoginId) {

    return from(sPUsers)
        .innerJoin(sPRoles)
        .on(sPUsers.spRoles.roleId.eq(sPRoles.roleId))
        .innerJoin(sPGroups)
        .on(sPUsers.spGroups.groupId.eq(sPGroups.groupId))
        .where(eqUserLoginId(userLoginId))
        .select(new QUserDTO(sPUsers.userLoginId, sPUsers.userNm, sPRoles.roleNm, sPGroups.groupNm))
        .fetchOne();
  }


  /**
   * 사용자 ID로 검색
   *
   * @param userLoginId 사용자 로그인 아이디
   * @return BooleanExpression
   */
  private BooleanExpression eqUserLoginId(String userLoginId) {

    return StringUtils.isEmptyParams(userLoginId) ?
        null : sPUsers.userLoginId.eq(userLoginId);
  }

}

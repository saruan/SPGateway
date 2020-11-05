package com.kbds.serviceapi.framework.repository.querydsl;

import com.kbds.serviceapi.framework.dto.MenuDTO;
import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.Cacheable;

/**
 * <pre>
 *  File  Name     : MenuCustomRepository
 *  Description    : Menu관련 Querdsl 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-27          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public interface SPMenuCustomRepository {

  /**
   * Security 최초 권한 정보 로딩을 위한 권한 별 Menu 정보 조회
   * @return
   */
  @Cacheable(value = "menuRoleList")
  Map<String, List<String>> selectListForSecurity();

  /**
   * 화면에서 사용할 메뉴 전체 리스트 조회
   * @return
   */
  @Cacheable(value = "allMenuList")
  List<MenuDTO> selectAllMenuList();

}
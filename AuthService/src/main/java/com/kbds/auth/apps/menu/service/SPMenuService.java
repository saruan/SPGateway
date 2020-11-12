package com.kbds.auth.apps.menu.service;


import com.kbds.auth.apps.menu.dto.MenuDTO;
import com.kbds.auth.apps.menu.repository.querydsl.SPMenuCustomRepository;
import com.kbds.auth.common.code.BizExceptionCode;
import com.kbds.auth.common.exception.BizException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : MenuService
 *  Description    : 메뉴 관련 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-23             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class SPMenuService {

  @Autowired
  SPMenuCustomRepository spMenuCustomRepository;

  /**
   *  화면을 구성할 전체 메뉴 목록을 조회 (권한 별로)
   * @return 메뉴 리스트 dto 객체
   */
  public List<MenuDTO> selectAllMenuList(){

    try {

      return spMenuCustomRepository.selectAllMenuList();
    }catch(Exception e){

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }
}

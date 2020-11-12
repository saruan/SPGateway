package com.kbds.serviceapi.framework.service;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.feign.AuthClient;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.dto.ResponseDTO;
import com.kbds.serviceapi.framework.exception.BizException;
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
  AuthClient authClient;

  /**
   * 화면을 구성할 전체 메뉴 목록을 조회 (권한 별로)
   *
   * @return 메뉴 리스트 DTO 객체
   */
  public ResponseDTO selectAllMenuList() {

    try {

      return authClient.selectAllMenuList(CommonUtils.setFeignCommonHeaders());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }
}

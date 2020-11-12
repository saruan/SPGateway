package com.kbds.auth.apps.api.controller;

import com.kbds.auth.apps.api.service.SPApiService;
import com.kbds.auth.common.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  File  Name     : SPApiController
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-09          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/auth/v1.0/api")
public class SPApiController {

  @Autowired
  SPApiService spApiService;

  /**
   * 권한/API 목록 조회
   *
   * @return  조회 결과 목록
   */
  @GetMapping
  public ResponseEntity<Object> selectListForSecurity() {

    Object result = CommonUtils.getResponseEntity(spApiService.selectListForSecurity());

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}

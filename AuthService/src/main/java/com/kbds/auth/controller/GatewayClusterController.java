package com.kbds.auth.controller;

import com.kbds.auth.service.GatewayClusterService;
import com.kbds.auth.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  Class Name     : GatewayClusterController.java
 *  Description    : Gateway 클러스터 관련 API 컨트롤러
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-04     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@RestController
@RequestMapping("/auth/")
public class GatewayClusterController {

  @Autowired
  GatewayClusterService gwClusterService;

  /**
   * SAML 생성 API
   *
   * @return
   */
  @GetMapping(value = "v1.0/oauth/saml")
  public ResponseEntity<Object> generateSAML() {

    Object result = CommonUtils.getResponseEntity(gwClusterService.generateSAML());

    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }
}

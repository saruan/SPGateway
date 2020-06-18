package com.kbds.auth.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kbds.auth.service.GatewayClusterService;
import com.kbds.auth.utils.CommonUtils;


@RestController
@RequestMapping("/auth/")
public class GatewayClusterController {

  @Autowired
  GatewayClusterService gwClusterService;

  /**
   * SAML 생성 API
   * 
   * @param params
   * @return
   */
  @GetMapping(value = "v1.0/oauth/saml")
  public ResponseEntity<Object> generateSAML(HttpServletRequest request) {

    Object result = CommonUtils.getResponseEntity(gwClusterService.generateSAML());

    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }
}

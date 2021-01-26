package com.kbds.auth.apps.jwt.controller;

import com.kbds.auth.apps.jwt.service.JwtService;
import com.kbds.auth.common.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  File  Name     : JwtController
 *  Description    : Jwt 관리 컨트롤러
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-01-21         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/auth/v1.0/jwt")
public class JwtController {

  final JwtService jwtService;

  public JwtController(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  /**
   * JWT Token 생성 API
   *
   * @return 생성된 JWT Token
   */
  @PostMapping
  public ResponseEntity<Object> generateJWT() {

    Object result = CommonUtils.getResponseEntity(jwtService.generateJWT());

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }


}

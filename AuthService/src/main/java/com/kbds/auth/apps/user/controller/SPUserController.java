package com.kbds.auth.apps.user.controller;

import com.kbds.auth.apps.cluster.service.GatewayClusterService;
import com.kbds.auth.apps.user.dto.UserDTO;
import com.kbds.auth.apps.user.service.SPUserDetailService;
import com.kbds.auth.common.utils.CommonUtils;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  File  Name     : SPUserController
 *  Description    : 사용자 관리 컨트롤러
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-19         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/auth/v1.0/user")
public class SPUserController {

  private final SPUserDetailService spUserDetailService;

  /**
   * Constructor Injection
   * @param spUserDetailService 사용자 관리 서비스
   */
  public SPUserController(SPUserDetailService spUserDetailService) {

    this.spUserDetailService = spUserDetailService;
  }

  /**
   * 사용자 등록
   *
   * @return Response
   */
  @PostMapping
  public ResponseEntity<Object> registerUser(@RequestBody @Valid UserDTO userDTO) {

    spUserDetailService.registerUser(userDTO);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}

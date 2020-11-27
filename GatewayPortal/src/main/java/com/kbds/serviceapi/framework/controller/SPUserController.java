package com.kbds.serviceapi.framework.controller;

import com.kbds.serviceapi.framework.dto.UserDTO;
import com.kbds.serviceapi.framework.service.SPUserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  File  Name     : SPUserController
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-04          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/portal/v1.0/user")
public class SPUserController {

  @Autowired
  SPUserService spUserService;

  /**
   * Menu List 조회
   *
   * @return Menu List Array
   **/
  @PostMapping(value = "/login")
  public ResponseEntity<Object> login(@RequestParam String id, @RequestParam String password) {

    return new ResponseEntity<>(spUserService.getAccessToken(id, password), HttpStatus.OK);
  }

  /**
   * 사용자 등록
   * @param userDTO 사용자 정보 객체
   * @return  등록 결과
   */
  @PostMapping
  public ResponseEntity<Object> register(@RequestBody @Valid UserDTO userDTO) {

    return new ResponseEntity<>(spUserService.registerUser(userDTO), HttpStatus.OK);
  }

  /**
   * 토큰 유효성 체크
   * @return  체크 결과
   */
  @GetMapping(value = "/validate/token")
  public ResponseEntity<Object> checkAccessToken() {

    return new ResponseEntity<>(HttpStatus.OK);
  }
}

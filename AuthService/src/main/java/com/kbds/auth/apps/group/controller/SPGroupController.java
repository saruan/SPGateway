package com.kbds.auth.apps.group.controller;

import com.kbds.auth.apps.group.service.SPGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  File  Name     : SPGroupController
 *  Description    : 그룹 관련 컨트롤러 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-11-24        	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/portal/v1.0/group")
public class SPGroupController {

  private final  SPGroupService spGroupService;

  /**
   * Constructor Injection
   * @param spGroupService  SPGroupService
   */
  public SPGroupController(SPGroupService spGroupService) {

    this.spGroupService = spGroupService;
  }

  /**
   * 그룹 코드 조회
   *
   * @return Menu List Array
   **/
  @GetMapping
  public ResponseEntity<Object> selectAllGroups() {

    return new ResponseEntity<>(spGroupService.selectAllGroups(), HttpStatus.OK);
  }

}
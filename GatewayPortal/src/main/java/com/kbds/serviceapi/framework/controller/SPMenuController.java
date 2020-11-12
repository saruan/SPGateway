package com.kbds.serviceapi.framework.controller;

import com.kbds.serviceapi.framework.service.SPMenuService;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  File  Name     : MenuController
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-24             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/portal/v1.0/menu")
public class SPMenuController {

  @Autowired
  SPMenuService spMenuService;

  /**
   * Menu List 조회
   * @return  Menu List Array
   **/
  @GetMapping
  @Transactional
  public ResponseEntity<Object> selectMenuList(){

    return new ResponseEntity<>(spMenuService.selectAllMenuList(), HttpStatus.OK);
  }
}

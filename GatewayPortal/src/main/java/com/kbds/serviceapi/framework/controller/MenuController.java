package com.kbds.serviceapi.framework.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.serviceapi.apis.dto.RoutingDTO;
import com.kbds.serviceapi.framework.dto.MenuDTO;
import com.kbds.serviceapi.framework.entity.Menu;
import com.kbds.serviceapi.framework.repository.MenuRepository;
import com.kbds.serviceapi.framework.repository.querydsl.MenuCustomRepository;
import com.kbds.serviceapi.framework.service.MenuService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
@RequestMapping("/portal/")
public class MenuController {

  @Autowired
  MenuService menuService;

  @GetMapping(value = "/v1.0/menu")
  @Transactional
  public ResponseEntity<Object> selectMenuList(){

    List<MenuDTO> result = menuService.selectAllMenuList();

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}

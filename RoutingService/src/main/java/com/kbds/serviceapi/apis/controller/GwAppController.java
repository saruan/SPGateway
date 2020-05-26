package com.kbds.serviceapi.apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kbds.serviceapi.apis.dto.AppDTO;
import com.kbds.serviceapi.apis.service.GwAppService;
import com.kbds.serviceapi.common.utils.CommonUtils;

/**
 *
 * <pre>
 *  Class Name     : GwRoutingController.java
 *  Description    : 서비스 Routing 관련 컨트롤러
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@RestController
@RequestMapping("/api/service")
public class GwAppController {

  @Autowired
  GwAppService gwAppService;

  /**
   * 전체 App 조회
   * 
   * @return
   */
  @GetMapping(value = "/v1/app/")
  public ResponseEntity<Object> findServices(@ModelAttribute AppDTO params) {

    Object result = CommonUtils.getResponseEntity(gwAppService.findApps(params));

    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }

  /**
   * App 상제 조회
   * 
   * @return
   */
  @GetMapping(value = "/v1/app/{id}")
  public ResponseEntity<Object> findServiceDetail(@PathVariable Long id) {

    Object result = CommonUtils.getResponseEntity(gwAppService.findAppDetail(id));

    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }

  /**
   * 신규 App 등록
   * 
   * @param params
   * @return
   */
  @PostMapping(value = "/v1/app")
  public ResponseEntity<Object> registService(@RequestBody AppDTO params) {

    gwAppService.registApp(params);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<Object>(result, HttpStatus.CREATED);
  }

  /**
   * App 수정
   * 
   * @param params
   * @return
   */
  @PutMapping(value = "/v1/app/{id}")
  public ResponseEntity<Object> updateService(@RequestBody AppDTO params, @PathVariable Long id) {

    gwAppService.updateApp(params, id);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<Object>(result, HttpStatus.CREATED);
  }
}
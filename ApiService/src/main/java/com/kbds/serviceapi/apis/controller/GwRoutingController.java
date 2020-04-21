package com.kbds.serviceapi.apis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kbds.serviceapi.apis.dto.RoutingDTO;
import com.kbds.serviceapi.apis.service.GwRoutingService;
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
public class GwRoutingController {

  @Autowired
  GwRoutingService gwRoutingService;

  /**
   * 전체 Routing 서비스 목록 조회 API
   * 
   * @return
   */
  @GetMapping(value = "/v1/routes")
  public ResponseEntity<Object> selectRoutingList(@ModelAttribute RoutingDTO params) {

    Object result = CommonUtils.getResponseEntity(gwRoutingService.findServices(params));

    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }

  /**
   * Routing 특정 서비스 조회 API
   * 
   * @return
   */
  @GetMapping(value = "/v1/routes/{id}")
  public ResponseEntity<Object> selectRoutingDetail(@PathVariable Long id) {

    Object result = CommonUtils.getResponseEntity(gwRoutingService.findServiceDetail(id));

    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }

  /**
   * 신규 Routing 서비스 등록
   * 
   * @param params
   * @return
   */
  @PostMapping(value = "/v1/routes")
  public ResponseEntity<Object> registRoutingService(@RequestBody RoutingDTO params) {

    gwRoutingService.registService(params);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<Object>(result, HttpStatus.CREATED);
  }

  /**
   * Routing 서비스 수정
   * 
   * @param params
   * @return
   */
  @PutMapping(value = "/v1/routes/{id}")
  public ResponseEntity<Object> updateRoutingService(@PathVariable Long id,
      @RequestBody RoutingDTO params) {

    gwRoutingService.updateService(params, id);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }

  /**
   * Routing 서비스 삭제
   * 
   * @param params
   * @return
   */
  @DeleteMapping(value = "/v1/routes/{id}")
  public ResponseEntity<Object> deleteRoutingService(@PathVariable Long id) {

    gwRoutingService.deleteService(id);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }
}

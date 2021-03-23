package com.kbds.serviceapi.apis.controller;

import com.kbds.serviceapi.framework.dto.SearchDTO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
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
import com.kbds.serviceapi.portal.api.dto.RoutingDTO;
import com.kbds.serviceapi.portal.api.service.GwRoutingService;
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
@Slf4j
@RestController
@RequestMapping("/api/service/v1.0/routes")
public class GwRoutingApiController {

  @Autowired
  GwRoutingService gwRoutingService;

  /**
   * 전체 Routing 서비스 목록 조회 API
   * 
   * @return  서비스 조회 결과
   */
  @GetMapping
  public ResponseEntity<Object> findServices(@ModelAttribute SearchDTO searchDTO, HttpServletRequest httpServletRequest) {

    Object result = CommonUtils.getResponseEntity(gwRoutingService.findServices(searchDTO));

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * G/W Routing Bean으로 등록할 정보들 조회 API
   * 
   * @return Gateway에 등록할 라우팅 목록
   */
  @GetMapping(value = "/gateway")
  public ResponseEntity<Object> findGwServices() {

    Object result = CommonUtils.getResponseEntity(gwRoutingService.findGwServices());

    return new ResponseEntity<>(result, HttpStatus.OK);
  }


  /**
   * Routing 특정 서비스 조회 API
   * 
   * @return  서비스 상세 데이터
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<Object> findServiceDetail(@PathVariable Long id) {

    Object result = CommonUtils.getResponseEntity(gwRoutingService.findServiceDetail(id));

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * 신규 Routing 서비스 등록
   * 
   * @param params  등록 파라미터
   * @return  등록 결과
   */
  @PostMapping
  public ResponseEntity<Object> registerService(@RequestBody @Valid RoutingDTO params) {

    gwRoutingService.registerService(params);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }

  /**
   * Routing 서비스 수정
   * 
   * @param params  수정 파라미터
   * @return  수정 결과
   */
  @PutMapping(value = "/{id}")
  public ResponseEntity<Object> updateService(@PathVariable Long id,
      @RequestBody @Valid RoutingDTO params) {

    gwRoutingService.updateService(params, id);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * Routing 서비스 삭제
   * 
   * @param id  삭제할 ID
   * @return  삭제 결과
   */
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Object> deleteService(@PathVariable Long id) {

    gwRoutingService.deleteService(id);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}

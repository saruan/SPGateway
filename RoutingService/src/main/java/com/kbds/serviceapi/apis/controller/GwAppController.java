package com.kbds.serviceapi.apis.controller;

import com.kbds.serviceapi.apis.dto.AppDTO;
import com.kbds.serviceapi.apis.service.GwAppService;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import javax.validation.Valid;
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

/**
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
  public ResponseEntity<Object> findApps(@ModelAttribute SearchDTO searchDTO) {

    Object result = CommonUtils.getResponseEntity(gwAppService.findApps(searchDTO));

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * App 상제 조회
   *
   * @return
   */
  @GetMapping(value = "/v1/app/{appId}")
  public ResponseEntity<Object> findAppDetail(@PathVariable Long appId) {

    Object result = CommonUtils.getResponseEntity(gwAppService.findAppDetail(appId));

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * 신규 App 등록
   *
   * @param params
   * @return
   */
  @PostMapping(value = "/v1/app")
  public ResponseEntity<Object> registerApp(@RequestBody @Valid AppDTO params) {

    gwAppService.registerApp(params);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }

  /**
   * App 수정
   *
   * @param params
   * @return
   */
  @PutMapping(value = "/v1/app/{appId}")
  public ResponseEntity<Object> updateApp(@RequestBody @Valid AppDTO params,
      @PathVariable Long appId) {

    gwAppService.updateApp(params, appId);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * App 삭제
   *
   * @param appId
   * @return
   */
  @DeleteMapping(value = "/v1/app/{appId}")
  public ResponseEntity<Object> deleteApp(@PathVariable Long appId) {

    gwAppService.deleteApp(appId);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}

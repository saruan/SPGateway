package com.kbds.auth.cluster.controller;

import com.kbds.auth.cluster.service.GatewayClusterService;
import com.kbds.auth.common.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  Class Name     : GatewayClusterController.java
 *  Description    : Gateway 클러스터 관련 API 컨트롤러
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-04             구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@RestController
@RequestMapping("/auth")
public class GatewayClusterController {

  @Autowired
  GatewayClusterService gwClusterService;

  /**
   * JWT Token 생성 API
   *
   * @return  생성된 JWT Token
   */
  @GetMapping(value = "/v1.0/oauth/jwt")
  public ResponseEntity<Object> generateJWT() {

    Object result = CommonUtils.getResponseEntity(gwClusterService.generateJWT());

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * G/W 클러스터 목록 조회
   *
   * @return  등록된 G/W 클러스터 목록
   */
  @GetMapping(value = "/v1.0/jwt/cluster")
  public ResponseEntity<Object> selectAllClusters() {

    Object result = CommonUtils.getResponseEntity(gwClusterService.selectAllClusters());

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}

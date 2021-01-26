package com.kbds.auth.apps.cluster.controller;

import com.kbds.auth.apps.cluster.dto.GatewayClusterDTO;
import com.kbds.auth.apps.cluster.service.GatewayClusterService;
import com.kbds.auth.common.utils.CommonUtils;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/auth/v1.0/cluster")
public class GatewayClusterController {

  private final GatewayClusterService gwClusterService;

  public GatewayClusterController(GatewayClusterService gwClusterService) {
    this.gwClusterService = gwClusterService;
  }

  /**
   * G/W 클러스터 목록 조회
   *
   * @return 등록된 G/W 클러스터 목록
   */
  @GetMapping
  public ResponseEntity<Object> selectClustersByConditions() {

    Object result = CommonUtils.getResponseEntity(gwClusterService.selectAllClusters());

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * G/W 클러스터 등록
   *
   * @return 등록 결과
   */
  @PostMapping
  public ResponseEntity<Object> registerCluster(
      @ModelAttribute @Valid GatewayClusterDTO gatewayClusterDTO,
      @RequestPart("file") MultipartFile fileList) {

    gwClusterService.registerCluster(gatewayClusterDTO, fileList);

    Object result = CommonUtils.getResponseEntity(true);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}

package com.kbds.serviceapi.portal.code.controller;

import com.kbds.serviceapi.apis.code.ServiceAuthType;
import com.kbds.serviceapi.apis.code.core.AbstractCode;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.portal.code.dto.CodeDTO;
import com.kbds.serviceapi.portal.code.service.CodeService;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  File  Name     : CodeController
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-10-14             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/portal/v1.0/code")
public class CodeController {

  @Autowired
  CodeService codeService;

  /**
   * 서비스 코드 조회
   * @param type 코드 클래스명
   * @return 결과 리스트
   */
  @GetMapping("/{type}")
  public ResponseEntity<Object> getCode(@PathVariable String type) {

    return new ResponseEntity<>(CommonUtils.getResponseEntity(codeService.convertEnumToValues(type))
        , HttpStatus.OK);
  }

}

package com.kbds.auth.apps.saml.controller;

import com.kbds.auth.apps.saml.dto.SamlDTO;
import com.kbds.auth.apps.saml.service.SamlService;
import com.kbds.auth.common.dto.ResponseDTO;
import com.kbds.auth.common.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  File  Name     : SamlController
 *  Description    : SAML Assertion 처리용 Controller
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-01-14         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/auth/v1.0/saml")
public class SamlController {

  private final SamlService samlService;

  public SamlController(SamlService samlService) {

    this.samlService = samlService;
  }

  /**
   * SAML Assertion
   *
   * @return 등록 결과 정보
   */
  @PostMapping(value = "/assertion")
  public ResponseEntity<Object> generateSamlAssertion(@RequestBody SamlDTO samlDTO) {

    ResponseDTO result = CommonUtils.getResponseEntity(samlService.generateSamlAssertion(samlDTO));

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }

  /**
   * SAML Assertion Validation Check
   *
   * @return  검증 결과
   */
  @PostMapping(value = "/assertion/validate")
  public ResponseEntity<Object> validateSamlAssertion(
      @RequestParam(value = "saml") String assertion) {

    ResponseDTO result = CommonUtils.getResponseEntity(samlService.validateSamlAssertion(assertion));

    return new ResponseEntity<>(result, HttpStatus.CREATED);
  }
}

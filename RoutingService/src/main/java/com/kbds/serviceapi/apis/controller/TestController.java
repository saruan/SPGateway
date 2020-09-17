package com.kbds.serviceapi.apis.controller;

import com.kbds.serviceapi.apis.dto.AppDTO;
import com.kbds.serviceapi.common.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  File  Name     : TestController
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0                	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@RestController
@RequestMapping("/test/")
public class TestController {

  @GetMapping(value = "/test/")
  public ResponseEntity<Object> findApps(@ModelAttribute AppDTO params) {

    return new ResponseEntity<>("result", HttpStatus.OK);
  }
}

package com.kbds.serviceapi.apis.code;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <pre>
 *  File  Name     :
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
@ExtendWith(MockitoExtension.class)
class ServiceLoginTypeTest {

  @Test
  void getCode() {

    assertEquals(ServiceLoginType.APPKEY.getCode(), "APPKEY");
  }

  @Test
  void getType() {

    assertEquals(ServiceLoginType.APPKEY.getType(), "API_KEY");
  }

  @Test
  void getDesc() {

    assertEquals(ServiceLoginType.APPKEY.getDesc(), "API KEY 인증 방식");
  }
}
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
class ServiceAuthTypeTest {

  @Test
  void getType() {

    assertEquals(ServiceAuthType.PUBLIC.getType(), "public");
  }

  @Test
  void getDesc() {

    assertEquals(ServiceAuthType.PUBLIC.getDesc(), "public");
  }

  @Test
  void getCode() {

    assertEquals(ServiceAuthType.PUBLIC.getCode(), "PUBLIC");
  }
}
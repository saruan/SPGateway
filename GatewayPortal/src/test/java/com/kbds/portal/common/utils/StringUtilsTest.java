package com.kbds.portal.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <pre>
 *  File  Name     : StringUtilsTest
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-03          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@ExtendWith(MockitoExtension.class)
public class StringUtilsTest {

  @Test
  void 파라미터_널_체크() {

    String param1 = "A";
    String param2 = "B";

    assertEquals(false, StringUtils.isEmptyParams(param1, param2));
  }

  @Test
  void 파라미터_널_존재_오류() {

    String param1 = "";
    String param2 = null;

    assertEquals(true, StringUtils.isEmptyParams(param1, param2));
  }

}

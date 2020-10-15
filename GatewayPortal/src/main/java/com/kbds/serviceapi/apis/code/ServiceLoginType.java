package com.kbds.serviceapi.apis.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kbds.serviceapi.apis.code.core.AbstractCode;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *  File  Name     : ServiceLoginType
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-09-25         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Getter
@AllArgsConstructor
public enum ServiceLoginType implements AbstractCode {

  OAUTH("1", "Oauth2.0 방식"),
  APPKEY("2", "APPKEY 방식");

  @JsonValue
  private final String type;
  private final String desc;

  public String getDesc(){

    return name();
  }
}

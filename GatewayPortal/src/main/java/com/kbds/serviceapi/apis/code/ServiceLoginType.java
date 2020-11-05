package com.kbds.serviceapi.apis.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kbds.serviceapi.apis.code.core.AbstractCode;
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

  NONE("인증 없음", "인증 없이 사용"),
  OAUTH("OAUTH2.0", "Oauth2.0 인증 방식"),
  APPKEY("API_KEY", "API KEY 인증 방식");

  final private String type;
  final private String desc;

  public String getCode() {

    return name();
  }

  @JsonCreator
  public static ServiceLoginType findByValue(String value){

    return ServiceLoginType.valueOf(value);
  }
}

package com.kbds.serviceapi.apis.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kbds.serviceapi.apis.code.core.AbstractCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *  File  Name     : ServiceAuthType
 *  Description    : 서비스 인증 타입
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-10-05             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Getter
@AllArgsConstructor
public enum ServiceAuthType implements AbstractCode {

  PUBLIC("public", "public"),
  SECURITY("sercurity", "security");

  private final String type;
  private final String desc;

  public String getCode(){

    return name();
  }

  @JsonCreator
  public static ServiceAuthType findByValue(String value){

    return ServiceAuthType.valueOf(value);
  }
}

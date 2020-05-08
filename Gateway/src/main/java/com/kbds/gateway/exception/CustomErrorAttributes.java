package com.kbds.gateway.exception;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import com.kbds.gateway.code.GatewayExceptionCode;

/**
 * 
 *
 * <pre>
 *  Class Name     : CustomErrorAttributes.java
 *  Description    : Gateway에서 사용할 공통 Error Handler 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-22     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Component
public class CustomErrorAttributes implements ErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

    Map<String, Object> errorAttributes = new LinkedHashMap<>();

    Throwable error = getError(request);

    // 에러의 종류가 GatewayExcpetion일 경우 정해진 규격에 맞춰 화면에 전달한다.
    if (error instanceof GatewayException) {

      errorAttributes.put("timestamp", new Date());
      errorAttributes.put("path", request.path());
      errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
      errorAttributes.put("resultCd",
          GatewayExceptionCode.valueOf(((GatewayException) error).getMessage()).getCode());
      errorAttributes.put("resultMsg",
          GatewayExceptionCode.valueOf(((GatewayException) error).getMessage()).getMsg());
    }
    // 그 이외의 정해진 규격이 아닌 Gateway 오류일 경우 아래와 같이 설정한다.
    else {

      errorAttributes.put("timestamp", new Date());
      errorAttributes.put("path", request.path());
      errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
      errorAttributes.put("resultCd", GatewayExceptionCode.GWE0001.getCode());
      errorAttributes.put("resultMsg", error.getMessage());
    }

    return errorAttributes;
  }

  @Override
  public Throwable getError(ServerRequest request) {

    return (Throwable) request.attribute(this.getClass().getName())
        .orElseThrow(() -> new IllegalStateException("Exception Attribute를 찾을 수 없습니다."));
  }

  @Override
  public void storeErrorInformation(Throwable error, ServerWebExchange exchange) {
    exchange.getAttributes().putIfAbsent(this.getClass().getName(), error);
  }
}

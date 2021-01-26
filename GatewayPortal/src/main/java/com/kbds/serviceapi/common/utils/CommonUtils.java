package com.kbds.serviceapi.common.utils;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.kbds.serviceapi.common.code.CommonCode;
import com.kbds.serviceapi.common.feign.GatewayClient;
import com.kbds.serviceapi.framework.dto.ResponseDTO;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *  Class Name     : CommonUtils.java
 *  Description    : 일반 공통 유틸 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-16     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Component
public class CommonUtils {

  // 로그용 변수
  private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
  private static String secretKey;
  private static String basicAuthorization;
  private static GatewayClient gatewayClient;

  @Value("${jwt.secret-key}")
  public void setSecretKey(String secretKey) {

    CommonUtils.secretKey = secretKey;
  }

  @Value("${services.auth.basic}")
  public void setBasicAuthorization(String basicAuthorization) {

    CommonUtils.basicAuthorization = basicAuthorization;
  }

  @Autowired
  private void setGatewayClient(GatewayClient gatewayClient) {

    CommonUtils.gatewayClient = gatewayClient;
  }

  /**
   * 공통 Response Model 생성 유틸 메소드
   *
   * @param params  데이터 결과
   * @return  ResponseDTO 객체
   */
  public static ResponseDTO getResponseEntity(Object params) {

    ResponseDTO responseDTO = new ResponseDTO();

    responseDTO.setResultCode(CommonCode.SUCCESS.getCode());
    responseDTO.setResultMessage(CommonCode.SUCCESS.getMessage());
    responseDTO.setResultData(params);

    return responseDTO;
  }

  /**
   * RoutingService의 최신 데이터를 Gateway에 변경 요청한다.
   *
   */
  public static void refreshGatewayRoutes() {

    JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
    Algorithm algorithm = Algorithm.HMAC256(secretKey);

    Map<String, String> headers = new HashMap<>();

    headers.put(HttpHeaders.AUTHORIZATION, CommonCode.TOKEN_PREFIX + builder.sign(algorithm));

    try {

      gatewayClient.callRefreshGateway(headers);

    } catch (Exception e) {

      logger.error(e.toString());
    }

  }

  /**
   * 인증서버에서 사용하는 헤더 정보 Return
   * @return  Header 객체
   */
 public static Map<String, String> setFeignCommonHeaders(){

   Map<String, String> headers = new HashMap<>();

   headers.put(HttpHeaders.AUTHORIZATION, basicAuthorization);
   
   return headers;
 }
}

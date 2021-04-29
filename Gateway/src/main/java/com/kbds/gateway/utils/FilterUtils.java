package com.kbds.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbds.gateway.code.GatewayCode;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.server.ServerWebExchange;

/**
 * <pre>
 *  File  Name     : FilterUtils
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-23          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public class FilterUtils {

  final static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 서블릿 종류가 요청일 경우 데이터 적재
   *
   * @param exchange ServerWebExchange
   * @return 요청 데이터
   */
  public static Map<String, Object> getRequestParams(ServerWebExchange exchange)
      throws JsonProcessingException {

    /* Request Body 추출 */
    Object attribute = exchange.getAttribute(GatewayCode.CACHE_REQUEST_BODY.getCode());
    String requestBody = "";

    DataBuffer buffer = (DataBuffer) attribute;

    if (buffer != null) {

      requestBody = StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString();
    }

    if (!isJSONValid(requestBody)) {

      return new HashMap<>();
    } else {

      return objectMapper.readValue(requestBody, Map.class);
    }
  }

  /**
   * JSON String 여부 체크
   *
   * @param params 전문
   * @return JSON 양식 결과 유무
   */
  public static boolean isJSONValid(String params) {

    try {

      if(GatewayCode.BLANK.getCode().equals(params)){

        return false;
      }

      objectMapper.readTree(params);

      return true;
    } catch (IOException e) {

      return false;
    }
  }

  /**
   * HS Key 검증
   * @param hsKey HS Key 값
   * @param appKey  검증 Secret 값
   * @param requestBody Body 값
   * @return  검증 결과
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws InvalidKeyException  InvalidKeyException
   */
  public static boolean isValidHsKey(String hsKey, String appKey, byte[] requestBody)
      throws NoSuchAlgorithmException, InvalidKeyException {

    if(StringUtils.isEmptyParams(appKey, hsKey)){

      return false;
    }

    byte[] keys = appKey.getBytes();
    SecretKeySpec secretKeySpec = new SecretKeySpec(keys, HmacAlgorithms.HMAC_SHA_256.getName());
    Mac mac = Mac.getInstance(HmacAlgorithms.HMAC_SHA_256.getName());
    mac.init(secretKeySpec);

    return hsKey.equals(Base64.encodeBase64String(mac.doFinal(requestBody)));
  }

  /**
   * ByteBuffer to byte[]
   *
   * @param byteBuffer ByteBuffer 값
   * @return  Byte Array
   */
  public static byte[] getByteArrayFromByteBuffer(ByteBuffer byteBuffer) {
    byte[] bytesArray = new byte[byteBuffer.remaining()];
    byteBuffer.get(bytesArray, 0, bytesArray.length);
    return bytesArray;
  }
}

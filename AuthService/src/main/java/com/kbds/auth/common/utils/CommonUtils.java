package com.kbds.auth.common.utils;

import org.springframework.stereotype.Component;
import com.kbds.auth.common.code.AuthCode;
import com.kbds.auth.common.dto.ResponseDTO;

/**
 * 
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
 *
 */
@Component
public class CommonUtils {

  /**
   * 공통 Response Model 생성 유틸 메소드
   * 
   * @param params
   * @return
   */
  public static ResponseDTO getResponseEntity(Object params) {

    ResponseDTO responseDTO = new ResponseDTO();

    responseDTO.setResultCode(AuthCode.SUCCESS.getCode());
    responseDTO.setResultMessage(AuthCode.SUCCESS.getDesc());
    responseDTO.setResultData(params);

    return responseDTO;
  }
}

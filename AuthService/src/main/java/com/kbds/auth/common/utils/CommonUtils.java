package com.kbds.auth.common.utils;

import com.kbds.auth.common.code.ConstantsCode;
import com.kbds.auth.common.dto.ResponseDTO;
import org.springframework.stereotype.Component;

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
   * @param params  Service 호출 결과
   * @return  ResponseDTO 객체
   */
  public static ResponseDTO getResponseEntity(Object params) {

    ResponseDTO responseDTO = new ResponseDTO();

    responseDTO.setResultCode(ConstantsCode.SUCCESS.getCode());
    responseDTO.setResultMessage(ConstantsCode.SUCCESS.getMessage());
    responseDTO.setResultData(params);

    return responseDTO;
  }
}

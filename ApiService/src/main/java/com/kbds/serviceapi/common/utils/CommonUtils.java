package com.kbds.serviceapi.common.utils;

import com.kbds.serviceapi.common.code.CommonCode;
import com.kbds.serviceapi.framework.dto.ResponseDTO;

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
public class CommonUtils {

  /**
   * 공통 Response Model 생성 유틸 클래스
   * 
   * @param params
   * @return
   */
  public static ResponseDTO getResponseEntity(Object params) {

    ResponseDTO responseDTO = new ResponseDTO();

    responseDTO.setResultCode(CommonCode.SUCCESS.getResultCode());
    responseDTO.setResultMessage(CommonCode.SUCCESS.getResultMessage());
    responseDTO.setResultData(params);

    return responseDTO;
  }
}

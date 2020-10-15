package com.kbds.serviceapi.portal.code.service;

import com.kbds.serviceapi.apis.code.core.AbstractCode;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.code.CommonCode;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.portal.code.dto.CodeDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : CodeService
 *  Description    : 코드 관리 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-10-14          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class CodeService {

  /**
   * Enum -> List<CodeDTO> 변환
   *
   * @param classNm Class Name
   * @return 상세 목록
   */
  public List<CodeDTO> convertEnumToValues(
      String classNm) {

    try {
      Class<? extends AbstractCode> enumClass = (Class<? extends AbstractCode>) Class
          .forName(CommonCode.BASE_CODE_PACKAGE.getCode() + classNm);

      return Arrays.stream(enumClass.getEnumConstants()).
          map(t -> new CodeDTO(t.getType(), t.getDesc())
          ).collect(Collectors.toList());
    } catch (ClassNotFoundException e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }
}

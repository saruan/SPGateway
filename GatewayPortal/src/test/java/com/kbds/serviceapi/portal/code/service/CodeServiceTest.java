package com.kbds.serviceapi.portal.code.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kbds.serviceapi.apis.code.ServiceLoginType;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.portal.code.dto.CodeDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <pre>
 *  File  Name     :
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0                	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@ExtendWith(MockitoExtension.class)
class CodeServiceTest {

  @InjectMocks
  CodeService codeService;

  @Test
  void 목적_코드_목록_조회() {

    String enumNm = "ServiceLoginType";

    List<CodeDTO> codeDTOS = codeService.convertEnumToValues(enumNm);

    assertEquals(3, codeDTOS.size());
    assertEquals(ServiceLoginType.APPKEY.getCode(),
        codeDTOS.stream().filter(e -> e.getType().equals(ServiceLoginType.APPKEY.getCode()))
            .findAny().get().getType());
  }

  @Test
  void 목족_코드_조회_실패() {

    String enumNm = "ServiceLoginType222";

    BizException ex = assertThrows(BizException.class,
        () -> codeService.convertEnumToValues(enumNm));
    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }
}
package com.kbds.serviceapi.portal.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.portal.app.dto.AppDTO;
import com.kbds.serviceapi.portal.app.entity.GwServiceAppMapping;
import com.kbds.serviceapi.portal.app.entity.key.GwServiceAppMappingKey;
import com.kbds.serviceapi.portal.app.repository.GwServiceAppMappingRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

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
class GwServiceAppMappingServiceTest {

  @Mock
  GwServiceAppMappingRepository gwServiceAppMappingRepository;

  @InjectMocks
  GwServiceAppMappingService gwServiceAppMappingService;

  @Test
  void 매핑_데이터_등록() {

    List<Long> serviceIdList = new ArrayList<>();
    GwServiceAppMappingKey key = new GwServiceAppMappingKey(1L, 1L);
    GwServiceAppMapping param = new GwServiceAppMapping(key);

    Long appId = 1L;
    serviceIdList.add(1L);
    serviceIdList.add(2L);
    AppDTO appDTO = new AppDTO(appId, "APP_수정", "APP_KEY_MODIFY", "APP_수정",
        "N", serviceIdList, "1", "1", new Date(), new Date());

    when(gwServiceAppMappingRepository
        .deleteByIdAppIdAndIdServiceIdNotIn(appId, appDTO.getServiceId())).thenReturn(1L);

    doReturn(param).when(gwServiceAppMappingRepository).save(param);

    gwServiceAppMappingService.registerServiceAppMapping(appDTO, appId);
  }

  @Test
  void 매핑_데이터_등록_기타_실패() {

    List<Long> serviceIdList = new ArrayList<>();

    Long appId = 1L;
    serviceIdList.add(1L);
    serviceIdList.add(2L);
    AppDTO appDTO = new AppDTO(appId, "APP_수정", "APP_KEY_MODIFY", "APP_수정",
        "N", serviceIdList, "1", "1", new Date(), new Date());

    when(gwServiceAppMappingRepository
        .deleteByIdAppIdAndIdServiceIdNotIn(appId, appDTO.getServiceId()))
        .thenThrow(new ArrayIndexOutOfBoundsException());

    BizException ex = assertThrows(BizException.class,
        () -> gwServiceAppMappingService.registerServiceAppMapping(appDTO, appId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }
}
package com.kbds.serviceapi.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.kbds.serviceapi.apis.dto.AppDTO;
import com.kbds.serviceapi.apis.entity.GwApp;
import com.kbds.serviceapi.apis.entity.GwServiceAppMapping;
import com.kbds.serviceapi.apis.entity.key.GwServiceAppMappingKey;
import com.kbds.serviceapi.apis.querydsl.GwAppCustomRepository;
import com.kbds.serviceapi.apis.querydsl.GwRoutingCustomRepository;
import com.kbds.serviceapi.apis.repository.GwAppRepository;
import com.kbds.serviceapi.apis.repository.GwRoutingRepository;
import com.kbds.serviceapi.apis.repository.GwServiceAppMappingRepository;
import com.kbds.serviceapi.apis.service.GwAppService;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.exception.BizException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * <pre>
 *  File  Name     : AppServiceTest
 *  Description    : APP 관련 단위 테스트
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-07-21          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@ExtendWith(MockitoExtension.class)
public class AppServiceTest {

  @Spy
  @InjectMocks
  GwAppService gwAppService;

  @Mock
  GwAppRepository gwAppRepository;

  @Mock
  GwAppCustomRepository gwAppCustomRepository;

  @Mock
  GwRoutingRepository gwRoutingRepository;

  @Mock
  GwRoutingCustomRepository gwRoutingCustomRepository;

  @Mock
  GwServiceAppMappingRepository gwServiceAppMappingRepository;

  @Mock
  CommonUtils commonUtils;

  @Spy
  @InjectMocks
  ModelMapper modelMapper;

  SearchDTO searchConditions;
  AppDTO registAppDTO;
  AppDTO updateAppDTO;
  List<AppDTO> appListDTO;
  List<Long> serviceIdList;
  Long appId;

  /**
   * 기초 데이터 적재
   */
  @BeforeEach
  void initData() {

    appId = 1L;

    serviceIdList = new ArrayList<>();
    serviceIdList.add(1L);
    serviceIdList.add(2L);

    registAppDTO = new AppDTO(appId, "APP_등록", "", "APP_설명",
        "Y", serviceIdList, "1", "1", new Date(), new Date());

    searchConditions = SearchDTO.builder().name("APP_등록").build();

    updateAppDTO = new AppDTO(appId, "APP_수정", "APP_KEY_MODIFY", "APP_수정",
        "N", serviceIdList, "1", "1", new Date(), new Date());

    appListDTO = new ArrayList<>();
    appListDTO.add(registAppDTO);

    ReflectionTestUtils.setField(CommonUtils.class, "secretKey", "secretKey");
    ReflectionTestUtils.setField(gwAppService, "modelMapper", new ModelMapper());

  }

  @Test
  void APP_리스트_조회_테스트() {

    when(gwAppCustomRepository.findByConditions(searchConditions)).thenReturn(appListDTO);

    gwAppService.findApps(searchConditions);
  }

  @Test
  void APP_등록_테스트() {

    doReturn(true).when(gwAppService).isValidAppNm(registAppDTO);
    doReturn(true).when(gwAppService).isValidServiceId(registAppDTO);

    GwApp gwApp = modelMapper.map(registAppDTO, GwApp.class);

    when(gwAppService.setGwAppWithAppKey(registAppDTO)).thenReturn(gwApp);
    when(gwAppRepository.save(gwApp)).thenReturn(gwApp);

    // Mapping 테이블에 데이터 등록
    List<GwServiceAppMapping> params = new ArrayList<GwServiceAppMapping>();

    for (int i = 0; i < serviceIdList.size(); i++) {

      GwServiceAppMappingKey key = new GwServiceAppMappingKey(serviceIdList.get(i), appId);
      GwServiceAppMapping param = new GwServiceAppMapping(key);

      params.add(param);
    }

    when(gwServiceAppMappingRepository.saveAll(params)).thenReturn(params);

    gwAppService.registerApp(registAppDTO);
  }

  @Test
  void APP_서비스_ID_유효성_테스트() {

    doReturn(false).when(gwAppService).isValidServiceId(registAppDTO);

    BizException ex = assertThrows(BizException.class, () -> {

      gwAppService.registerApp(registAppDTO);
    });

    assertEquals(ex.getMessage(), BizExceptionCode.COM005.getCode());
  }

  @Test
  void APP_이름_중복_테스트() {

    doReturn(true).when(gwAppService).isValidServiceId(registAppDTO);
    doReturn(false).when(gwAppService).isValidAppNm(registAppDTO);

    BizException ex = assertThrows(BizException.class, () -> {

      gwAppService.registerApp(registAppDTO);
    });

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }

  @Test
  void APP_수정_테스트() {

    doReturn(true).when(gwAppService).isValidServiceId(updateAppDTO);
    doReturn(true).when(gwAppService).isValidModifyAppNm(updateAppDTO, appId);
    doReturn(false).when(gwAppService).isEmptyAppKey(updateAppDTO.getAppKey());

    GwApp gwApp = modelMapper.map(updateAppDTO, GwApp.class);

    when(gwAppRepository.findByAppId(appId)).thenReturn(gwApp);
    when(gwAppRepository.save(gwApp)).thenReturn(gwApp);

    // Mapping 테이블에 데이터 등록
    List<GwServiceAppMapping> params = new ArrayList<GwServiceAppMapping>();

    when(gwServiceAppMappingRepository.
        deleteByIdAppIdAndIdServiceIdNotIn(appId, serviceIdList)).thenReturn(1L);

    for (int i = 0; i < serviceIdList.size(); i++) {

      GwServiceAppMappingKey key = new GwServiceAppMappingKey(serviceIdList.get(i), appId);
      GwServiceAppMapping param = new GwServiceAppMapping(key);

      params.add(param);
    }

    when(gwServiceAppMappingRepository.saveAll(params)).thenReturn(params);

    gwAppService.updateApp(updateAppDTO, appId);
  }

  @Test
  void APP_수정_이름_중복_테스트() {

    doReturn(true).when(gwAppService).isValidServiceId(updateAppDTO);
    doReturn(false).when(gwAppService).isValidModifyAppNm(updateAppDTO, appId);

    BizException ex = assertThrows(BizException.class, () -> {

      gwAppService.updateApp(updateAppDTO, appId);
    });

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }

  @Test
  void APP_수정_앱키_공백_테스트() {

    updateAppDTO.setAppKey(null);

    BizException ex = assertThrows(BizException.class, () -> {

      gwAppService.updateApp(updateAppDTO, appId);
    });

    assertEquals(ex.getMessage(), BizExceptionCode.COM002.getCode());
  }
}

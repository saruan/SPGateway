package com.kbds.serviceapi.portal.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.kbds.serviceapi.apis.code.ServiceAuthType;
import com.kbds.serviceapi.apis.code.ServiceLoginType;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.portal.api.dto.RoutingDTO;
import com.kbds.serviceapi.portal.api.entity.GwService;
import com.kbds.serviceapi.portal.api.repository.GwRoutingRepository;
import com.kbds.serviceapi.portal.app.dto.AppDTO;
import com.kbds.serviceapi.portal.app.entity.GwApp;
import com.kbds.serviceapi.portal.app.repository.GwAppRepository;
import com.kbds.serviceapi.portal.app.repository.GwServiceAppMappingRepository;
import com.kbds.serviceapi.portal.app.service.GwAppService;
import com.kbds.serviceapi.portal.app.service.GwServiceAppMappingService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class GwAppServiceTest {

  @Spy
  @InjectMocks
  GwAppService gwAppService;

  @Mock
  GwAppRepository gwAppRepository;

  @Mock
  GwServiceAppMappingService gwServiceAppMappingService;

  @Mock
  GwServiceAppMappingRepository gwServiceAppMappingRepository;

  @Mock
  GwRoutingRepository gwRoutingRepository;

  @Spy
  @InjectMocks
  ModelMapper modelMapper;

  SearchDTO searchConditions;
  AppDTO registerAppDTO;
  AppDTO updateAppDTO;
  List<AppDTO> appListDTO;
  List<Long> serviceIdList;
  Map<GwApp, List<GwService>> gwApps;
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

    registerAppDTO = new AppDTO(appId, "APP_등록", "", "APP_설명",
        "Y", serviceIdList, "1", "1", new Date(), new Date());

    searchConditions = SearchDTO.builder().name("APP_등록").build();

    updateAppDTO = new AppDTO(appId, "APP_수정", "APP_KEY_MODIFY", "APP_수정",
        "N", serviceIdList, "1", "1", new Date(), new Date());

    appListDTO = new ArrayList<>();
    appListDTO.add(registerAppDTO);

    ReflectionTestUtils.setField(CommonUtils.class, "secretKey", "secretKey");
    ReflectionTestUtils.setField(gwAppService, "modelMapper", new ModelMapper());

  }

  @Test
  void APP_리스트_조회_테스트() {

    when(gwAppRepository.findByConditions(searchConditions)).thenReturn(appListDTO);

    gwAppService.findApps(searchConditions);
  }

  @Test
  void APP_등록_테스트() {

    doReturn(true).when(gwAppService).isValidAppNm(registerAppDTO);
    doReturn(true).when(gwAppService).isValidServiceId(registerAppDTO);

    GwApp gwApp = modelMapper.map(registerAppDTO, GwApp.class);

    when(gwAppService.setGwAppWithAppKey(registerAppDTO)).thenReturn(gwApp);
    when(gwAppRepository.save(gwApp)).thenReturn(gwApp);

    doNothing().when(gwServiceAppMappingService).registerServiceAppMapping(registerAppDTO, appId);

    gwAppService.registerApp(registerAppDTO);
  }

  @Test
  void APP_서비스_ID_유효성_테스트() {

    doReturn(3L).when(gwRoutingRepository).countByServiceIdIn(registerAppDTO.getServiceId());

    BizException ex = assertThrows(BizException.class, () -> gwAppService.registerApp(
        registerAppDTO));

    assertEquals(ex.getMessage(), BizExceptionCode.COM005.getCode());
  }

  @Test
  void APP_이름_중복_테스트() {

    doReturn(true).when(gwAppService).isValidServiceId(registerAppDTO);
    doReturn(appListDTO).when(gwAppRepository).findByAppNm(registerAppDTO.getAppNm());

    BizException ex = assertThrows(BizException.class, () -> gwAppService.registerApp(
        registerAppDTO));

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

    doNothing().when(gwServiceAppMappingService).registerServiceAppMapping(updateAppDTO, appId);

    gwAppService.updateApp(updateAppDTO, appId);
  }

  @Test
  void APP_수정_이름_중복_테스트() {

    doReturn(true).when(gwAppService).isValidServiceId(updateAppDTO);
    doReturn(1).when(gwAppRepository).countByAppNmAndAppIdNot(updateAppDTO.getAppNm(), appId);

    BizException ex = assertThrows(BizException.class,
        () -> gwAppService.updateApp(updateAppDTO, appId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }

  @Test
  void APP_수정_서비스_ID_오류_테스트() {

    doReturn(false).when(gwAppService).isValidServiceId(updateAppDTO);

    BizException ex = assertThrows(BizException.class,
        () -> gwAppService.updateApp(updateAppDTO, appId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM005.getCode());
  }

  @Test
  void APP_상세_조회_테스트() {

    gwApps = new HashMap<>();
    List<GwService> gwServices;
    RoutingDTO
        registerRoutingDTO = RoutingDTO.builder().serviceId(1L).serviceNm("등록")
        .servicePath("/regist")
        .serviceDesc("Desc").serviceLoginType(ServiceLoginType.OAUTH).serviceAuthType(
            ServiceAuthType.PUBLIC).regUserNo("1").build();

    GwApp gwApp = modelMapper.map(registerAppDTO, GwApp.class);
    GwService gwService = modelMapper.map(registerRoutingDTO, GwService.class);

    gwServices = new ArrayList<>();
    gwServices.add(gwService);
    gwApps.put(gwApp, gwServices);

    doReturn(gwApps).when(gwAppRepository).findAppDetailById(appId);

    gwAppService.findAppDetail(appId);
  }

  @Test
  void APP_삭제_테스트() {

    doReturn(false).when(gwAppService).isUsingApp(appId);
    doNothing().when(gwAppRepository).deleteById(appId);

    gwAppService.deleteApp(appId);
  }

  @Test
  void APP_삭제_사용중_테스트() {

    doReturn(1L).when(gwServiceAppMappingRepository).countByIdAppId(appId);

    BizException ex = assertThrows(BizException.class, () -> gwAppService.deleteApp(appId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM006.getCode());
  }

  @Test
  void APP_수정_앱키_공백_테스트() {

    updateAppDTO.setAppKey(null);

    BizException ex = assertThrows(BizException.class,
        () -> gwAppService.updateApp(updateAppDTO, appId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM002.getCode());
  }
}

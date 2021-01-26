package com.kbds.serviceapi.portal.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import java.util.ArrayList;
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

@ExtendWith(MockitoExtension.class)
public class GwRoutingServiceTest {

  @Spy
  @InjectMocks
  GwRoutingService gwRoutingService;

  @Mock
  GwRoutingRepository gwRoutingRepository;

  @Spy
  @InjectMocks
  ModelMapper modelMapper;

  SearchDTO searchConditions;
  RoutingDTO registerRoutingDTO;
  RoutingDTO updateRoutingDTO;
  List<RoutingDTO> routingListDTO;
  Long serviceId;

  /**
   * 기초 데이터 적재
   */
  @BeforeEach
  void initData() {

    serviceId = 1L;

    searchConditions = SearchDTO.builder().name("검색").servicePath("/test").build();

    registerRoutingDTO = RoutingDTO.builder().serviceId(1L).serviceNm("등록").servicePath("/register")
        .serviceDesc("Desc").serviceLoginType(ServiceLoginType.OAUTH).serviceAuthType(
            ServiceAuthType.PUBLIC).regUserNo("1").build();

    updateRoutingDTO = RoutingDTO.builder().serviceId(1L).serviceNm("수정").servicePath("/update")
        .serviceDesc("Desc").serviceLoginType(ServiceLoginType.OAUTH)
        .serviceAuthType(ServiceAuthType.PUBLIC).uptUserNo("1").build();

    routingListDTO = new ArrayList<>();
    routingListDTO.add(registerRoutingDTO);

    ReflectionTestUtils.setField(CommonUtils.class, "secretKey", "secretKey");
    ReflectionTestUtils.setField(gwRoutingService, "modelMapper", new ModelMapper());
  }

  @Test
  void API_목록_조회_테스트() {

    doReturn(routingListDTO).when(gwRoutingRepository).findByConditions(searchConditions);

    List<RoutingDTO> result = gwRoutingService.findServices(searchConditions);

    assertEquals(registerRoutingDTO, result.get(0));
  }

  @Test
  void API_목록_조회_실패_테스트() {

    when(gwRoutingRepository.findByConditions(searchConditions))
        .thenThrow(new ArrayIndexOutOfBoundsException());

    BizException ex = assertThrows(BizException.class,
        () -> gwRoutingService.findServices(searchConditions));

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }

  @Test
  void API_GW_등록용_목록_조회_테스트() {

    doReturn(routingListDTO).when(gwRoutingRepository).findByGwConditions();

    List<RoutingDTO> result = gwRoutingService.findGwServices();

    assertEquals(registerRoutingDTO, result.get(0));
  }

  @Test
  void API_GW_등록용_목록_조회_실패_테스트() {

    when(gwRoutingRepository.findByGwConditions())
        .thenThrow(new ArrayIndexOutOfBoundsException());

    BizException ex = assertThrows(BizException.class,
        () -> gwRoutingService.findGwServices());

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }

  @Test
  void API_등록_테스트() {

    GwService gwService = modelMapper.map(registerRoutingDTO, GwService.class);

    doReturn(false).when(gwRoutingRepository).isRegisteredService(registerRoutingDTO);
    doReturn(gwService).when(gwRoutingRepository).save(gwService);

    gwRoutingService.registerService(registerRoutingDTO);
  }

  @Test
  void API_등록_중복_테스트() {

    doReturn(true).when(gwRoutingRepository).isRegisteredService(registerRoutingDTO);

    BizException ex = assertThrows(BizException.class,
        () -> gwRoutingService.registerService(registerRoutingDTO));

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }

  @Test
  void API_수정_테스트() {

    doReturn(true).when(gwRoutingRepository).isValidUpdateData(updateRoutingDTO, serviceId);

    gwRoutingService.updateService(updateRoutingDTO, serviceId);
  }

  @Test
  void API_수정_유효하지_않음_테스트() {

    doReturn(false).when(gwRoutingRepository).isValidUpdateData(updateRoutingDTO, serviceId);

    BizException ex = assertThrows(BizException.class,
        () -> gwRoutingService.updateService(updateRoutingDTO, serviceId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }

  @Test
  void API_수정_DB_오류_테스트() {

    GwService gwService = modelMapper.map(updateRoutingDTO, GwService.class);

    doReturn(gwService).when(gwRoutingRepository).findByServiceId(serviceId);
    doReturn(true).when(gwRoutingRepository).isValidUpdateData(updateRoutingDTO, serviceId);
    when(gwRoutingRepository.save(gwService))
        .thenThrow(new RuntimeException("Exception"));

    BizException ex = assertThrows(BizException.class,
        () -> gwRoutingService.updateService(updateRoutingDTO, serviceId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }

  @Test
  void API_삭제_테스트() {

    Long serviceIdList = 2L;

    doReturn(2L).when(gwRoutingRepository).deleteService(serviceIdList);

    gwRoutingService.deleteService(serviceIdList);
  }

  @Test
  void API_삭제_기타_오류_테스트() {

    Long serviceId = 2L;

    doReturn(2L).when(gwRoutingRepository).countByGwAppIdServiceId(serviceId);

    when(gwRoutingRepository.countByGwAppIdServiceId(serviceId))
        .thenThrow(new ArrayIndexOutOfBoundsException());

    BizException ex = assertThrows(BizException.class,
        () -> gwRoutingService.deleteService(serviceId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }
}

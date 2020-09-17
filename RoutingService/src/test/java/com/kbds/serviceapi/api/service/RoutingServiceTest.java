package com.kbds.serviceapi.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.kbds.serviceapi.apis.dto.RoutingDTO;
import com.kbds.serviceapi.apis.entity.GwService;
import com.kbds.serviceapi.apis.repository.querydsl.GwRoutingCustomRepository;
import com.kbds.serviceapi.apis.repository.GwRoutingRepository;
import com.kbds.serviceapi.apis.service.GwRoutingService;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.exception.BizException;
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
public class RoutingServiceTest {

  @Spy
  @InjectMocks
  GwRoutingService gwRoutingService;

  @Mock
  GwRoutingRepository gwRoutingRepository;

  @Mock
  GwRoutingCustomRepository gwRoutingCustomRepository;

  @Mock
  CommonUtils commonUtils;

  @Spy
  @InjectMocks
  ModelMapper modelMapper;

  SearchDTO searchConditions;
  RoutingDTO registRoutingDTO;
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

    registRoutingDTO = RoutingDTO.builder().serviceId(1L).serviceNm("등록").servicePath("/regist")
        .serviceDesc("Desc").serviceLoginType("1").serviceAuthType("1").regUserNo("1").build();

    updateRoutingDTO = RoutingDTO.builder().serviceId(1L).serviceNm("수정").servicePath("/update")
        .serviceDesc("Desc").serviceLoginType("2").serviceAuthType("2").uptUserNo("1").build();

    routingListDTO = new ArrayList<>();
    routingListDTO.add(registRoutingDTO);

    ReflectionTestUtils.setField(CommonUtils.class, "secretKey", "secretKey");
    ReflectionTestUtils.setField(gwRoutingService, "modelMapper", new ModelMapper());
  }

  @Test
  void API목록_조회_테스트(){

    doReturn(routingListDTO).when(gwRoutingCustomRepository).findByConditions(searchConditions);

    List<RoutingDTO> result = gwRoutingService.findServices(searchConditions);

    assertEquals(registRoutingDTO, result.get(0));
  }

  @Test
  void API등록_테스트() {

    GwService gwService = modelMapper.map(registRoutingDTO, GwService.class);

    doReturn(false).when(gwRoutingCustomRepository).isRegisteredService(registRoutingDTO);
    doReturn(gwService).when(gwRoutingRepository).save(gwService);

    gwRoutingService.registerService(registRoutingDTO);
  }

  @Test
  void API등록_중복_테스트() {

    GwService gwService = modelMapper.map(registRoutingDTO, GwService.class);

    doReturn(true).when(gwRoutingCustomRepository).isRegisteredService(registRoutingDTO);

    BizException ex = assertThrows(BizException.class, () -> {

      gwRoutingService.registerService(registRoutingDTO);
    });

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }

  @Test
  void API수정_테스트() {

    GwService gwService = modelMapper.map(updateRoutingDTO, GwService.class);

    doReturn(gwService).when(gwRoutingRepository).findByServiceId(serviceId);
    doReturn(true).when(gwRoutingCustomRepository).isValidUpdateData(updateRoutingDTO, serviceId);

    gwRoutingService.updateService(updateRoutingDTO, serviceId);
  }

  @Test
  void API수정_중복데이터_테스트() {

    GwService gwService = modelMapper.map(updateRoutingDTO, GwService.class);

    doReturn(gwService).when(gwRoutingRepository).findByServiceId(serviceId);
    doReturn(false).when(gwRoutingCustomRepository).isValidUpdateData(updateRoutingDTO, serviceId);

    BizException ex = assertThrows(BizException.class, () -> {

      gwRoutingService.updateService(updateRoutingDTO, serviceId);
    });

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }

  @Test
  void API수정_데이터없음_테스트() {

    doReturn(null).when(gwRoutingRepository).findByServiceId(serviceId);

    BizException ex = assertThrows(BizException.class, () -> {

      gwRoutingService.updateService(updateRoutingDTO, serviceId);
    });

    assertEquals(ex.getMessage(), BizExceptionCode.COM004.getCode());
  }

  @Test
  void API수정_DB오류_테스트() {

    GwService gwService = modelMapper.map(updateRoutingDTO, GwService.class);

    doReturn(gwService).when(gwRoutingRepository).findByServiceId(serviceId);
    doReturn(true).when(gwRoutingCustomRepository).isValidUpdateData(updateRoutingDTO, serviceId);
    when(gwRoutingRepository.save(gwService))
        .thenThrow(new RuntimeException("Exception"));

    BizException ex = assertThrows(BizException.class, () -> {

      gwRoutingService.updateService(updateRoutingDTO, serviceId);
    });

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }

  @Test
  void API삭제_테스트() {

    Long[] serviceIdList = {1L, 2L};

    doReturn(2L).when(gwRoutingCustomRepository).deleteService(serviceIdList);

    gwRoutingService.deleteService(serviceIdList);
  }
}

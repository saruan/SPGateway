package com.kbds.serviceapi.portal.api.repository.querydsl.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.kbds.serviceapi.apis.code.ServiceAuthType;
import com.kbds.serviceapi.apis.code.ServiceLoginType;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.portal.api.dto.RoutingDTO;
import com.kbds.serviceapi.portal.api.entity.GwService;
import com.kbds.serviceapi.portal.api.repository.GwRoutingRepository;
import com.kbds.serviceapi.portal.api.repository.querydsl.GwRoutingCustomRepository;
import com.kbds.serviceapi.setting.RepositoryTest;
import io.micrometer.core.instrument.search.Search;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
class GwRoutingCustomRepositoryImplTest extends RepositoryTest {

  @Autowired
  GwRoutingRepository gwRoutingRepository;

  @Autowired
  private ModelMapper modelMapper;

  private GwService gwService;

  @BeforeEach
  public void setUp() {

    RoutingDTO routingDTO = RoutingDTO.builder().serviceId(1L).serviceNm("!테스트 코드 등록")
        .servicePath("/register").serviceDesc("Desc").serviceTargetUrl("http://test.co.kr/")
        .serviceLoginType(ServiceLoginType.OAUTH)
        .filterId(1L).serviceAuthType(ServiceAuthType.PUBLIC).regUserNo("1").build();

    gwService = modelMapper.map(routingDTO, GwService.class);
    gwService = gwRoutingRepository.save(gwService);
  }

  @Test
  void findByGwConditions() {

    final List<RoutingDTO> routingGwList = gwRoutingRepository.findByGwConditions();

    assertTrue(routingGwList.size() > 0);
  }

  @Test
  void findByConditions() {

    SearchDTO searchDTO = new SearchDTO();
    searchDTO.setName("!테스트 코드 등록");

    final List<RoutingDTO> routingList = gwRoutingRepository.findByConditions(searchDTO);

    assertTrue(routingList.size() > 0);
    assertEquals(routingList.get(0).getServiceNm(), "!테스트 코드 등록");
    assertEquals(routingList.get(0).getServicePath(), "/register");
    assertEquals(routingList.get(0).getServiceTargetUrl(), "http://test.co.kr/");
  }

  @Test
  void isRegisteredService() {

    RoutingDTO routingDTO = RoutingDTO.builder().serviceNm("!테스트 코드 등록").build();

    assertTrue(gwRoutingRepository.isRegisteredService(routingDTO));
  }

  @Test
  void isValidUpdateData() {

    RoutingDTO routingDTO = RoutingDTO.builder().serviceNm("!테스트 코드 등록").build();

    assertFalse(gwRoutingRepository.isValidUpdateData(routingDTO, 1L));
  }

  @Test
  void deleteService() {

    assertEquals(1, gwRoutingRepository.deleteService(gwService.getServiceId()));
  }
}
package com.kbds.serviceapi.portal.app.repository.querydsl.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.portal.api.entity.GwService;
import com.kbds.serviceapi.portal.app.dto.AppDTO;
import com.kbds.serviceapi.portal.app.entity.GwApp;
import com.kbds.serviceapi.portal.app.repository.GwAppRepository;
import com.kbds.serviceapi.setting.RepositoryTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
class GwAppCustomRepositoryImplTest extends RepositoryTest {

  @Autowired
  GwAppRepository gwAppRepository;

  @Autowired
  private ModelMapper modelMapper;

  private GwApp gwApp;

  @BeforeEach
  public void setUp() {

    List<Long> serviceIdList = new ArrayList<>();
    serviceIdList.add(45L);

    AppDTO appDTO = AppDTO.builder().appNm("!테스트 앱 등록")
        .appDesc("앱 설명").appKey("12absidfb==").serviceId(serviceIdList).regUserNo("1").build();

    gwApp = modelMapper.map(appDTO, GwApp.class);
    gwApp = gwAppRepository.save(gwApp);
  }

  @Test
  void 조건별_검색_테스트() {

    SearchDTO searchDTO = new SearchDTO();
    searchDTO.setName("등록");

    List<AppDTO> appList = gwAppRepository.findByConditions(searchDTO);

    assertTrue(appList.size() > 0);
    assertEquals(appList.get(0).getAppNm(), "!테스트 앱 등록");
    assertEquals(appList.get(0).getAppDesc(), "앱 설명");
  }

  @Test
  void APP_ID_검색_테스트() {

    Map<GwApp, List<GwService>> appDetails = gwAppRepository.findAppDetailById(gwApp.getAppId());

    assertTrue(appDetails.containsKey(gwApp));
  }
}
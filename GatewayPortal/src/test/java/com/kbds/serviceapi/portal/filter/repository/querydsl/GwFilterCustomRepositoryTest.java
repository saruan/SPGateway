package com.kbds.serviceapi.portal.filter.repository.querydsl;

import static org.junit.jupiter.api.Assertions.*;

import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.portal.app.dto.AppDTO;
import com.kbds.serviceapi.portal.app.entity.GwApp;
import com.kbds.serviceapi.portal.app.repository.GwAppRepository;
import com.kbds.serviceapi.portal.app.repository.GwServiceAppMappingRepository;
import com.kbds.serviceapi.portal.filter.dto.FilterDTO;
import com.kbds.serviceapi.portal.filter.entity.GwServiceFilter;
import com.kbds.serviceapi.portal.filter.repository.GwFilterRepository;
import com.kbds.serviceapi.setting.RepositoryTest;
import java.util.ArrayList;
import java.util.List;
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
class GwFilterCustomRepositoryTest extends RepositoryTest {

  @Autowired
  GwFilterRepository gwFilterRepository;

  @Autowired
  private ModelMapper modelMapper;

  private GwServiceFilter gwServiceFilter;

  @BeforeEach
  public void setUp() {

    List<Long> serviceIdList = new ArrayList<>();
    serviceIdList.add(45L);

    FilterDTO filterDTO = FilterDTO.builder().filterNm("!테스트 필터 등록")
        .filterDesc("필터 설명").filterBean("TestFilter").regUserNo("1").build();

    gwServiceFilter = modelMapper.map(filterDTO, GwServiceFilter.class);
    gwServiceFilter = gwFilterRepository.save(gwServiceFilter);
  }

  @Test
  void findByConditions() {

    SearchDTO searchDTO = new SearchDTO();
    searchDTO.setName("등록");

    List<FilterDTO> filterList = gwFilterRepository.findByConditions(searchDTO);

    assertTrue(filterList.size() > 0);
    assertEquals(filterList.get(0).getFilterNm(), "!테스트 필터 등록");
    assertEquals(filterList.get(0).getFilterBean(), "TestFilter");
  }

  @Test
  void isValidData() {

    FilterDTO checkDTO = FilterDTO.builder().filterBean("notRegisterBean").filterNm("newFilter")
        .build();

    assertTrue(gwFilterRepository.isValidData(checkDTO));

  }

  @Test
  void checkUpdateValidation() {

    FilterDTO checkDTO = FilterDTO.builder().filterBean("notRegisterBean").filterNm("newFilter")
        .filterId(gwServiceFilter.getFilterId()).build();

    assertTrue(gwFilterRepository.checkUpdateValidation(checkDTO));
  }
}
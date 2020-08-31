package com.kbds.serviceapi.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.kbds.serviceapi.apis.dto.FilterDTO;
import com.kbds.serviceapi.apis.entity.GwServiceFilter;
import com.kbds.serviceapi.apis.querydsl.GwFilterCustomRepository;
import com.kbds.serviceapi.apis.repository.GwFilterRepository;
import com.kbds.serviceapi.apis.service.GwFilterService;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.exception.BizException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
 *  File  Name     : FilterTest
 *  Description    : FilterService 단위 테스트
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-06-30          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@ExtendWith(MockitoExtension.class)
public class FilterServiceTest {

  @Spy
  @InjectMocks
  GwFilterService gwFilterService;

  @Mock
  GwFilterRepository gwFilterRepository;

  @Mock
  GwFilterCustomRepository gwFilterCustomRepository;

  @InjectMocks
  ModelMapper modelMapper;

  Long filterId;
  GwServiceFilter filterDetailEntity;
  List<FilterDTO> filterList;
  SearchDTO searchConditions;
  FilterDTO filterDetailDTO;
  FilterDTO filterUpdateDetailDTO;

  @BeforeEach
  void initData() {

    filterId = 1L;
    filterDetailEntity = new GwServiceFilter(filterId, "기본 공통 필터",
        "인증 처리용 필터", "CommonFilter", "Y");

    filterDetailEntity.setUptUserNo("1");
    filterDetailEntity.setRegUserNo("1");
    filterDetailEntity.setRegDt(new Date());
    filterDetailEntity.setUptDt(new Date());

    filterDetailDTO = new FilterDTO(filterId, "기본 공통 필터",
        "인증 처리용 필터", "CommonFilter", "Y",
        "1", "1", new Date(), new Date());

    filterUpdateDetailDTO = new FilterDTO(filterId, "수정 필터",
        "수정 필터", "UpdateFilter",
        "Y", "1", "1", new Date(), new Date());

    filterList = new ArrayList<>();
    filterList.add(filterDetailDTO);

    searchConditions = new SearchDTO();

    ReflectionTestUtils.setField(gwFilterService, "modelMapper", new ModelMapper());
  }

  @Test
  void 필터_단건_조회_결과있음_테스트() {

    when(gwFilterRepository.findById(filterId)).thenReturn(Optional.of(filterDetailEntity));

    FilterDTO filterDetail = (FilterDTO) gwFilterService.findFilterDetail(filterId);

    assertEquals(filterDetail.getFilterNm(), filterDetailEntity.getFilterNm());
    assertEquals(filterDetail.getFilterId(), filterDetailEntity.getFilterId());
    assertEquals(filterDetail.getFilterBean(), filterDetailEntity.getFilterBean());
    assertEquals(filterDetail.getFilterDesc(), filterDetailEntity.getFilterDesc());
  }

  @Test
  void 필터_리스트_조회_테스트() {

    when(gwFilterCustomRepository.findByConditions(searchConditions)).thenReturn(filterList);

    List<FilterDTO> serviceFilterList = gwFilterService.findFilters(searchConditions);

    assertTrue(serviceFilterList.size() > 0);
    assertEquals(serviceFilterList.get(0).getFilterId(), filterDetailDTO.getFilterId());
    assertEquals(serviceFilterList.get(0).getFilterBean(), filterDetailDTO.getFilterBean());
    assertEquals(serviceFilterList.get(0).getFilterDesc(), filterDetailDTO.getFilterDesc());
  }

  @Test
  void 필터_등록_테스트() {

    when(gwFilterCustomRepository.isValidData(filterDetailDTO)).thenReturn(false);
    when(gwFilterRepository.save(filterDetailEntity)).thenReturn(filterDetailEntity);

    gwFilterService.registerFilter(filterDetailDTO);
  }

  @Test
  void 필터_등록_중복_테스트() {

    when(gwFilterCustomRepository.isValidData(filterDetailDTO)).thenReturn(true);

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.registerFilter(filterDetailDTO));

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());

  }

  @Test
  void 필터_수정_테스트() {

    when(gwFilterCustomRepository.checkUpdateValidation(filterUpdateDetailDTO)).thenReturn(true);
    when(gwFilterRepository.findByFilterId(filterId)).thenReturn(filterDetailEntity);
    when(gwFilterRepository.save(filterDetailEntity)).thenReturn(filterDetailEntity);

    gwFilterService.updateFilter(filterUpdateDetailDTO, filterId);
  }

  @Test
  void 필터_수정_조회값_없음_테스트() {

    when(gwFilterRepository.findByFilterId(filterId)).thenReturn(null);

    BizException ex = assertThrows(BizException.class, ()
        -> gwFilterService.updateFilter(filterUpdateDetailDTO, filterId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM004.getCode());
  }

  @Test
  void 필터_수정_유효값_아님_테스트() {

    when(gwFilterCustomRepository.checkUpdateValidation(filterUpdateDetailDTO)).thenReturn(false);
    when(gwFilterRepository.findByFilterId(filterId)).thenReturn(filterDetailEntity);

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.updateFilter(filterUpdateDetailDTO, filterId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }
  
  @Test
  void 엔티티_모델_변환_테스트() {

    GwServiceFilter filterDetail = new GwServiceFilter(1L, "기본 공통 필터",
        "인증 처리용 필터", "CommonFilter", "Y");

    FilterDTO filterDTO = modelMapper.map(filterDetail, FilterDTO.class);

    assertEquals(filterDTO.getFilterNm(), filterDetail.getFilterNm());
    assertEquals(filterDTO.getFilterId(), filterDetail.getFilterId());
    assertEquals(filterDTO.getFilterBean(), filterDetail.getFilterBean());
    assertEquals(filterDTO.getFilterDesc(), filterDetail.getFilterDesc());
  }
}

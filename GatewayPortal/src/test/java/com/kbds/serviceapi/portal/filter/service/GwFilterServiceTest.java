package com.kbds.serviceapi.portal.filter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.portal.api.repository.GwRoutingRepository;
import com.kbds.serviceapi.portal.filter.dto.FilterDTO;
import com.kbds.serviceapi.portal.filter.entity.GwServiceFilter;
import com.kbds.serviceapi.portal.filter.repository.GwFilterRepository;
import com.kbds.serviceapi.portal.filter.service.GwFilterService;
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
public class GwFilterServiceTest {

  @Spy
  @InjectMocks
  GwFilterService gwFilterService;

  @Mock
  GwFilterRepository gwFilterRepository;

  @Mock
  GwRoutingRepository gwRoutingRepository;

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
  void 필터_상세_조회_테스트() {

    when(gwFilterRepository.findById(filterId)).thenReturn(Optional.of(filterDetailEntity));

    FilterDTO filterDetail = (FilterDTO) gwFilterService.findFilterDetail(filterId);

    assertEquals(filterDetail.getFilterNm(), filterDetailEntity.getFilterNm());
    assertEquals(filterDetail.getFilterId(), filterDetailEntity.getFilterId());
    assertEquals(filterDetail.getFilterBean(), filterDetailEntity.getFilterBean());
    assertEquals(filterDetail.getFilterDesc(), filterDetailEntity.getFilterDesc());
  }

  @Test
  void 필터_상세_조회_데이터_없음_테스트() {

    when(gwFilterRepository.findById(filterId)).thenReturn(Optional.empty());

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.findFilterDetail(filterId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM004.getCode());
  }

  @Test
  void 필터_상세_조회_기타_실패_테스트() {

    when(gwFilterRepository.findById(filterId))
        .thenThrow(new ArrayIndexOutOfBoundsException());

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.findFilterDetail(filterId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }

  @Test
  void 필터_리스트_조회_테스트() {

    when(gwFilterRepository.findByConditions(searchConditions)).thenReturn(filterList);

    List<FilterDTO> serviceFilterList = gwFilterService.findFilters(searchConditions);

    assertTrue(serviceFilterList.size() > 0);
    assertEquals(serviceFilterList.get(0).getFilterId(), filterDetailDTO.getFilterId());
    assertEquals(serviceFilterList.get(0).getFilterBean(), filterDetailDTO.getFilterBean());
    assertEquals(serviceFilterList.get(0).getFilterDesc(), filterDetailDTO.getFilterDesc());
  }

  @Test
  void 필터_조회_실패_테스트() {

    when(gwFilterRepository.findByConditions(searchConditions))
        .thenThrow(new ArrayIndexOutOfBoundsException());

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.findFilters(searchConditions));

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }

  @Test
  void 필터_등록_테스트() {

    when(gwFilterRepository.isValidData(filterDetailDTO)).thenReturn(true);
    when(gwFilterRepository.save(filterDetailEntity)).thenReturn(filterDetailEntity);

    gwFilterService.registerFilter(filterDetailDTO);
  }

  @Test
  void 필터_등록_중복_테스트() {

    when(gwFilterRepository.isValidData(filterDetailDTO)).thenReturn(false);

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.registerFilter(filterDetailDTO));

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());

  }

  @Test
  void 필터_등록_기타_실패_테스트() {

    when(gwFilterRepository.isValidData(filterDetailDTO)).thenReturn(true);

    when(gwFilterRepository.save(filterDetailEntity))
        .thenThrow(new ArrayIndexOutOfBoundsException());

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.registerFilter(filterDetailDTO));

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());

  }

  @Test
  void 필터_수정_테스트() {

    when(gwFilterRepository.checkUpdateValidation(filterUpdateDetailDTO)).thenReturn(true);
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

    when(gwFilterRepository.checkUpdateValidation(filterUpdateDetailDTO)).thenReturn(false);
    when(gwFilterRepository.findByFilterId(filterId)).thenReturn(filterDetailEntity);

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.updateFilter(filterUpdateDetailDTO, filterId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM003.getCode());
  }

  @Test
  void 필터_수정_기타_오류_테스트() {

    when(gwFilterRepository.findByFilterId(filterId))
        .thenThrow(new ArrayIndexOutOfBoundsException());

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.updateFilter(filterUpdateDetailDTO, filterId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
  }

  @Test
  void 필터_삭제_테스트() {

    when(gwRoutingRepository.countByFilterFilterId(filterId))
        .thenReturn(0L);

    doNothing().when(gwFilterRepository).deleteById(filterId);

    gwFilterService.deleteFilter(filterId);
  }

  @Test
  void 필터_삭제_API_사용중_테스트() {

    when(gwRoutingRepository.countByFilterFilterId(filterId))
        .thenReturn(1L);

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.deleteFilter(filterId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM007.getCode());
  }

  @Test
  void 필터_삭제_기타_오류_테스트() {

    when(gwRoutingRepository.countByFilterFilterId(filterId))
        .thenThrow(new ArrayIndexOutOfBoundsException());

    BizException ex = assertThrows(BizException.class,
        () -> gwFilterService.deleteFilter(filterId));

    assertEquals(ex.getMessage(), BizExceptionCode.COM001.getCode());
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

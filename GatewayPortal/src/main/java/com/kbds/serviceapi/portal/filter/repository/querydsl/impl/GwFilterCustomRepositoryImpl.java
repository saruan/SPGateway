package com.kbds.serviceapi.portal.filter.repository.querydsl.impl;

import static com.kbds.serviceapi.portal.filter.entity.QGwServiceFilter.gwServiceFilter;

import com.kbds.serviceapi.common.utils.StringUtils;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.portal.api.entity.GwService;
import com.kbds.serviceapi.portal.filter.dto.FilterDTO;
import com.kbds.serviceapi.portal.filter.dto.QFilterDTO;
import com.kbds.serviceapi.portal.filter.repository.querydsl.GwFilterCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  Class Name     : GwServiceCustomRepositoryImpl.java
 *  Description    : 사용자 정의 Querydsl 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Service
public class GwFilterCustomRepositoryImpl extends QuerydslRepositorySupport
    implements GwFilterCustomRepository {

  /**
   * 생성자
   */
  public GwFilterCustomRepositoryImpl() {

    super(GwService.class);
  }

  /**
   * 검색 조건에 맞게 GW_SERVICE 테이블을 조회하는 Custom Repository
   */
  @Override
  public List<FilterDTO> findByConditions(SearchDTO searchDTO) {

    // GW_SERVICE_FILTER에서 검색 조건문으로 등록한 조건에 맞게 검색한 후 FilterDTO로 결과 값을 매핑시킨다.
    return from(gwServiceFilter)
          .select(new QFilterDTO(
              gwServiceFilter.filterId,
              gwServiceFilter.filterNm,
              gwServiceFilter.filterDesc,
              gwServiceFilter.filterBean,
              gwServiceFilter.useYn,
              gwServiceFilter.regUserNo,
              gwServiceFilter.uptUserNo,
              gwServiceFilter.regDt,
              gwServiceFilter.uptDt))
          .where(likeNm(searchDTO.getName()))
          .fetch();
  }

  /**
   * 필터 등록 전 중복 서비스 여부 체크
   */
  @Override
  public boolean isValidData(FilterDTO param) {

    // 이미 등록되어 있는 데이터가 있다면 true 없다면 false를 리턴한다.
    return from(gwServiceFilter)
          .select()
          .where(eqFilterNmOrFilterBean(param.getFilterNm(), param.getFilterBean()))
          .fetchCount() > 0;
  }

  /**
   * 필터 수정 전 유효성 체크
   */
  @Override
  public boolean checkUpdateValidation(FilterDTO param) {

    // 이미 등록되어 있는 데이터가 있다면 false 없다면 true를 리턴한다.
    return from(gwServiceFilter)
          .select()
          .where(eqFilterNmOrFilterBean(param.getFilterNm(), param.getFilterBean()),
              neFilterId(param.getFilterId()))
          .fetchCount() <= 0;
  }

  /**
   * Filter Bean LIKE 검색
   * @param name
   * @return
   */
  private BooleanExpression likeNm(String name){

    return StringUtils.isEmptyParams(name) ? null : gwServiceFilter.filterNm.contains(name);
  }

  /**
   * Filter 중복 체크
   * @param filterNm
   * @return
   */
  private BooleanExpression eqFilterNmOrFilterBean(String filterNm, String filterBean){

    return StringUtils.isEmptyParams(filterNm, filterBean) ?
        null : gwServiceFilter.filterNm.eq(filterNm).or(gwServiceFilter.filterBean.eq(filterBean));
  }

  /**
   * 현재 Filter ID를 제외한 검색
   * @param filterId
   * @return
   */
  private BooleanExpression neFilterId(Long filterId){

    return filterId == null ?
        null : gwServiceFilter.filterId.ne(filterId);
  }
  
}

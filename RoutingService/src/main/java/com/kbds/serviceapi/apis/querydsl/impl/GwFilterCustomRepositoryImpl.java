package com.kbds.serviceapi.apis.querydsl.impl;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import com.kbds.serviceapi.apis.dto.FilterDTO;
import com.kbds.serviceapi.apis.entity.GwService;
import com.kbds.serviceapi.apis.entity.QGwServiceFilter;
import com.kbds.serviceapi.apis.querydsl.GwFilterCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;

/**
 * 
 *
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
 *
 */
@Service
public class GwFilterCustomRepositoryImpl extends QuerydslRepositorySupport
    implements GwFilterCustomRepository {

  /**
   * 생성자
   * 
   * @param domainClass
   */
  public GwFilterCustomRepositoryImpl() {

    super(GwService.class);
  }

  /**
   * 검색 조건에 맞게 GW_SERVICE 테이블을 조회하는 Custom Repository
   */
  @Override
  public List<FilterDTO> findbyConditions(FilterDTO param) {

    QGwServiceFilter gwFilter = QGwServiceFilter.gwServiceFilter;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    if (param.getFilterId() != null) {
      builder.and(gwFilter.filterId.eq(param.getFilterId()));
    }

    if (!StringUtils.isEmpty(param.getFilterBean())) {
      builder.and(gwFilter.filterBean.eq(param.getFilterBean()));
    }

    // GW_SERVICE_FILTER에서 검색 조건문으로 등록한 조건에 맞게 검색한 후 FilterDTO로 결과 값을 매핑시킨다.
    return from(gwFilter).select(Projections.constructor(FilterDTO.class, gwFilter.filterId,
        gwFilter.filterNm, gwFilter.filterDesc, gwFilter.filterBean, gwFilter.useYn,
        gwFilter.regUserNo, gwFilter.uptUserNo, gwFilter.regDt, gwFilter.uptDt)).where(builder)
        .fetch();
  }

  /**
   * 필터 등록 전 중복 서비스 여부 체크
   */
  @Override
  public boolean checkRegistValidation(FilterDTO param) {

    QGwServiceFilter gwServiceFilter = QGwServiceFilter.gwServiceFilter;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    // servicePath, serviceNm은 한 가지라도 등록이 되어 있다면 등록이 되지 않아야 한다.
    if (!StringUtils.isEmpty(param.getFilterBean())) {
      builder.and(gwServiceFilter.filterBean.eq(param.getFilterBean()));
    }

    if (!StringUtils.isEmpty(param.getFilterNm())) {
      builder.and(gwServiceFilter.filterNm.eq(param.getFilterNm()));
    }

    // filterNm, filterBean은 한 가지라도 등록이 되어 있다면 등록이 되지 않아야 한다.
    if (!StringUtils.isEmpty(param.getFilterNm()) && !StringUtils.isEmpty(param.getFilterBean())) {
      builder.and(gwServiceFilter.filterNm.eq(param.getFilterNm())
          .or(gwServiceFilter.filterBean.eq(param.getFilterBean())));
    }

    // 이미 등록되어 있는 데이터가 있다면 true 없다면 false를 리턴한다.
    return from(gwServiceFilter).fetchJoin().select().where(builder).fetchCount() > 0 ? true
        : false;
  }

  /**
   * 필터 수정 전 유효성 체크
   */
  @Override
  public boolean checkUpdateValidation(FilterDTO param) {

    QGwServiceFilter gwServiceFilter = QGwServiceFilter.gwServiceFilter;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    // 해당 filterId 이 외의 자료를 체크하기 위한 조건
    builder.and(gwServiceFilter.filterId.ne(param.getFilterId()));

    // 필터명, 필터빈은 한 가지라도 등록이 되어 있다면 수정이 되지 않아야 한다.
    if (!StringUtils.isEmpty(param.getFilterBean()) && !StringUtils.isEmpty(param.getFilterNm())) {
      builder.and(gwServiceFilter.filterNm.eq(param.getFilterNm())
          .or(gwServiceFilter.filterBean.eq(param.getFilterBean())));
    }

    // 이미 등록되어 있는 데이터가 있다면 true 없다면 false를 리턴한다.
    return from(gwServiceFilter).fetchJoin().select().where(builder).fetchCount() > 0 ? true
        : false;
  }

}

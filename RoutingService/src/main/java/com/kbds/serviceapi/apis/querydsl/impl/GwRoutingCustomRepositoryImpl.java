package com.kbds.serviceapi.apis.querydsl.impl;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import com.kbds.serviceapi.apis.dto.RoutingDTO;
import com.kbds.serviceapi.apis.entity.GwService;
import com.kbds.serviceapi.apis.entity.QGwApp;
import com.kbds.serviceapi.apis.entity.QGwService;
import com.kbds.serviceapi.apis.entity.QGwServiceAppMapping;
import com.kbds.serviceapi.apis.entity.QGwServiceFilter;
import com.kbds.serviceapi.apis.querydsl.GwRoutingCustomRepository;
import com.kbds.serviceapi.common.code.CommonCode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;

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
public class GwRoutingCustomRepositoryImpl extends QuerydslRepositorySupport
    implements GwRoutingCustomRepository {

  /**
   * 생성자
   * 
   * @param domainClass
   */
  public GwRoutingCustomRepositoryImpl() {

    super(GwService.class);
  }

  /**
   * Gw Bean Bean 등록 정보 조회 Custom Repository
   */
  @Override
  public List<RoutingDTO> findbyGwConditions() {

    QGwService gwService = QGwService.gwService;
    QGwServiceAppMapping gwServiceAppMapping = QGwServiceAppMapping.gwServiceAppMapping;
    QGwApp gwApp = QGwApp.gwApp;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    builder.and(gwService.useYn.eq(CommonCode.Y.getResultCode()));
    // builder.and(gwApp.useYn.eq(CommonCode.Y.getResultCode()));

    // @formatter:off
    // GwService, GwApp, Mapping 테이블과 조인 후 appKey를 , 로 합쳐 전달한다.    
    return from(gwService)
          .leftJoin(gwServiceAppMapping)
             .on(gwService.serviceId.eq(gwServiceAppMapping.gwService.serviceId))
          .leftJoin(gwApp)
             .on(gwApp.appId.eq(gwServiceAppMapping.gwApp.appId))
          .select(Projections.constructor(RoutingDTO.class, 
                 gwService.serviceId,
                 gwService.filter.filterId, 
                 gwService.serviceNm,
                 Expressions.stringTemplate("group_concat({0}, ',')", gwApp.appKey),
                 gwService.servicePath, 
                 gwService.serviceTargetUrl, 
                 gwService.serviceDesc,
                 gwService.serviceLoginType, 
                 gwService.serviceAuthType, 
                 gwService.useYn,
                 gwService.filter.filterBean, 
                 gwService.filter.useYn, 
                 gwService.regUserNo,
                 gwService.uptUserNo, 
                 gwService.regDt, 
                 gwService.uptDt))
          .where(builder).groupBy(gwService.serviceId).fetch();
    
    // @formatter:on

  }

  /**
   * 검색 조건에 맞게 GW_SERVICE 테이블을 조회하는 Custom Repository
   */
  @Override
  public List<RoutingDTO> findbyConditions(RoutingDTO param) {

    QGwService gwService = QGwService.gwService;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    // 서비스명 검색
    if (!StringUtils.isEmpty(param.getServiceNm())) {
      builder.and(gwService.serviceNm.eq(param.getServiceNm()));
    }

    // 서비스 경로 검색
    if (!StringUtils.isEmpty(param.getServicePath())) {
      builder.and(gwService.servicePath.eq(param.getServicePath()));
    }

    // 로그인 타입 검색
    if (!StringUtils.isEmpty(param.getServiceLoginType())) {
      builder.and(gwService.serviceLoginType.eq(param.getServiceLoginType()));
    }

    // 인증 방식 검색
    if (!StringUtils.isEmpty(param.getServiceAuthType())) {
      builder.and(gwService.serviceAuthType.eq(param.getServiceAuthType()));
    }

    // 사용 유무 검색
    if (!StringUtils.isEmpty(param.getUseYn())) {
      builder.and(gwService.useYn.eq(param.getUseYn()));
      builder.and(gwService.filter.useYn.eq(param.getUseYn()));
    }

    // GW_SERVICE에서 검색 조건문으로 등록한 조건에 맞게 검색한 후 RoutingServiceDTO로 결과 값을 매핑시킨다.
    return from(gwService).select(
        Projections.constructor(RoutingDTO.class, gwService.serviceId, gwService.filter.filterId,
            gwService.serviceNm, gwService.servicePath, gwService.serviceTargetUrl,
            gwService.serviceDesc, gwService.serviceLoginType, gwService.serviceAuthType,
            gwService.useYn, gwService.filter.filterBean, gwService.filter.useYn,
            gwService.regUserNo, gwService.uptUserNo, gwService.regDt, gwService.uptDt))
        .where(builder).fetch();
  }

  /**
   * 서비스 등록 전 중복 서비스 여부 체크
   */
  @Override
  public boolean checkRegistValidation(RoutingDTO param) {

    QGwService gwService = QGwService.gwService;
    QGwServiceFilter gwServiceFilter = QGwServiceFilter.gwServiceFilter;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    // filterId가 유효한 값인지 체크한다.
    builder.and(gwServiceFilter.filterId.eq(param.getFilterId()));

    // servicePath, serviceNm은 한 가지라도 등록이 되어 있다면 등록이 되지 않아야 한다.
    if (!StringUtils.isEmpty(param.getServicePath())
        && !StringUtils.isEmpty(param.getServiceNm())) {
      builder.and(gwService.servicePath.eq(param.getServicePath())
          .or(gwService.serviceNm.eq(param.getServiceNm())));
    }

    // 이미 등록되어 있는 데이터가 있다면 true 없다면 false를 리턴한다.
    return from(gwService).innerJoin(gwServiceFilter).on(builder).fetchJoin().select()
        .fetchCount() > 0 ? true : false;
  }

  /**
   * 서비스 수정 전 중복 서비스 체크
   */
  @Override
  public boolean checkUpdateValidation(RoutingDTO param, Long serviceId) {

    QGwService gwService = QGwService.gwService;
    QGwServiceFilter gwServiceFilter = QGwServiceFilter.gwServiceFilter;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    // filterId가 유효한 값인지 체크한다.
    builder.and(gwServiceFilter.filterId.eq(param.getFilterId()))
        .and(gwServiceFilter.useYn.eq(CommonCode.Y.getResultCode()));

    // 해당 serviceId 이 외의 자료를 체크하기 위한 조건
    builder.and(gwService.serviceId.ne(serviceId));

    // servicePath, serviceNm은 한 가지라도 등록이 되어 있다면 등록이 되지 않아야 한다.
    if (!StringUtils.isEmpty(param.getServicePath())
        && !StringUtils.isEmpty(param.getServiceNm())) {
      builder.and(gwService.servicePath.eq(param.getServicePath())
          .or(gwService.serviceNm.eq(param.getServiceNm())));
    }

    // 이미 등록되어 있는 데이터가 있다면 true 없다면 false를 리턴한다.
    return from(gwService).innerJoin(gwServiceFilter).on(builder).fetchJoin().select()
        .fetchCount() > 0 ? true : false;
  }


  /**
   * 서비스 삭제
   */
  @Override
  public long deleteService(Long[] serviceId) {
    QGwService gwService = QGwService.gwService;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    // 논리적으로 삭제 처리할 데이터들을 IN 조건에 등록한다.
    builder.and(gwService.serviceId.in(serviceId));

    // USE_YN 값을 N으로 변경하는 쿼리를 수행한다.
    return update(gwService).where(builder).set(gwService.useYn, CommonCode.N.getResultCode())
        .execute();
  }

  /**
   * 필터 삭제 이후 연관 서비스 수정
   */
  @Override
  public long updateServiceByFilter(Long filterId) {

    QGwService gwService = QGwService.gwService;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    // 논리적으로 삭제 처리할 데이터들을 IN 조건에 등록한다.
    builder.and(gwService.filter.filterId.eq(filterId));

    // USE_YN 값을 N으로 변경하는 쿼리를 수행한다.
    return update(gwService).where(builder).set(gwService.useYn, CommonCode.N.getResultCode())
        .execute();
  }

}

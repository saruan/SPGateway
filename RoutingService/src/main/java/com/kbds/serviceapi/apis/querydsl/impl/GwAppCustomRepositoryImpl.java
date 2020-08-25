package com.kbds.serviceapi.apis.querydsl.impl;

import com.kbds.serviceapi.apis.dto.AppDTO;
import com.kbds.serviceapi.apis.entity.GwApp;
import com.kbds.serviceapi.apis.entity.QGwApp;
import com.kbds.serviceapi.apis.entity.QGwService;
import com.kbds.serviceapi.apis.entity.QGwServiceAppMapping;
import com.kbds.serviceapi.apis.querydsl.GwAppCustomRepository;
import com.querydsl.core.BooleanBuilder;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;

@Service
public class GwAppCustomRepositoryImpl extends QuerydslRepositorySupport
    implements GwAppCustomRepository {

  @Autowired
  ModelMapper modelMapper;

  /**
   * 생성자
   */
  public GwAppCustomRepositoryImpl() {

    super(GwApp.class);
  }

  @Override
  public List<AppDTO> findbyConditions(AppDTO param) {
    QGwApp qGwApp = QGwApp.gwApp;
    QGwServiceAppMapping qGwServiceAppMapping = QGwServiceAppMapping.gwServiceAppMapping;
    QGwService qGwService = QGwService.gwService;

    // 검색 조건문 등록
    BooleanBuilder builder = new BooleanBuilder();

    // APP명 검색
    if (!StringUtils.isEmpty(param.getAppNm())) {

      builder.and(qGwApp.appNm.eq(param.getAppNm()));
    }

    // 사용 유무 검색
    if (!StringUtils.isEmpty(param.getUseYn())) {

      builder.and(qGwApp.useYn.eq(param.getUseYn()));
    }

    // @formatter:off

    // 조회 쿼리 실행
    List<GwApp> gwApps = from(qGwApp)
        .innerJoin(qGwServiceAppMapping)
          .on(qGwApp.appId.eq(qGwServiceAppMapping.gwApp.appId))
        .innerJoin(qGwService)
          .on(qGwServiceAppMapping.gwService.serviceId.eq(qGwService.serviceId))
        .where(builder).fetch();

    return gwApps.stream().map(ele -> {

      // 결과 데이터 중 서비스 ID만 결과 값에 담아서 준다.
      List<Long> serviceIds = ele.getGwService().stream()
          .map(data -> data.getGwService().getServiceId())
          .collect(Collectors.toList());

      return new AppDTO(
          ele.getAppId(),
          ele.getAppNm(),
          ele.getAppKey(),
          ele.getAppDesc(),
          ele.getUseYn(),
          serviceIds,
          ele.getRegUserNo(),
          ele.getUptUserNo(),
          ele.getRegDt(),
          ele.getUptDt());

    }).collect(Collectors.toList());
    // @formatter:on

    /*
      App 검색 시 GwService 정보 노출 범위때문에 우선 보류 (현재는 서비스 ID List만 보여줌)
    List<GwApp> gwApps = from(gwApp).innerJoin(qGwServiceAppMapping).innerJoin(qGwService)
        .where(builder).fetch();

    return gwApps.stream().map(ele -> {

      List<RoutingDTO> gwService = ele.getGwService().stream()
          .map(mapping -> modelMapper.map(mapping.getGwService(), RoutingDTO.class))
          .collect(Collectors.toList());

      // @formatter:off
    return new AppDTO(
        ele.getAppId(),
        ele.getAppNm(),
        ele.getAppKey(),
        ele.getAppDesc(),
        ele.getUseYn(),
        gwService,
        ele.getRegUserNo(),
        ele.getUptUserNo(),
        ele.getRegDt(),
        ele.getUptDt());
      // @formatter:on
    }).collect(Collectors.toList());
    */
  }
}

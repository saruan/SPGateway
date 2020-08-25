package com.kbds.serviceapi.apis.querydsl.impl;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.kbds.serviceapi.apis.dto.AppDTO;
import com.kbds.serviceapi.apis.entity.GwApp;
import com.kbds.serviceapi.apis.entity.QGwApp;
import com.kbds.serviceapi.apis.entity.QGwService;
import com.kbds.serviceapi.apis.entity.QGwServiceAppMapping;
import com.kbds.serviceapi.apis.querydsl.GwAppCustomRepository;
import com.querydsl.core.BooleanBuilder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    //   조회 쿼리 실행
    //   - APP의 API는 리스트 형태이므로 GroupBy를 한 후 자식 엔티티를 말아준다.
    Map<GwApp, List<Long>> gwApps = from(qGwApp)
        .innerJoin(qGwServiceAppMapping).fetchJoin()
        .on(qGwApp.appId.eq(qGwServiceAppMapping.gwApp.appId))
        .innerJoin(qGwService).fetchJoin()
        .on(qGwServiceAppMapping.gwService.serviceId.eq(qGwService.serviceId))
        .where(builder).transform(
            groupBy(qGwApp).as(list(qGwService.serviceId)));

    Iterator<GwApp> keys = gwApps.keySet().iterator();

    // AppDTO로 변환 후 리턴
    return gwApps.entrySet().stream().map(entry -> {

      GwApp gwApp = entry.getKey();

      return new AppDTO(gwApp.getAppId(), gwApp.getAppNm(), gwApp.getAppKey(), gwApp.getAppDesc()
          , gwApp.getUseYn(), entry.getValue(), gwApp.getRegUserNo(), gwApp.getUptUserNo(),
          gwApp.getRegDt(), gwApp.getUptDt());
    }).collect(Collectors.toList());
  }
}

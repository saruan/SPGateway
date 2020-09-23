package com.kbds.portal.apis.repository.querydsl.impl;

import static com.kbds.portal.apis.entity.QGwApp.gwApp;
import static com.kbds.portal.apis.entity.QGwService.gwService;
import static com.kbds.portal.apis.entity.QGwServiceAppMapping.gwServiceAppMapping;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.kbds.portal.apis.dto.AppDTO;
import com.kbds.portal.apis.entity.GwApp;
import com.kbds.portal.apis.repository.querydsl.GwAppCustomRepository;
import com.kbds.portal.common.utils.StringUtils;
import com.kbds.portal.framework.dto.SearchDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
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
  public List<AppDTO> findByConditions(SearchDTO searchDTO) {

    // 조회 쿼리 실행
    Map<GwApp, List<Long>> gwApps = from(gwApp)
        .innerJoin(gwServiceAppMapping).fetchJoin()
          .on(gwApp.appId.eq(gwServiceAppMapping.gwApp.appId))
        .innerJoin(gwService).fetchJoin()
          .on(gwServiceAppMapping.gwService.serviceId.eq(gwService.serviceId))
        .where(likeNm(searchDTO.getName()))
          .transform(groupBy(gwApp).as(list(gwService.serviceId)));

    // AppDTO로 변환 후 리턴
    return gwApps.entrySet().stream().map(entry -> {

      GwApp gwApp = entry.getKey();

      return new AppDTO(gwApp.getAppId(), gwApp.getAppNm(), gwApp.getAppKey(), gwApp.getAppDesc()
          , gwApp.getUseYn(), entry.getValue(), gwApp.getRegUserNo(), gwApp.getUptUserNo(),
          gwApp.getRegDt(), gwApp.getUptDt());
    }).collect(Collectors.toList());
  }

  /**
   * 이름 LIKE 검색
   * @param name
   * @return
   */
  private BooleanExpression likeNm(String name){

    return StringUtils.isEmptyParams(name) ? null : gwApp.appNm.contains(name);
  }
}

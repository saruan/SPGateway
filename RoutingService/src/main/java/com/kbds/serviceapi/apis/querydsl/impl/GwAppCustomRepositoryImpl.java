package com.kbds.serviceapi.apis.querydsl.impl;

import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import com.kbds.serviceapi.apis.dto.AppDTO;
import com.kbds.serviceapi.apis.entity.GwApp;
import com.kbds.serviceapi.apis.querydsl.GwAppCustomRepository;

@Service
public class GwAppCustomRepositoryImpl extends QuerydslRepositorySupport
    implements GwAppCustomRepository {

  /**
   * 생성자
   * 
   * @param domainClass
   */
  public GwAppCustomRepositoryImpl() {

    super(GwApp.class);
  }

  @Override
  public List<AppDTO> updateServiceAppMapping(AppDTO param) {

    return null;
  }
}

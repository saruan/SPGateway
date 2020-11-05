package com.kbds.serviceapi.portal.app.service;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.portal.app.dto.AppDTO;
import com.kbds.serviceapi.portal.app.entity.GwServiceAppMapping;
import com.kbds.serviceapi.portal.app.entity.key.GwServiceAppMappingKey;
import com.kbds.serviceapi.portal.app.repository.GwServiceAppMappingRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  File  Name     : GwServiceAppMappingService
 *  Description    : 서비스-APP 매핑 관리 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-08-31         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
public class GwServiceAppMappingService {

  @Autowired
  GwServiceAppMappingRepository gwServiceAppMappingRepository;

  /**
   * Service/App Mapping 테이블 데이터 등록
   * @param appDTO
   * @param id
   */
  public void registerServiceAppMapping(AppDTO appDTO, Long id){

    try {
      // Mapping 테이블에 데이터 등록
      List<Long> serviceIdList = appDTO.getServiceId();

      gwServiceAppMappingRepository.deleteByIdAppIdAndIdServiceIdNotIn(id, appDTO.getServiceId());

      if (serviceIdList.size() > 0) {

        for (Long aLong : serviceIdList) {

          GwServiceAppMappingKey key = new GwServiceAppMappingKey(aLong, id);
          GwServiceAppMapping param = new GwServiceAppMapping(key);

          gwServiceAppMappingRepository.save(param);
        }
      }
    }catch(Exception e){

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }
}

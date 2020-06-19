package com.kbds.serviceapi.apis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kbds.serviceapi.apis.dto.AppDTO;
import com.kbds.serviceapi.apis.dto.EmptyJsonBody;
import com.kbds.serviceapi.apis.entity.GwApp;
import com.kbds.serviceapi.apis.entity.GwServiceAppMapping;
import com.kbds.serviceapi.apis.entity.key.GwServiceAppMappingKey;
import com.kbds.serviceapi.apis.querydsl.GwAppCustomRepository;
import com.kbds.serviceapi.apis.repository.GwAppRepository;
import com.kbds.serviceapi.apis.repository.GwRoutingRepository;
import com.kbds.serviceapi.apis.repository.GwServiceAppMappingRepository;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.common.utils.StringUtils;
import com.kbds.serviceapi.framework.exception.BizException;

/**
 *
 * <pre>
 *  Class Name     : GwAppService.java
 *  Description    : App 관리 서비스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-25    	   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Service
public class GwAppService {

  Logger logger = LoggerFactory.getLogger(GwAppService.class);

  @Autowired
  GwAppRepository gwAppRepository;

  @Autowired
  GwServiceAppMappingRepository gwServiceAppMappingRepository;

  @Autowired
  GwAppCustomRepository gwAppCustomRepository;

  @Autowired
  GwRoutingRepository gwRoutingRepository;

  @Autowired
  ModelMapper modelMapper;


  /**
   * App 검색 기능
   * 
   * @param params
   * @return
   */
  public List<AppDTO> findApps(AppDTO params) {

    try {

      return null;// gwAppCustomRepository.findbyConditions(params);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  };

  /**
   * failed to lazily initialize a collection of role App 상세 검색 기능
   * 
   * @param params
   * @return
   */
  @Transactional
  public Object findAppDetail(Long id) {

    if (id == null) {

      throw new BizException(BizExceptionCode.COM002);
    }

    try {

      Optional<GwApp> gwApp = gwAppRepository.findById(id);

      if (!gwApp.isPresent()) {

        return new EmptyJsonBody();
      }

      return modelMapper.map(gwApp.get(), AppDTO.class);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  };

  /**
   * App 등록
   * 
   * @param reqParam
   */
  @Transactional
  public void registApp(AppDTO reqParam) {


    // 필수 파라미터 체크(App명)
    if (StringUtils.isEmptyParams(reqParam.getAppNm())) {

      throw new BizException(BizExceptionCode.COM002);
    }

    try {

      if (!isValidServiceId(reqParam)) {

        throw new BizException(BizExceptionCode.COM005);
      }

      if (!isValidAppNm(reqParam)) {

        throw new BizException(BizExceptionCode.COM003);
      }

      // AppKey 생성 후 DB에 APP 등록
      GwApp gwApp = modelMapper.map(reqParam, GwApp.class);
      gwApp.setAppKey(StringUtils.generateAppKey());
      Long appId = gwAppRepository.save(gwApp).getAppId();

      // Mapping 테이블에 데이터 등록
      List<Long> serviceIdList = reqParam.getServiceId();
      List<GwServiceAppMapping> params = new ArrayList<GwServiceAppMapping>();

      if (serviceIdList.size() > 0) {

        for (int i = 0; i < serviceIdList.size(); i++) {

          GwServiceAppMappingKey key = new GwServiceAppMappingKey(serviceIdList.get(i), appId);
          GwServiceAppMapping param = new GwServiceAppMapping(key);

          params.add(param);
        }

        gwServiceAppMappingRepository.saveAll(params);
      }

      // 등록 이후 게이트웨이에 해당 정보를 갱신해준다.
      CommonUtils.refreshGatewayRoutes(reqParam.getRegUserNo());

    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * App 수정
   * 
   * @param reqParam
   */
  @Transactional
  public void updateApp(AppDTO reqParam, Long id) {

    logger.info("========================");
    logger.info("Update App Info");
    logger.info("params [" + reqParam.toString() + "]");
    logger.info("========================");

    // 필수 파라미터 체크(App명)
    if (StringUtils.isEmptyParams(reqParam.getAppNm())) {

      throw new BizException(BizExceptionCode.COM002);
    }

    // 필수 파라미터 체크(AppKey)
    if (StringUtils.isEmptyParams(reqParam.getAppKey())) {

      throw new BizException(BizExceptionCode.COM002);
    }

    try {

      if (!isValidServiceId(reqParam)) {

        throw new BizException(BizExceptionCode.COM005);
      }

      if (!isValidModifyAppNm(reqParam, id)) {

        throw new BizException(BizExceptionCode.COM003);
      }

      // GwApp 테이블에 정보 수정
      GwApp gwApp = gwAppRepository.findByAppId(id);

      gwApp.setAppKey(reqParam.getAppKey());
      gwApp.setAppDesc(reqParam.getAppDesc());
      gwApp.setAppNm(reqParam.getAppNm());
      gwApp.setUptUserNo(reqParam.getUptUserNo());
      gwApp.setUseYn(reqParam.getUseYn());

      gwAppRepository.save(gwApp);

      // Mapping 테이블에 데이터 등록
      List<Long> serviceIdList = reqParam.getServiceId();
      List<GwServiceAppMapping> params = new ArrayList<GwServiceAppMapping>();

      gwServiceAppMappingRepository.deleteByIdAppIdAndIdServiceIdNotIn(id, reqParam.getServiceId());

      if (serviceIdList.size() > 0) {

        for (int i = 0; i < serviceIdList.size(); i++) {

          GwServiceAppMappingKey key = new GwServiceAppMappingKey(serviceIdList.get(i), id);
          GwServiceAppMapping param = new GwServiceAppMapping(key);

          params.add(param);
        }

        gwServiceAppMappingRepository.saveAll(params);
      }

      // 등록 이후 게이트웨이에 해당 정보를 갱신해준다.
      CommonUtils.refreshGatewayRoutes(reqParam.getRegUserNo());

    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * App 삭제 (등록 되어 있는 API가 있을 경우 App 삭제 금지)
   * 
   * @param id
   */
  public void deleteApp(Long appId) {

    if (appId == null) {

      throw new BizException(BizExceptionCode.COM002);
    }

    try {

      if (isUseApp(appId)) {

        throw new BizException(BizExceptionCode.COM006);
      }

      gwAppRepository.deleteById(appId);

    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * ServiceId 유효성 체크
   * 
   * @param reqParam
   * @return
   */
  public boolean isValidServiceId(AppDTO reqParam) {

    return gwRoutingRepository.countByserviceIdIn(reqParam.getServiceId()) == reqParam
        .getServiceId().size();
  }

  /**
   * App 등록 시 이름 유효성 체크
   * 
   * @param reqParam
   * @return
   */
  public boolean isValidAppNm(AppDTO reqParam) {

    return gwAppRepository.findByAppNm(reqParam.getAppNm()).size() == 0;
  }

  /**
   * App 수정 시 이름 유효성 체크
   * 
   * @param reqParam
   * @return
   */
  public boolean isValidModifyAppNm(AppDTO reqParam, Long appId) {

    return gwAppRepository.findByAppNmAndAppIdNot(reqParam.getAppNm(), appId).size() == 0;
  }

  /**
   * App에 등록 되어 있는 Api가 존재하는지 체크
   * 
   * @param appId
   * @return
   */
  public boolean isUseApp(Long appId) {

    return gwServiceAppMappingRepository.countByIdAppId(appId) > 0;
  }
}

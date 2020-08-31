package com.kbds.serviceapi.apis.service;

import com.kbds.serviceapi.apis.dto.AppDTO;
import com.kbds.serviceapi.apis.dto.EmptyDataDTO;
import com.kbds.serviceapi.apis.entity.GwApp;
import com.kbds.serviceapi.apis.querydsl.GwAppCustomRepository;
import com.kbds.serviceapi.apis.repository.GwAppRepository;
import com.kbds.serviceapi.apis.repository.GwRoutingRepository;
import com.kbds.serviceapi.apis.repository.GwServiceAppMappingRepository;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.common.utils.StringUtils;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.exception.BizException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
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
 */
@Service
public class GwAppService {

  @Autowired
  GwAppRepository gwAppRepository;

  @Autowired
  GwServiceAppMappingRepository gwServiceAppMappingRepository;

  @Autowired
  GwAppCustomRepository gwAppCustomRepository;

  @Autowired
  GwRoutingRepository gwRoutingRepository;

  @Autowired
  GwServiceAppMappingService gwServiceAppMappingService;

  @Autowired
  ModelMapper modelMapper;


  /**
   * APP 리스트 검색 기능
   *
   * @param searchDTO
   * @return
   */
  @Transactional
  public List<AppDTO> findApps(SearchDTO searchDTO) {

    try {

      return gwAppCustomRepository.findByConditions(searchDTO);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * APP 상세 검색 기능
   *
   * @param id
   * @return
   */
  @Transactional
  public Object findAppDetail(Long id) {

    try {

      Optional<GwApp> gwApp = gwAppRepository.findById(id);

      if (!gwApp.isPresent()) {

        return new EmptyDataDTO();
      }

      return modelMapper.map(gwApp.get(), AppDTO.class);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * APP 등록
   *
   * <pre>
   * 1. 파라미터 유효성 체크
   * 2. 이미 등록된 데이터인지 체크
   * 3. GwApp에 데이터 등록
   * 4. GwServiceAppMapping에 데이터 등록
   * </pre>
   *
   * @param reqParam
   */
  @Transactional
  public void registerApp(AppDTO reqParam) {

    try {

      if (!isValidServiceId(reqParam)) {

        throw new BizException(BizExceptionCode.COM005);
      }

      if (!isValidAppNm(reqParam)) {

        throw new BizException(BizExceptionCode.COM003);
      }

      GwApp gwApp = setGwAppWithAppKey(reqParam);

      // APP 등록 후 등록된 APP_ID를 전달 받는다.
      Long appId = gwAppRepository.save(gwApp).getAppId();

      // Mapping 데이터 등록
      gwServiceAppMappingService.registerServiceAppMapping(reqParam, appId);

      // 등록 이후 게이트웨이에 해당 정보를 갱신해준다.
      CommonUtils.refreshGatewayRoutes(reqParam.getRegUserNo());
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * APP 수정
   *
   * <pre>
   * 1. 파라미터 유효성 체크
   * 2. GwApp 테이블 수정
   * 3. GwServiceAppMapping 테이블 수정
   * </pre>
   *
   * @param reqParam
   */
  @Transactional
  public void updateApp(AppDTO reqParam, Long appId) {

    try {

      // 필수 파라미터 체크(AppKey)
      if (isEmptyAppKey(reqParam.getAppKey())) {

        throw new BizException(BizExceptionCode.COM002);
      }

      if (!isValidServiceId(reqParam)) {

        throw new BizException(BizExceptionCode.COM005);
      }

      if (!isValidModifyAppNm(reqParam, appId)) {

        throw new BizException(BizExceptionCode.COM003);
      }

      // GwApp 테이블에 정보 수정
      GwApp gwApp = gwAppRepository.findByAppId(appId);

      gwApp.setAppKey(reqParam.getAppKey());
      gwApp.setAppDesc(reqParam.getAppDesc());
      gwApp.setAppNm(reqParam.getAppNm());
      gwApp.setUptUserNo(reqParam.getUptUserNo());
      gwApp.setUseYn(reqParam.getUseYn());

      gwAppRepository.save(gwApp);

      // Mapping 데이터 등록
      gwServiceAppMappingService.registerServiceAppMapping(reqParam, appId);

      CommonUtils.refreshGatewayRoutes(reqParam.getRegUserNo());
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * APP 삭제 (등록 되어 있는 API가 있을 경우 APP 삭제 금지)
   *
   * @param appId
   */
  public void deleteApp(Long appId) {

    try {

      if (isUsingApp(appId)) {

        throw new BizException(BizExceptionCode.COM006);
      }

      gwAppRepository.deleteById(appId);

      CommonUtils.refreshGatewayRoutes("");
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 입력 받은 서비스ID가 실제 DB에 존재 하는지 체크
   *
   * @param reqParam
   * @return
   */
  public boolean isValidServiceId(AppDTO reqParam) {

    return gwRoutingRepository.countByserviceIdIn(reqParam.getServiceId()) == reqParam
        .getServiceId().size();
  }

  /**
   * APP 이름이 DB에 존재하는지 체크
   *
   * @param reqParam
   * @return
   */
  public boolean isValidAppNm(AppDTO reqParam) {

    return gwAppRepository.findByAppNm(reqParam.getAppNm()).size() == 0;
  }

  /**
   * APP 수정 시 전달 받은 APP 이름이 DB에 존재하는지 체크
   *
   * @param reqParam
   * @return
   */
  public boolean isValidModifyAppNm(AppDTO reqParam, Long appId) {

    return gwAppRepository.findByAppNmAndAppIdNot(reqParam.getAppNm(), appId).size() == 0;
  }

  /**
   * 현재 사용중인 APP인지 체크
   *
   * @param appId
   * @return
   */
  public boolean isUsingApp(Long appId) {

    return gwServiceAppMappingRepository.countByIdAppId(appId) > 0;
  }

  /**
   * APP DTO -> 엔티티 변환 후 APP_KEY 설정
   *
   * @param reqParam
   * @return
   */
  public GwApp setGwAppWithAppKey(AppDTO reqParam) {

    GwApp gwApp = modelMapper.map(reqParam, GwApp.class);
    gwApp.setAppKey(StringUtils.generateAppKey());

    return gwApp;
  }

  /**
   * AppKey 유효성 체크
   *
   * @param appKey
   * @return
   */
  public boolean isEmptyAppKey(String appKey) {

    return StringUtils.isEmptyParams(appKey);
  }
}

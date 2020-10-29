package com.kbds.serviceapi.portal.app.service;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.common.utils.StringUtils;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.portal.api.dto.RoutingDTO;
import com.kbds.serviceapi.portal.api.entity.GwService;
import com.kbds.serviceapi.portal.api.repository.GwRoutingRepository;
import com.kbds.serviceapi.portal.app.dto.AppDTO;
import com.kbds.serviceapi.portal.app.dto.AppDetailDTO;
import com.kbds.serviceapi.portal.app.entity.GwApp;
import com.kbds.serviceapi.portal.app.repository.GwAppRepository;
import com.kbds.serviceapi.portal.app.repository.GwServiceAppMappingRepository;
import com.kbds.serviceapi.portal.app.repository.querydsl.GwAppCustomRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
   * @param searchDTO 검색 조건
   * @return  결과 목록
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
   * @param id  AppID
   * @return  상세 정보
   */
  @Transactional
  public AppDetailDTO findAppDetail(Long id) {

    GwApp gwApp;
    
    try {
      // APP 정보 조회
      Map<GwApp, List<GwService>> gwApps = gwAppCustomRepository.findAppDetailById(id);
      
      if(!gwApps.keySet().stream().findFirst().isPresent()){

        throw new BizException(BizExceptionCode.COM004);
      }

      gwApp = gwApps.keySet().stream().findFirst().get();

      // 하위 API 서비스 정보 변환
      List<RoutingDTO> routingDTOS = gwApps.get(gwApp).stream()
          .map(list -> modelMapper.map(list, RoutingDTO.class)).collect(Collectors.toList());

      return new AppDetailDTO(gwApp.getAppId(), gwApp.getAppNm(), gwApp.getAppKey()
          , gwApp.getAppDesc(), routingDTOS, gwApp.getRegUserNo(), gwApp.getUptUserNo(),
          gwApp.getRegDt(), gwApp.getUptDt());
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
   * @param reqParam  등록 정보
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
   * @param reqParam  수정 정보
   * @param appId APP ID
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
   * @param appId 삭제 대상 APP ID
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
   * @param reqParam  체크할 서비스 정보
   * @return  유효 여부
   */
  public boolean isValidServiceId(AppDTO reqParam) {

    return gwRoutingRepository.countByServiceIdIn(reqParam.getServiceId()) == reqParam
        .getServiceId().size();
  }

  /**
   * APP 이름이 DB에 존재하는지 체크
   *
   * @param reqParam  체크할 앱 정보
   * @return  유효 여부
   */
  public boolean isValidAppNm(AppDTO reqParam) {

    return gwAppRepository.findByAppNm(reqParam.getAppNm()).size() == 0;
  }

  /**
   * APP 수정 시 전달 받은 APP 이름이 DB에 존재하는지 체크
   *
   * @param reqParam  체크할 앱 정보
   * @return  유효 여부
   */
  public boolean isValidModifyAppNm(AppDTO reqParam, Long appId) {

    return gwAppRepository.findByAppNmAndAppIdNot(reqParam.getAppNm(), appId).size() == 0;
  }

  /**
   * 현재 사용중인 APP인지 체크
   *
   * @param appId APP ID
   * @return  유효 여부
   */
  public boolean isUsingApp(Long appId) {

    return gwServiceAppMappingRepository.countByIdAppId(appId) > 0;
  }

  /**
   * APP DTO -> 엔티티 변환 후 APP_KEY 설정
   *
   * @param reqParam  APP KEY 정보 저장할 객체
   * @return  저장 결과 객체
   */
  public GwApp setGwAppWithAppKey(AppDTO reqParam) {

    GwApp gwApp = modelMapper.map(reqParam, GwApp.class);
    gwApp.setAppKey(StringUtils.generateAppKey());

    return gwApp;
  }

  /**
   * AppKey 유효성 체크
   *
   * @param appKey  APPKEY
   * @return  유효 여부
   */
  public boolean isEmptyAppKey(String appKey) {

    return StringUtils.isEmptyParams(appKey);
  }
}

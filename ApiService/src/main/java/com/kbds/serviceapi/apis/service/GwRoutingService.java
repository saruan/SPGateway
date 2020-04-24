package com.kbds.serviceapi.apis.service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.kbds.serviceapi.apis.dto.EmptyJsonBody;
import com.kbds.serviceapi.apis.dto.RoutingDTO;
import com.kbds.serviceapi.apis.entity.GwService;
import com.kbds.serviceapi.apis.querydsl.GwRoutingCustomRepository;
import com.kbds.serviceapi.apis.repository.GwRoutingRepository;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.constants.CommonConstants;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.exception.BizException;

/**
 *
 * <pre>
 *  Class Name     : GwRoutingService.java
 *  Description    : 라우팅 서비스 클래스
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
public class GwRoutingService {

  // QueryDsl용 커스텀 레파지토리
  @Autowired
  GwRoutingCustomRepository gwServiceCustomRepository;

  @Autowired
  GwRoutingRepository gwServiceRepository;

  // 형 변환을 위한 ModelMapper
  @Autowired
  ModelMapper modelMapper;

  // Routes,Filter 관리 서버 주소
  @Value("${gateway.cluster.refreshUrl}")
  String gatewayRefreshUrl;

  /**
   * 서비스 검색 기능
   * 
   * @param params
   * @return
   */
  public List<RoutingDTO> findServices(RoutingDTO params) {

    try {

      return gwServiceCustomRepository.findbyConditions(params);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.COM001, e.getArg());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  };

  /**
   * 서비스 상세 검색 기능
   * 
   * @param params
   * @return
   */
  @Transactional
  public Object findServiceDetail(Long id) {

    try {

      // DB 상에서 해당 serviceId를 가진 Entity를 불러온다.
      Optional<GwService> gwService = gwServiceRepository.findById(id);

      if (!gwService.isPresent()) {

        return new EmptyJsonBody();
      }

      // 결과 값으로 전달할 RoutingDTO로 변환한 후 리턴한다.
      return modelMapper.map(gwService.get(), RoutingDTO.class);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.COM001, e.getArg());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  };


  /**
   * 서비스 등록
   * 
   * @param reqParam
   */
  public void registService(RoutingDTO reqParam) {

    // 필수 파라미터 체크
    // 항목 - 서비스명, 서비스 API URL 경로, 사용자
    if (StringUtils.isEmpty(reqParam.getServiceNm())) {

      throw new BizException(BizExceptionCode.COM002, "serviceNm");
    }

    if (StringUtils.isEmpty(reqParam.getServicePath())) {

      throw new BizException(BizExceptionCode.COM002, "servicePath");
    }

    if (StringUtils.isEmpty(reqParam.getRegUserNo())) {

      throw new BizException(BizExceptionCode.COM002, "regUserNo");
    }

    // 서비스가 이미 등록 된 서비스인지 체크한다.
    // 항목 - 서비스명, 서비스 API URL 경로
    RoutingDTO checkParam = new RoutingDTO();

    checkParam.setServiceNm(reqParam.getServiceNm());
    checkParam.setServicePath(reqParam.getServicePath());
    checkParam.setFilterId(reqParam.getFilterId());

    if (gwServiceCustomRepository.checkRegistValidation(checkParam)) {

      throw new BizException(BizExceptionCode.COM003, BizExceptionCode.COM003.getMsg());
    }

    try {

      // DTO -> Entity로 형변환을 한 후 DB에 저장한다.
      gwServiceRepository.save(modelMapper.map(reqParam, GwService.class));

      // 등록 이후 게이트웨이에 해당 정보를 갱신해준다.
      CommonUtils.refreshGatewayRoutes(gatewayRefreshUrl, reqParam.getRegUserNo());

    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  };

  /**
   * 서비스 수정
   * 
   * @param reqParam
   */
  @Transactional
  public void updateService(RoutingDTO reqParam, Long id) {

    // 필수 파라미터 체크
    // 항목 - 서비스 ID, 수정자, 타겟 URL
    if (id == null) {

      throw new BizException(BizExceptionCode.COM002, "serviceId");
    }

    if (reqParam.getUptUserNo() == null) {

      throw new BizException(BizExceptionCode.COM002, "uptUserNo");
    }

    if (reqParam.getServiceTargetUrl() == null) {

      throw new BizException(BizExceptionCode.COM002, "serviceTargetUrl");
    }

    // DB 상에서 해당 serviceId를 가진 Entity를 불러온다.
    GwService gwService = gwServiceRepository.findById(id).get();

    if (gwService == null) {

      throw new BizException(BizExceptionCode.COM004, BizExceptionCode.COM004.getMsg());
    }

    // 수정하고자 하는 내용 중 중복이 허용되지 않는 데이터가 DB상에 등록되어 있는지 체크한다.
    // 항목 - 서비스명, 서비스 API URL 경로
    RoutingDTO checkParam = new RoutingDTO();

    checkParam.setServiceNm(reqParam.getServiceNm());
    checkParam.setServicePath(reqParam.getServicePath());
    checkParam.setFilterId(reqParam.getFilterId());
    checkParam.setServiceId(reqParam.getServiceId());

    if (gwServiceCustomRepository.checkUpdateValidation(checkParam, id)) {

      throw new BizException(BizExceptionCode.COM003, BizExceptionCode.COM003.getMsg());
    }

    try {

      // 데이터 정합성 체크가 끝나면 최종적으로 서비스를 갱신 시킨다.
      gwService.setServiceNm(reqParam.getServiceNm());
      gwService.setServicePath(reqParam.getServicePath());
      gwService.getFilter().setFilterId(reqParam.getFilterId());
      gwService.setServiceDesc(reqParam.getServiceDesc());
      gwService.setUptUserNo(reqParam.getUptUserNo());
      gwService.setServiceLoginType(reqParam.getServiceLoginType());
      gwService.setServiceAuthType(reqParam.getServiceAuthType());
      gwService.setServiceTargetUrl(reqParam.getServiceTargetUrl());

      gwServiceRepository.save(gwService);

      // 수정 이후 게이트웨이에 해당 정보를 갱신해준다.
      CommonUtils.refreshGatewayRoutes(gatewayRefreshUrl, reqParam.getUptUserNo());

    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }


  /**
   * 서비스 삭제
   * 
   * @param id
   */
  public void deleteService(Long id) {

    // 필수 파라미터 체크
    // 항목 - 서비스 ID, 수정자
    if (id == null) {

      throw new BizException(BizExceptionCode.COM002, "serviceId");
    }

    // DB 상에서 해당 serviceId를 가진 Entity를 불러온다.
    GwService gwService = gwServiceRepository.findById(id).get();

    if (gwService == null) {

      throw new BizException(BizExceptionCode.COM004, BizExceptionCode.COM004.getMsg());
    }

    // 서비스 사용 유무를 변경한다.
    gwService.setUseYn(CommonConstants.N);

    gwServiceRepository.save(gwService);
  }

}

package com.kbds.serviceapi.portal.api.service;

import com.kbds.serviceapi.apis.dto.EmptyDataDTO;
import com.kbds.serviceapi.portal.api.dto.RoutingDTO;
import com.kbds.serviceapi.portal.api.entity.GwService;
import com.kbds.serviceapi.apis.entity.GwServiceFilter;
import com.kbds.serviceapi.apis.repository.GwFilterRepository;
import com.kbds.serviceapi.portal.api.repository.GwRoutingRepository;
import com.kbds.serviceapi.portal.api.repository.querydsl.GwRoutingCustomRepository;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.utils.CommonUtils;
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
 *  Class Name     : GwRoutingService.java
 *  Description    : 라우팅 서비스 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자                변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Service
public class GwRoutingService {

  @Autowired
  GwRoutingCustomRepository gwServiceCustomRepository;

  @Autowired
  GwRoutingRepository gwServiceRepository;

  @Autowired
  GwFilterRepository gwFilterRepository;

  @Autowired
  ModelMapper modelMapper;

  /**
   * G/W Routing Bean으로 등록할 정보들 조회 서비스
   *
   * @return G/W API List
   */
  @Transactional
  public List<RoutingDTO> findGwServices() {

    try {

      return gwServiceCustomRepository.findByGwConditions();
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 서비스 검색 기능
   *
   * @param searchDTO 검색 DTO 클래스
   * @return 검색 결과에 맞는 API 목록
   */
  public List<RoutingDTO> findServices(SearchDTO searchDTO) {

    try {

      return gwServiceCustomRepository.findByConditions(searchDTO);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 서비스 상세 검색 기능
   *
   * @return API 상세 결과
   */
  @Transactional
  public Object findServiceDetail(Long id) {

    try {

      Optional<GwService> gwService = gwServiceRepository.findById(id);

      // 검색 결과가 없으면 빈 결과 값을 리턴
      if (!gwService.isPresent()) {

        return new EmptyDataDTO();
      }

      return modelMapper.map(gwService.get(), RoutingDTO.class);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 서비스 등록
   *
   * @param reqParam 서비스 등록 파라미터
   */
  public void registerService(RoutingDTO reqParam) {

    if (gwServiceCustomRepository.isRegisteredService(reqParam)) {

      throw new BizException(BizExceptionCode.COM003);
    }

    try {

      gwServiceRepository.save(modelMapper.map(reqParam, GwService.class));

      // 등록 이후 게이트웨이에 해당 정보를 갱신해준다.
      CommonUtils.refreshGatewayRoutes(reqParam.getRegUserNo());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 서비스 수정
   *
   * @param reqParam API 수정 파라미터
   * @param id       서비스 아이디
   */
  @Transactional
  public void updateService(RoutingDTO reqParam, Long id) {

    GwService gwService;

    try {

      // DB 상에서 해당 serviceId를 가진 Entity를 불러온다.
      gwService = gwServiceRepository.findByServiceId(id);

      // 해당 데이터가 없다면 화면으로 오류 전달
      if (gwService == null) {

        throw new BizException(BizExceptionCode.COM004);
      }

      // 수정하고자 하는 내용 중 중복이 허용되지 않는 데이터가 DB상에 등록되어 있는지 체크한다.
      if (!gwServiceCustomRepository.isValidUpdateData(reqParam, id)) {

        throw new BizException(BizExceptionCode.COM003);
      }

      GwServiceFilter serviceFilter = new GwServiceFilter();
      serviceFilter.setFilterId(reqParam.getFilterId());

      // 데이터 정합성 체크가 끝나면 최종적으로 서비스를 갱신 시킨다.
      gwService.setServiceNm(reqParam.getServiceNm());
      gwService.setServicePath(reqParam.getServicePath());
      gwService.setFilter(serviceFilter);
      gwService.setServiceDesc(reqParam.getServiceDesc());
      gwService.setUptUserNo(reqParam.getUptUserNo());
      gwService.setServiceLoginType(reqParam.getServiceLoginType());
      gwService.setServiceAuthType(reqParam.getServiceAuthType());
      gwService.setServiceTargetUrl(reqParam.getServiceTargetUrl());

      gwServiceRepository.save(gwService);

      // 수정 이후 게이트웨이에 해당 정보를 갱신해준다.
      CommonUtils.refreshGatewayRoutes(reqParam.getUptUserNo());
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()), e.toString());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 서비스 삭제
   *
   * @param serviceId 서비스 ID
   */
  @Transactional
  public long deleteService(Long serviceId) {

    long deletedCnt;

    // 필수 파라미터 체크, 기본적으로 API 전체 삭제는 허용하지 않는다.
    if (serviceId == null) {

      throw new BizException(BizExceptionCode.COM002);
    }

    try {

      if (isServiceUsingInApp(gwServiceRepository.countByGwAppIdServiceId(serviceId))) {

        throw new BizException(BizExceptionCode.COM009);
      }

      // 서비스 사용 유무를 변경한다.
      deletedCnt = gwServiceCustomRepository.deleteService(serviceId);

    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()), e.toString());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }

    return deletedCnt;
  }

  /**
   * 삭제하려는 API 현재 사용중인지
   *
   * @param cnt 사용중인 APP 수
   * @return 결과
   */
  public boolean isServiceUsingInApp(long cnt) {

    return cnt > 0;
  }
}

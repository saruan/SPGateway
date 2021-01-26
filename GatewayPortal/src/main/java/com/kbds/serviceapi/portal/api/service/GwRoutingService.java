package com.kbds.serviceapi.portal.api.service;

import com.kbds.serviceapi.apis.dto.EmptyDataDTO;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.utils.CommonUtils;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.portal.api.dto.RoutingDTO;
import com.kbds.serviceapi.portal.api.entity.GwService;
import com.kbds.serviceapi.portal.api.repository.GwRoutingRepository;
import com.kbds.serviceapi.portal.filter.entity.GwServiceFilter;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
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

  private final GwRoutingRepository gwServiceRepository;
  private final ModelMapper modelMapper;

  /**
   * Constructor Injection
   *
   * @param gwServiceRepository gwServiceRepository
   * @param modelMapper         modelMapper
   */
  public GwRoutingService(GwRoutingRepository gwServiceRepository,
      ModelMapper modelMapper) {
    this.gwServiceRepository = gwServiceRepository;
    this.modelMapper = modelMapper;
  }

  /**
   * G/W Routing Bean으로 등록할 정보들 조회 서비스
   *
   * @return G/W API List
   */
  @Transactional
  public List<RoutingDTO> findGwServices() {

    try {

      return gwServiceRepository.findByGwConditions();
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

      return gwServiceRepository.findByConditions(searchDTO);
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

    if (gwServiceRepository.isRegisteredService(reqParam)) {

      throw new BizException(BizExceptionCode.COM003);
    }

    try {

      gwServiceRepository.save(modelMapper.map(reqParam, GwService.class));

      // 등록 이후 게이트웨이에 해당 정보를 갱신해준다.
      CommonUtils.refreshGatewayRoutes();
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

    try {

      if (!gwServiceRepository.isValidUpdateData(reqParam, id)) {

        throw new BizException(BizExceptionCode.COM003);
      }

      GwServiceFilter serviceFilter = new GwServiceFilter();
      serviceFilter.setFilterId(reqParam.getFilterId());

      GwService gwService = modelMapper.map(reqParam, GwService.class);

      /* 변환 이후 추가 정보 설정 */
      gwService.setServiceId(id);
      gwService.setFilter(serviceFilter);

      gwServiceRepository.save(gwService);

      CommonUtils.refreshGatewayRoutes();
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

    try {

      if (isServiceUsingInApp(gwServiceRepository.countByGwAppIdServiceId(serviceId))) {

        throw new BizException(BizExceptionCode.COM009);
      }

      return gwServiceRepository.deleteService(serviceId);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()), e.toString());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 삭제하려는 API 현재 사용중인지 체크
   *
   * @param cnt 사용중인 APP 수
   * @return 결과
   */
  public boolean isServiceUsingInApp(long cnt) {

    return cnt > 0;
  }
}

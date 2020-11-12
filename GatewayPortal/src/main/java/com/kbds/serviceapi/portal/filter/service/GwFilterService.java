package com.kbds.serviceapi.portal.filter.service;

import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.framework.exception.BizException;
import com.kbds.serviceapi.portal.api.repository.GwRoutingRepository;
import com.kbds.serviceapi.portal.filter.dto.FilterDTO;
import com.kbds.serviceapi.portal.filter.entity.GwServiceFilter;
import com.kbds.serviceapi.portal.filter.repository.GwFilterRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *  Class Name     : GwRoutingService.java
 *  Description    : 필터 관리 서비스 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Service
public class GwFilterService {

  private final GwFilterRepository gwFilterRepository;
  private final GwRoutingRepository gwRoutingRepository;
  private final ModelMapper modelMapper;

  /**
   * Constructor Injection
   *
   * @param gwFilterRepository  gwFilterRepository
   * @param gwRoutingRepository gwRoutingRepository
   * @param modelMapper         modelMapper
   */
  public GwFilterService(GwFilterRepository gwFilterRepository,
      GwRoutingRepository gwRoutingRepository, ModelMapper modelMapper) {

    this.gwFilterRepository = gwFilterRepository;
    this.gwRoutingRepository = gwRoutingRepository;
    this.modelMapper = modelMapper;
  }

  /**
   * 필터 검색 기능
   *
   * @param searchDTO 검색 객체
   * @return 필터 목록
   */
  public List<FilterDTO> findFilters(SearchDTO searchDTO) {

    try {

      return gwFilterRepository.findByConditions(searchDTO);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 필터 상세 검색 기능
   *
   * @param filterId 필터 ID
   * @return 상세 데이터
   */
  @Transactional
  public Object findFilterDetail(Long filterId) {

    try {

      // DB 상에서 해당 filterId를 가진 Entity 를 불러온다.
      Optional<GwServiceFilter> gwServiceFilter = gwFilterRepository.findById(filterId);

      if (!gwServiceFilter.isPresent()) {

        throw new BizException(BizExceptionCode.COM004);
      }

      // 결과 값으로 전달할 FilterDTO로 변환한 후 리턴한다.
      return modelMapper.map(gwServiceFilter.get(), FilterDTO.class);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 필터링 서비스 등록
   *
   * @param reqParam 등록 파라미터
   */

  @Transactional
  public void registerFilter(FilterDTO reqParam) {

    if (!gwFilterRepository.isValidData(reqParam)) {

      throw new BizException(BizExceptionCode.COM003);
    }

    try {

      // DTO 를 Entity 로 변환 후 데이터를 적재한다.
      GwServiceFilter param = modelMapper.map(reqParam, GwServiceFilter.class);

      gwFilterRepository.save(param);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 필터 수정
   *
   * @param reqParam 수정 파라미터
   */
  @Transactional
  public void updateFilter(FilterDTO reqParam, Long filterId) {

    GwServiceFilter gwServiceFilter;

    try {

      // DB 상에서 해당 serviceId를 가진 Entity를 불러온다.
      gwServiceFilter = gwFilterRepository.findByFilterId(filterId);

      if (gwServiceFilter == null) {

        throw new BizException(BizExceptionCode.COM004);
      }

      reqParam.setFilterId(filterId);

      if (!gwFilterRepository.checkUpdateValidation(reqParam)) {

        throw new BizException(BizExceptionCode.COM003);
      }

      // 데이터 정합성 체크가 끝나면 최종적으로 서비스를 갱신 시킨다.
      gwServiceFilter.setFilterNm(reqParam.getFilterNm());
      gwServiceFilter.setFilterDesc(reqParam.getFilterDesc());
      gwServiceFilter.setFilterBean(reqParam.getFilterBean());
      gwServiceFilter.setUptUserNo(reqParam.getUptUserNo());

      gwFilterRepository.save(gwServiceFilter);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 필터 삭제
   *
   * @param filterId 필터 ID
   */
  @Transactional
  public void deleteFilter(Long filterId) {

    try {

      if (isUseFilterService(filterId)) {

        throw new BizException(BizExceptionCode.COM007);
      }

      gwFilterRepository.deleteById(filterId);

    } catch (BizException e) {

      throw new BizException(BizExceptionCode.valueOf(e.getMessage()));
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * FilterId를 사용중인 서비스가 존재하는지 체크
   *
   * @param filterId 필터 ID
   * @return 존재 여부
   */
  public boolean isUseFilterService(Long filterId) {

    return gwRoutingRepository.countByFilterFilterId(filterId) > 0;
  }
}

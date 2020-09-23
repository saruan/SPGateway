package com.kbds.serviceapi.apis.service;

import com.kbds.serviceapi.apis.dto.EmptyDataDTO;
import com.kbds.serviceapi.apis.dto.FilterDTO;
import com.kbds.serviceapi.apis.entity.GwServiceFilter;
import com.kbds.serviceapi.apis.repository.querydsl.GwFilterCustomRepository;
import com.kbds.serviceapi.apis.repository.querydsl.GwRoutingCustomRepository;
import com.kbds.serviceapi.apis.repository.GwFilterRepository;
import com.kbds.serviceapi.apis.repository.GwRoutingRepository;
import com.kbds.serviceapi.common.code.BizExceptionCode;
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

  @Autowired
  GwFilterRepository gwFilterRepository;

  @Autowired
  GwFilterCustomRepository gwFilterCustomRepository;

  @Autowired
  GwRoutingRepository gwRoutingRepository;

  @Autowired
  GwRoutingCustomRepository gwRoutingCustomRepository;

  @Autowired
  ModelMapper modelMapper;

  /**
   * 필터 검색 기능
   *
   * @param searchDTO
   * @return
   */
  public List<FilterDTO> findFilters(SearchDTO searchDTO) {

    try {

      return gwFilterCustomRepository.findByConditions(searchDTO);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 필터 상세 검색 기능
   *
   * @param filterId
   * @return
   */
  @Transactional
  public Object findFilterDetail(Long filterId) {

    try {

      // DB 상에서 해당 filterId를 가진 Entity를 불러온다.
      Optional<GwServiceFilter> gwServiceFilter = gwFilterRepository.findById(filterId);

      if (!gwServiceFilter.isPresent()) {

        return new EmptyDataDTO();
      }

      // 결과 값으로 전달할 FilterDTO로 변환한 후 리턴한다.
      return modelMapper.map(gwServiceFilter.get(), FilterDTO.class);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 필터링 서비스 등록
   *
   * @param reqParam
   */

  @Transactional
  public void registerFilter(FilterDTO reqParam) {

    if (gwFilterCustomRepository.isValidData(reqParam)) {

      throw new BizException(BizExceptionCode.COM003);
    }

    try {

      // DTO를 Entity로 변환 후 데이터를 적재한다.
      GwServiceFilter param = modelMapper.map(reqParam, GwServiceFilter.class);

      gwFilterRepository.save(param);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }


  /**
   * 필터 수정
   *
   * @param reqParam
   * @return
   */
  @Transactional
  public void updateFilter(FilterDTO reqParam, Long filterId) {

    GwServiceFilter gwServiceFilter;

    try {

      // DB 상에서 해당 serviceId를 가진 Entity를 불러온다.
      gwServiceFilter = gwFilterRepository.findByFilterId(filterId);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }

    if (gwServiceFilter == null) {

      throw new BizException(BizExceptionCode.COM004);
    }

    try {

      reqParam.setFilterId(filterId);

      if (!gwFilterCustomRepository.checkUpdateValidation(reqParam)) {

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
   * @param filterId
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
   * @param filterId
   * @return
   */
  public boolean isUseFilterService(Long filterId) {

    return gwRoutingRepository.countByFilterFilterId(filterId) > 0;
  }
}

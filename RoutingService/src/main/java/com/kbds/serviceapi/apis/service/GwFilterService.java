package com.kbds.serviceapi.apis.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kbds.serviceapi.apis.dto.EmptyJsonBody;
import com.kbds.serviceapi.apis.dto.FilterDTO;
import com.kbds.serviceapi.apis.entity.GwServiceFilter;
import com.kbds.serviceapi.apis.querydsl.GwFilterCustomRepository;
import com.kbds.serviceapi.apis.querydsl.GwRoutingCustomRepository;
import com.kbds.serviceapi.apis.repository.GwFilterRepository;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.code.CommonCode;
import com.kbds.serviceapi.framework.exception.BizException;

/**
 *
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
 *
 */
@Service
public class GwFilterService {

  @Autowired
  GwFilterRepository gwFilterRepository;

  @Autowired
  GwFilterCustomRepository gwFilterCustomRepository;

  @Autowired
  GwRoutingCustomRepository gwRoutingCustomRepository;

  @Autowired
  ModelMapper modelMapper;

  /**
   * 필터 검색 기능
   * 
   * @param params
   * @return
   */
  public List<FilterDTO> findFilters(FilterDTO params) {

    try {

      return gwFilterCustomRepository.findbyConditions(params);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  };

  /**
   * 필터 상세 검색 기능
   * 
   * @param params
   * @return
   */
  @Transactional
  public Object findFilterDetail(Long id) {

    try {

      // DB 상에서 해당 filterId를 가진 Entity를 불러온다.
      Optional<GwServiceFilter> gwServiceFilter = gwFilterRepository.findById(id);

      if (!gwServiceFilter.isPresent()) {

        return new EmptyJsonBody();
      }

      // 결과 값으로 전달할 FilterDTO로 변환한 후 리턴한다.
      return modelMapper.map(gwServiceFilter.get(), FilterDTO.class);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  };


  /**
   * 필터링 서비스 등록
   * 
   * @param reqParam
   */

  @Transactional
  public void registFilter(FilterDTO reqParam) {

    // 필수 파라미터 체크
    // 항목 - filterBean, 필터명, 사용자명
    if (StringUtils.isEmpty(reqParam.getFilterBean())) {

      throw new BizException(BizExceptionCode.COM002);
    }

    if (StringUtils.isEmpty(reqParam.getFilterNm())) {

      throw new BizException(BizExceptionCode.COM002);
    }

    if (StringUtils.isEmpty(reqParam.getRegUserNo())) {

      throw new BizException(BizExceptionCode.COM002);
    }

    // 필터가 이미 등록 된 필터인지 체크한다.
    // 항목 - 필터명, 필터 Bean
    FilterDTO checkParam = new FilterDTO();

    checkParam.setFilterBean(reqParam.getFilterBean());
    checkParam.setFilterNm(reqParam.getFilterNm());

    if (gwFilterCustomRepository.checkRegistValidation(checkParam)) {

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
   */
  @Transactional
  public void updateFilter(FilterDTO reqParam, Long id) {

    GwServiceFilter gwServiceFilter = null;

    // 필수 파라미터 체크
    // 항목 - 서비스 ID

    if (reqParam == null) {

      throw new BizException(BizExceptionCode.COM002);
    }

    if (id == null) {

      throw new BizException(BizExceptionCode.COM002);
    }

    if (reqParam.getUptUserNo() == null) {

      throw new BizException(BizExceptionCode.COM002);
    }

    try {
      // DB 상에서 해당 serviceId를 가진 Entity를 불러온다.
      gwServiceFilter = gwFilterRepository.findById(id).get();

    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }

    if (gwServiceFilter == null) {

      throw new BizException(BizExceptionCode.COM004);
    }

    // 수정하고자 하는 내용 중 중복이 허용되지 않는 데이터가 DB상에 등록되어 있는지 체크한다.
    // 항목 - 필터명, 필터 Bean, 필터 ID
    FilterDTO checkParam = new FilterDTO();

    checkParam.setFilterBean(reqParam.getFilterBean());
    checkParam.setFilterId(reqParam.getFilterId());
    checkParam.setFilterNm(reqParam.getFilterNm());

    if (gwFilterCustomRepository.checkUpdateValidation(checkParam)) {

      throw new BizException(BizExceptionCode.COM003);
    }

    // 데이터 정합성 체크가 끝나면 최종적으로 서비스를 갱신 시킨다.
    gwServiceFilter.setFilterNm(reqParam.getFilterNm());
    gwServiceFilter.setFilterDesc(reqParam.getFilterDesc());
    gwServiceFilter.setFilterBean(reqParam.getFilterBean());
    gwServiceFilter.setUptUserNo(reqParam.getUptUserNo());

    try {

      gwFilterRepository.save(gwServiceFilter);

    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }

  /**
   * 필터 삭제
   * 
   * @param reqParam
   */
  @Transactional
  public void deleteFilter(Long id) {

    GwServiceFilter gwServiceFilter = null;

    // 필수 파라미터 체크
    // 항목 - 서비스 ID
    if (id == null) {

      throw new BizException(BizExceptionCode.COM002);
    }

    try {

      gwServiceFilter = gwFilterRepository.findById(id).get();

      // 필터를 논리적으로 삭제한다.
      gwServiceFilter.setUseYn(CommonCode.N.getResultCode());
      gwFilterRepository.save(gwServiceFilter);

      // 해당 필터에 연결 되어 있는 Routing Service의 Filter 정보를 기본 필터로 변경한 후 사용여부를 N으로 변경한다.
      gwRoutingCustomRepository.updateServiceByFilter(gwServiceFilter.getFilterId());

    } catch (NoSuchElementException e) {

      throw new BizException(BizExceptionCode.COM004);
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  }
}

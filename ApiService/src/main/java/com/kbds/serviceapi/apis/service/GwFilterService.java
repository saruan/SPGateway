package com.kbds.serviceapi.apis.service;

import java.util.List;
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
import com.kbds.serviceapi.apis.repository.GwFilterRepository;
import com.kbds.serviceapi.common.code.BizExceptionCode;
import com.kbds.serviceapi.common.constants.CommonConstants;
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
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.COM001, e.getArg());
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
  public Object findServiceDetail(Long id) {

    try {

      // DB 상에서 해당 filterId를 가진 Entity를 불러온다.
      Optional<GwServiceFilter> gwServiceFilter = gwFilterRepository.findById(id);

      if (!gwServiceFilter.isPresent()) {

        return new EmptyJsonBody();
      }

      // 결과 값으로 전달할 FilterDTO로 변환한 후 리턴한다.
      return modelMapper.map(gwServiceFilter.get(), FilterDTO.class);
    } catch (BizException e) {

      throw new BizException(BizExceptionCode.COM001, e.getArg());
    } catch (Exception e) {

      throw new BizException(BizExceptionCode.COM001, e.toString());
    }
  };


  /**
   * 필터링 서비스 등록
   * 
   * @param reqParam
   */
  public void regiestFilter(FilterDTO reqParam) {

    // 필수 파라미터 체크
    // 항목 - filterBean, 필터명, 사용자명
    if (StringUtils.isEmpty(reqParam.getFilterBean())) {

      throw new BizException(BizExceptionCode.COM002, "filterBean");
    }

    if (StringUtils.isEmpty(reqParam.getFilterNm())) {

      throw new BizException(BizExceptionCode.COM002, "filterNm");
    }

    if (StringUtils.isEmpty(reqParam.getRegUserNo())) {

      throw new BizException(BizExceptionCode.COM002, "regUserNo");
    }

    // 필터가 이미 등록 된 필터인지 체크한다.
    // 항목 - 필터명, 필터 Bean
    FilterDTO checkParam = new FilterDTO();

    checkParam.setFilterBean(reqParam.getFilterBean());
    checkParam.setFilterNm(reqParam.getFilterNm());

    if (gwFilterCustomRepository.checkRegistValidation(checkParam)) {

      throw new BizException(BizExceptionCode.COM003, BizExceptionCode.COM003.getMsg());
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
  public void updateFilter(FilterDTO reqParam) {

    // 필수 파라미터 체크
    // 항목 - 서비스 ID
    if (reqParam.getFilterId() == null) {

      throw new BizException(BizExceptionCode.COM002, "filterId");
    }

    if (reqParam.getUptUserNo() == null) {

      throw new BizException(BizExceptionCode.COM002, "uptUserNo");
    }

    // DB 상에서 해당 serviceId를 가진 Entity를 불러온다.
    GwServiceFilter gwServiceFilter = gwFilterRepository.findById(reqParam.getFilterId()).get();

    if (gwServiceFilter == null) {

      throw new BizException(BizExceptionCode.COM004, BizExceptionCode.COM004.getMsg());
    }

    // 수정하고자 하는 내용 중 중복이 허용되지 않는 데이터가 DB상에 등록되어 있는지 체크한다.
    // 항목 - 필터명, 필터 Bean, 필터 ID
    FilterDTO checkParam = new FilterDTO();

    checkParam.setFilterBean(reqParam.getFilterBean());
    checkParam.setFilterId(reqParam.getFilterId());
    checkParam.setFilterNm(reqParam.getFilterNm());

    if (gwFilterCustomRepository.checkUpdateValidation(checkParam)) {

      throw new BizException(BizExceptionCode.COM003, BizExceptionCode.COM003.getMsg());
    }

    // 데이터 정합성 체크가 끝나면 최종적으로 서비스를 갱신 시킨다.
    gwServiceFilter.setFilterNm(reqParam.getFilterNm());
    gwServiceFilter.setFilterDesc(reqParam.getFilterDesc());
    gwServiceFilter.setFilterBean(reqParam.getFilterBean());
    gwServiceFilter.setUptUserNo(reqParam.getUptUserNo());

    gwFilterRepository.save(gwServiceFilter);
  }

  /**
   * 필터 삭제
   * 
   * @param reqParam
   */
  public void deleteFilter(FilterDTO reqParam) {

    // 필수 파라미터 체크
    // 항목 - 서비스 ID
    if (reqParam.getFilterId() == null) {

      throw new BizException(BizExceptionCode.COM002, "filterId");
    }

    // DB 상에서 해당 serviceId를 가진 Entity를 불러온다.
    GwServiceFilter gwServiceFilter = gwFilterRepository.findById(reqParam.getFilterId()).get();

    if (gwServiceFilter == null) {

      throw new BizException(BizExceptionCode.COM004, BizExceptionCode.COM004.getMsg());
    }

    // 데이터 정합성 체크가 끝나면 최종적으로 서비스를 갱신 시킨다.
    gwServiceFilter.setUseYn(CommonConstants.N);

    gwFilterRepository.save(gwServiceFilter);
  }
}

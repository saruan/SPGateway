package com.kbds.serviceapi.portal.api.repository.querydsl;

import com.kbds.serviceapi.framework.dto.SearchDTO;
import com.kbds.serviceapi.portal.api.dto.RoutingDTO;
import java.util.List;

/**
 * <pre>
 *  Class Name     : GwServiceCustomRepository.java
 *  Description    : 라우팅 서비스 QueryDsl 레파지토리
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-14     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
public interface GwRoutingCustomRepository {

  /**
   * 조건문에 맞는 서비스 검색
   *
   * @param searchDTO 검색 조건
   * @return  검색 결과 리스트
   */
  List<RoutingDTO> findByConditions(SearchDTO searchDTO);

  /**
   * G/W Bean 등록용 서비스 조회
   *
   * @return G/W 등록할 모든 API 목록
   */
  List<RoutingDTO> findByGwConditions();

  /**
   * 해당 데이터가 등록이 가능한 데이터인지 검증
   *
   * @param param 등록 파라미터
   * @return  기등록 여부
   */
  boolean isRegisteredService(RoutingDTO param);

  /**
   * 해당 데이터가 수정이 가능한 데이터인지 검증
   *
   * @param param 수정 파라미터
   * @param serviceId 서비스 ID
   * @return  수정 가능 여부
   */
  boolean isValidUpdateData(RoutingDTO param, Long serviceId);


  /**
   * 서비스 삭제
   *
   * @param serviceId 서비스 ID
   * @return 삭제된 ID
   */
  long deleteService(Long serviceId);

}

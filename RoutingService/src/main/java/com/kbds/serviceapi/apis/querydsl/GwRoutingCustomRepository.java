package com.kbds.serviceapi.apis.querydsl;

import com.kbds.serviceapi.apis.dto.RoutingDTO;
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
   * @param param
   * @return
   */
  public List<RoutingDTO> findbyConditions(RoutingDTO param);

  /**
   * G/W Bean 등록용 서비스 조회
   *
   * @return
   */
  public List<RoutingDTO> findbyGwConditions();

  /**
   * 해당 데이터가 등록이 가능한 데이터인지 검증
   *
   * @param param
   * @return
   */
  public boolean checkRegistValidation(RoutingDTO param);

  /**
   * 해당 데이터가 수정이 가능한 데이터인지 검증
   *
   * @param param
   * @return
   */
  public boolean checkUpdateValidation(RoutingDTO param, Long serviceId);


  /**
   * 서비스 삭제
   *
   * @param serviceId
   * @return
   */
  public long deleteService(Long[] serviceId);


  /**
   * 필터 삭제 시 해당 필터를 사용하는 서비스 정보 업데이트
   *
   * @param filterId
   * @return
   */
  public long updateServiceByFilter(Long filterId);

}

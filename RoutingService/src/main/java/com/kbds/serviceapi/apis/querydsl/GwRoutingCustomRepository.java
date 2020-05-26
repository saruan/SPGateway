package com.kbds.serviceapi.apis.querydsl;

import java.util.List;
import com.kbds.serviceapi.apis.dto.RoutingDTO;

/**
 * 
 *
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
 *
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
   * @param param
   * @return
   */
  public List<RoutingDTO> findbyGwConditions();

  /**
   * 서비스 등록 전 필수 파라미터 중복 체크
   * 
   * @param param
   * @return
   */
  public boolean checkRegistValidation(RoutingDTO param);

  /**
   * 서비스 수정 전 필수 파라미터 중복 체크
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

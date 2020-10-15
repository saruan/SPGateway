package com.kbds.serviceapi.apis.repository.querydsl;

import com.kbds.serviceapi.apis.dto.RoutingDTO;
import com.kbds.serviceapi.framework.dto.SearchDTO;
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
   * @param searchDTO
   * @return
   */
  public List<RoutingDTO> findByConditions(SearchDTO searchDTO);

  /**
   * G/W Bean 등록용 서비스 조회
   *
   * @return
   */
  public List<RoutingDTO> findByGwConditions();

  /**
   * 해당 데이터가 등록이 가능한 데이터인지 검증
   *
   * @param param
   * @return
   */
  public boolean isRegisteredService(RoutingDTO param);

  /**
   * 해당 데이터가 수정이 가능한 데이터인지 검증
   *
   * @param param
   * @return
   */
  public boolean isValidUpdateData(RoutingDTO param, Long serviceId);


  /**
   * 서비스 삭제
   *
   * @param serviceId
   * @return
   */
  public long deleteService(Long[] serviceId);

}

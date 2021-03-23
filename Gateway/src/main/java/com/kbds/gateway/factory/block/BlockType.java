package com.kbds.gateway.factory.block;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kbds.gateway.dto.BlockDTO;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

/**
 * <pre>
 *  File  Name     : BlockType
 *  Description    : Block Filter 에서 사용할 동적 Filter Interface
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-19          	 구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
public interface BlockType {

  /**
   * Filter 데이터 생성
   * @param blockDTO  파라미터
   * @param serverWebExchange ServerWebExchange 객체
   */
  void makeFilterData(BlockDTO blockDTO, ServerWebExchange serverWebExchange);

  /**
   * BlockType 이름 리턴
   *
   * @return BlockType 명
   */
  String getBlockTypeName();
}

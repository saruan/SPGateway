package com.kbds.gateway.factory.block;

import com.kbds.gateway.code.BlockCode.BlockType;
import com.kbds.gateway.dto.BlockDto;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
public interface Block {

  /**
   * Filter 데이터 생성
   * @param blockDTO  파라미터
   * @param serverWebExchange ServerWebExchange 객체
   */
  Mono<Void> makeFilterData(BlockDto blockDTO, ServerWebExchange serverWebExchange);

  /**
   * BlockType 이름 리턴
   *
   * @return BlockType 명
   */
  BlockType getBlockTypeName();
}

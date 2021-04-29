package com.kbds.gateway.filter.system;

import com.kbds.gateway.code.BlockCode.BlockServlet;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.dto.BlockDto;
import com.kbds.gateway.factory.block.Block;
import com.kbds.gateway.factory.block.BlockTypeFactory;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  File  Name     : BlockFilter
 *  Description    : API 로 정의한 사용자 정의 Filter
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-23          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Service
@AllArgsConstructor
public class BlockFilter extends AbstractGatewayFilterFactory<List<BlockDto>> {

  private final BlockTypeFactory blockTypeFactory;

  @Override
  public GatewayFilter apply(List<BlockDto> blockDtoList) {

    return (exchange, chain) -> {

      Mono<Void> preFilter = Mono.empty();

      for (BlockDto blockDto : blockDtoList) {

        /* Request Type Block Create */
        Block block = blockTypeFactory.makeGrantType(blockDto.getBlockType());

        if (BlockServlet.REQUEST.equals(blockDto.getBlockServlet())) {

          preFilter = preFilter.then(block.makeFilterData(blockDto, exchange));
        }
      }

      return preFilter.then(chain.filter(exchange));
    };
  }
}

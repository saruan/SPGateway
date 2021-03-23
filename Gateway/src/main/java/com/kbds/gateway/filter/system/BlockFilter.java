package com.kbds.gateway.filter.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kbds.gateway.code.AuthTypeCode;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.BlockDTO;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.factory.block.BlockType;
import com.kbds.gateway.factory.block.BlockTypeFactory;
import java.nio.charset.StandardCharsets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *  File  Name     : BlockFilter
 *  Description    :
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-23          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Component
@Slf4j
@Data
public class BlockFilter extends AbstractGatewayFilterFactory<BlockDTO> {

  @Autowired
  BlockTypeFactory blockTypeFactory;

  public BlockFilter() {

    super();
  }

  @Override
  public GatewayFilter apply(BlockDTO blockDTO) {

    return (exchange, chain) -> {

      BlockType blockType = blockTypeFactory.makeGrantType(blockDTO.getBlockType());

      if (blockDTO.getBlockServlet().equals(GatewayCode.HTTP_REQUEST.getCode())) {

        blockType.makeFilterData(blockDTO, exchange);
      }

      return chain.filter(exchange);
    };
  }
}

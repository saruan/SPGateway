package com.kbds.gateway.hystrix;

import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.BlockDTO;
import com.kbds.gateway.dto.ResponseDTO;
import com.kbds.gateway.factory.block.BlockType;
import com.kbds.gateway.factory.block.BlockTypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  File  Name     : FallbackController
 *  Description    : Circuit Breaker Fallback Controller
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-16         	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Slf4j
@RestController
public class FallbackController {

  @Autowired
  BlockTypeFactory blockTypeFactory;

  /**
   * FallBack Controller
   *
   * @return Gateway Timeout
   */
  @RequestMapping("/fallback")
  @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
  public Flux<ResponseDTO> returnFallback() {

    ResponseDTO responseDTO = new ResponseDTO();

    responseDTO.setResultCode(GatewayExceptionCode.GWE004.getCode());
    responseDTO.setResultMessage(GatewayExceptionCode.GWE004.getMsg());
    responseDTO.setResultMessage("");

    return Flux.just(responseDTO);
  }
}

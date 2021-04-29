package com.kbds.gateway.hystrix;

import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.ResponseDto;
import com.kbds.gateway.factory.block.BlockTypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
  public Flux<ResponseDto> returnFallback() {

    ResponseDto responseDTO = new ResponseDto();

    responseDTO.setResultCode(GatewayExceptionCode.GWE004.name());
    responseDTO.setResultMessage(GatewayExceptionCode.GWE004.getMsg());
    responseDTO.setResultMessage("");

    return Flux.just(responseDTO);
  }
}

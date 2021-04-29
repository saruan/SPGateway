package com.kbds.gateway.factory.block.impl;

import com.kbds.gateway.code.BlockCode.BlockType;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.BlockDto;
import com.kbds.gateway.dto.BlockDto.Assertion;
import com.kbds.gateway.dto.BlockDto.Log;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.factory.block.Block;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <pre>
 *  File  Name     : ReactiveRestBlock
 *  Description    : Reactive 형태의 Rest 통신 Block
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-04-21             구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Slf4j
@Component
@AllArgsConstructor
public class ReactiveRestBlock implements Block {

  private final WebClient webClient;
  private final AssertionBlock assertionBlock;
  private final LogBlock logBlock;

  @Override
  public Mono<Void> makeFilterData(BlockDto blockDTO, ServerWebExchange serverWebExchange) {

    List<Assertion> assertionList = blockDTO.getReactiveRest().getRestAssertions();
    List<Log> logList = blockDTO.getLog();

    String method = blockDTO.getReactiveRest().getMethod();
    String url = blockDTO.getReactiveRest().getUrl();

    return setHttpMethod(webClient, method, url)
        .flatMap(clientResponse -> {

          /* 응답 Status 가 오류일 경우 Exception 처리 */
          if (clientResponse.statusCode().isError()) {

            return clientResponse.bodyToMono(Map.class).flatMap(error -> Mono
                .error(new GatewayException(GatewayExceptionCode.GWE001,
                    clientResponse.statusCode(), error.toString())));
          }

          /* 통신 이후 작업 수행 */
          return clientResponse.bodyToMono(Map.class).flatMap(data -> {

            assertionBlock.analysisMessage(data, assertionList);
            logBlock.setLogMessage(data, logList, serverWebExchange);

            return Mono.empty();
          });
        });
  }

  @Override
  public BlockType getBlockTypeName() {

    return BlockType.REACTIVE_REST;
  }

  /**
   * HttpMethod 설정
   *
   * @param webClient WebClient 객체
   * @param method    HttpMethod 변수
   * @return RequestHeadersUriSpec
   */
  private Mono<ClientResponse> setHttpMethod(WebClient webClient, String method, String url) {

    if (HttpMethod.GET.matches(method)) {
      return webClient.get().uri(url).exchange();
    } else if (HttpMethod.POST.matches(method)) {
      return webClient.post().uri(url).exchange();
    } else if (HttpMethod.PUT.matches(method)) {
      return webClient.put().uri(url).exchange();
    } else if (HttpMethod.DELETE.matches(method)) {
      return webClient.delete().uri(url).exchange();
    } else {
      log.info("Invalid Http Method Type");
      throw new GatewayException(GatewayExceptionCode.GWE001, HttpStatus.BAD_REQUEST);
    }
  }
}

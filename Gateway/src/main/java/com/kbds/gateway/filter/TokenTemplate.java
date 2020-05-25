package com.kbds.gateway.filter;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.kbds.gateway.code.GatewayCode;
import com.kbds.gateway.code.GatewayExceptionCode;
import com.kbds.gateway.dto.RoutingDTO;
import com.kbds.gateway.exception.GatewayException;
import com.kbds.gateway.utils.StringUtils;

/**
 * 
 *
 * <pre>
 *  Class Name     : CommonTemplate.java
 *  Description    : 샘플용 공통 필터 템플릿
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 * 
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-20     구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 *
 */
@Service("TokenTemplate")
public class TokenTemplate extends AbstractGatewayFilterFactory<RoutingDTO> {

  // 로그용 변수
  Logger logger = LoggerFactory.getLogger(TokenTemplate.class);

  public TokenTemplate() {
    super();
  }

  @Override
  public GatewayFilter apply(RoutingDTO routingDTO) {

    return (exchange, chain) -> {

      Object attribute = exchange.getAttribute("cachedRequestBody");

      if (attribute instanceof DataBuffer) {

        DataBuffer buffer = (DataBuffer) attribute;

        Map<String, String> queryParam = StringUtils.queryToMap(buffer);

        if (!queryParam.containsKey(GatewayCode.REUQESTPARAM_SAML.getCode())) {

          throw new GatewayException(GatewayExceptionCode.GWE0001, HttpStatus.UNAUTHORIZED,
              "Invalid SAML");
        }

      } else {

        throw new GatewayException(GatewayExceptionCode.GWE0001, HttpStatus.UNAUTHORIZED,
            "Invalid Paramters");
      }

      return chain.filter(exchange);
    };
  }
}
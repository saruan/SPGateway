package com.kbds.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *  Class Name     : Resilience4jConfiguration.java
 *  Description    : Resilience4jConfiguration 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2021-03-16    	       구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Configuration
public class Resilience4jConfiguration {

  /**
   * Resilience4 기본 설정
   *
   * @return  ReactiveResilience4JCircuitBreakerFactory 설정 리턴
   */
  @Bean
  public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {

    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
        .failureRateThreshold(50)  /* 감지 실패율 임계 퍼센트 */
        .permittedNumberOfCallsInHalfOpenState(2) /* CircuitBreaker 오픈 시에 허용 호출 수 */
        .slidingWindowSize(3)   /* CircuitBreaker 닫혔을 때 결과 기록 사이즈 */
        .minimumNumberOfCalls(10)  /* CircuitBreaker 오픈 최소 오류 수 */
        .waitDurationInOpenState(Duration.ofMillis(10000))  /* CircuitBreaker 오픈 지속 시간 */
        .build();

    return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
        .circuitBreakerConfig(circuitBreakerConfig)
        .build());
  }
}
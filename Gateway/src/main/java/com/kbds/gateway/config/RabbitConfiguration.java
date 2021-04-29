package com.kbds.gateway.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *  Class Name     : RabbitConfiguration.java
 *  Description    : Message Queue 관련 추가 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-22    	       구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Configuration
public class RabbitConfiguration {

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
      MessageConverter messageConverter) {

    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);
    rabbitTemplate.setExchange("GATEWAY_EXCHANGE");

    return rabbitTemplate;
  }

  @Bean
  public MessageConverter messageConverter() {

    return new Jackson2JsonMessageConverter();
  }
}
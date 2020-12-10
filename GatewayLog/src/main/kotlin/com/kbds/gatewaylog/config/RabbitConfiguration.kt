package com.kbds.gatewaylog.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * <pre>
 *  File  Name     : RabbitConfiguration
 *  Description    : RabbitMQ Consumer 설정
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자        	       변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-05-22          	   구경태          Initialized
 * -------------------------------------------------------------------------------
 *  </pre>
 */
@Configuration
open class RabbitConfiguration {

    private val queueNm = "GATEWAY_LOG"
    private val exchangeNm = "GATEWAY_EXCHANGE"
    private val routingKey = "gateway.routing.#"

    @Bean
    open fun queue(): Queue {

        return Queue(queueNm, false)
    }

    @Bean
    open fun exchange(): TopicExchange {

        return TopicExchange(exchangeNm)
    }

    @Bean
    open fun binding(queue: Queue, exchange: TopicExchange): Binding {

        return BindingBuilder.bind(queue).to(exchange).with(routingKey)
    }

    @Bean
    open fun rabbitTemplate(connectionFactory: ConnectionFactory,
                            messageConverter: MessageConverter): RabbitTemplate {

        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter
        rabbitTemplate.setExchange(exchangeNm)

        return rabbitTemplate
    }

    @Bean
    open fun messageConverter(): MessageConverter? {
        return Jackson2JsonMessageConverter()
    }
}
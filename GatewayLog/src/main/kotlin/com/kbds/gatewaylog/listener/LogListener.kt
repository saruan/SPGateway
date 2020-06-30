package com.kbds.gatewaylog

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kbds.gatewaylog.entity.ServiceLog
import com.kbds.gatewaylog.repository.ServiceLogRepository
import com.kbds.gatewaylog.utils.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.text.ParseException

/**
 * <pre>
 *  Class Name     : LogListener.kt
 *  Description    : 서비스 로그를 큐에서 읽어 DB에 적재하는 Listener 클래스
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자                변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-20     	      구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Component
class LogListener {

    @Autowired
    lateinit var serviceLogRepository: ServiceLogRepository

    @KafkaListener(topics = ["GATEWAY_LOG"], groupId = "GATEWAY_LOG_GROUP")
    fun receive(serviceLog: String) {

        val mapper = jacksonObjectMapper()
        val logData = mapper.readValue<ServiceLog>(serviceLog)

        try {

            DateUtils.validateDateFormat(logData.requestDt, logData.responseDt)
            serviceLogRepository.save(logData)
        } catch (e: ParseException) {

            print("Invalid Date Format")
        }
    }
}
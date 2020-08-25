package com.kbds.gatewaylog

import com.kbds.gatewaylog.entity.ServiceLog
import com.kbds.gatewaylog.repository.ServiceLogRepository
import com.kbds.gatewaylog.utils.DateUtils
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
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

    @RabbitListener(queues = ["GATEWAY_LOG"])
    fun receive(serviceLog: ServiceLog) {

        try {

            DateUtils.validateDateFormat(serviceLog.requestDt, serviceLog.responseDt)
            serviceLogRepository.save(serviceLog)
        } catch (e: ParseException) {

            print("Invalid Date Format")
        }
    }
}
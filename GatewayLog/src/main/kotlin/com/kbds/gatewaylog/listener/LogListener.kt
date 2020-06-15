package com.kbds.gatewaylog

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kbds.gatewaylog.entity.ServiceLog
import com.kbds.gatewaylog.repository.ServiceLogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class LogListener {
	
	@Autowired
	lateinit var serviceLogRepository : ServiceLogRepository
	
	@KafkaListener(topics = ["GATEWAY_LOG"], groupId = "GATEWAY_LOG_GROUP")
	fun receive(serviceLog: String) {
		
		val mapper = jacksonObjectMapper()
				
		val logData = mapper.readValue<ServiceLog>(serviceLog)

		serviceLogRepository.save(logData)
	}
}
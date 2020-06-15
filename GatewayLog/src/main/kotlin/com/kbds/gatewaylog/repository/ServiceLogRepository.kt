package com.kbds.gatewaylog.repository

import org.springframework.data.repository.CrudRepository
import com.kbds.gatewaylog.entity.ServiceLog

interface ServiceLogRepository : CrudRepository<ServiceLog, Int>{
	
}
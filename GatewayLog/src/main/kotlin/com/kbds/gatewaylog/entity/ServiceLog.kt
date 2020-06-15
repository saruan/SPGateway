package com.kbds.gatewaylog.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue

@Entity(name = "SERVICE_LOG")
data class ServiceLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Int,
	var requestHeader: String,
	var requestParams: String,
	var response: String,
	var service: String
)
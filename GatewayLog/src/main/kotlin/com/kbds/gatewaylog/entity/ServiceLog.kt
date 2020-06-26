package com.kbds.gatewaylog.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * <pre>
 *  Class Name     : ServiceLog.kt
 *  Description    : 서비스 로그 Entity
 *  Author         : 구경태 (kyungtae.koo@kbfg.com)
 *
 * -------------------------------------------------------------------------------
 *     변경No        변경일자                변경자          Description
 * -------------------------------------------------------------------------------
 *     Ver 1.0      2020-04-20     		   구경태          Initialized
 * -------------------------------------------------------------------------------
 * </pre>
 */
@Entity(name = "SERVICE_LOG")
data class ServiceLog(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int,
        var requestHeader: String = "",
        var requestParams: String = "",
        var response: String = "",
        var appKey: String = "",
        val serviceNm: String = "",
        val clientService: String = "",
        var requestDt: String,
        var responseDt: String
)
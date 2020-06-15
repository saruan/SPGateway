package com.kbds.gatewaylog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

@SpringBootApplication
open class GatewayLogApplication

fun main(args: Array<String>) {
	runApplication<GatewayLogApplication>(*args)
}

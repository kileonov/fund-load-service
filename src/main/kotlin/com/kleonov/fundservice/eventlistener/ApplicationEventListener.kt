package com.kleonov.fundservice.eventlistener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kleonov.fundservice.model.FundRequest
import com.kleonov.fundservice.service.FundService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.util.ResourceUtils

@Configuration
class ApplicationEventListener(
    private val fundService: FundService,
    private val objectMapper: ObjectMapper,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun consumePayload() {
        val input = "classpath:input.txt"

        try {
            val lines = ResourceUtils.getFile(input).readLines()
            for (line in lines) {
                val fundRequest = objectMapper.readValue<FundRequest>(line)
                fundService.loadFund(fundRequest)
            }
        } catch (e: Exception) {
            println("Error reading file: $e")
        }
    }
}
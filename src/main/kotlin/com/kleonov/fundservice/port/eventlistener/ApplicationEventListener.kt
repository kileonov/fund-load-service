package com.kleonov.fundservice.port.eventlistener

import com.fasterxml.jackson.databind.ObjectMapper
import com.kleonov.fundservice.adapter.SystemFileAdapter
import com.kleonov.fundservice.mapper.mapToResponse
import com.kleonov.fundservice.model.FundRequest
import com.kleonov.fundservice.port.model.FundResponse
import com.kleonov.fundservice.service.FundService
import com.kleonov.fundservice.service.IdempotenceLoadFundException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class ApplicationEventListener(
    private val systemFileAdapter: SystemFileAdapter,
    private val fundService: FundService,
    private val objectMapper: ObjectMapper,
) {

    val latch = CountDownLatch(1)

    private val log = KotlinLogging.logger {  }

    @EventListener(ApplicationReadyEvent::class)
    fun consumePayload() {
        try {
            val lines = systemFileAdapter.readFileLines("input.txt")
            val response = mutableListOf<FundResponse>()
            for (line in lines) {
                val fundRequest = objectMapper.readValue(line, FundRequest::class.java)
                log.info { "Loading funds with id ${fundRequest.id} for customerId ${fundRequest.customerId}" }

                val loadFund = fundService.loadFund(fundRequest)
                loadFund.fold(
                    ifLeft = {
                        if (it !is IdempotenceLoadFundException) {
                            response += fundRequest.mapToResponse()
                        }
                    },
                    ifRight = {
                        response += it.mapToResponse()
                    }
                )
            }
            if (response.isNotEmpty()) {
                systemFileAdapter.writeLinesToFile(
                    lines = response.map { objectMapper.writeValueAsString(it) },
                    fileName = "output.txt"
                )
            }
        } catch (e: Exception) {
            log.error { "Error raised during loading funds. Error: $e" }
            throw e
        } finally {
            log.info { "Loaded all funds" }
            latch.countDown()
        }
    }
}
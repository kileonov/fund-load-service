package com.kleonov.fundservice.datautil

import com.kleonov.fundservice.domain.Fund
import com.kleonov.fundservice.model.FundRequest
import com.kleonov.fundservice.port.model.FundResponse
import java.math.BigDecimal
import java.time.OffsetDateTime

object FundLoadTestUtil {

    const val inputLoadFundJson = """{"id":"123","customer_id":"456","load_amount":"${'$'}123.45","time":"2000-01-01T00:00:00Z"}"""
    const val outputAcceptedFundJson = """{"id":"123","customer_id":"456","accepted":true}"""
    const val outputDeclinedFundJson = """{"id":"123","customer_id":"456","accepted":false}"""

    fun createLoadFundRequest(): FundRequest = FundRequest(
        id = "123",
        customerId = "456",
        loadAmount = BigDecimal("123.45"),
        time = OffsetDateTime.parse("2000-01-01T00:00:00Z")
    )

    fun createLoadAcceptedFundResponse(): FundResponse = FundResponse(
        id = "123",
        customerId = "456",
        accepted = true
    )

    fun createLoadDeclinedFundResponse(): FundResponse = FundResponse(
        id = "123",
        customerId = "456",
        accepted = false
    )
}

fun createLoadFund(): Fund = Fund(
    id = "123",
    customerId = "456",
    loadAmount = BigDecimal("123.45"),
    time = OffsetDateTime.parse("2000-01-01T00:00:00Z"),
    createdAt = OffsetDateTime.parse("2000-01-01T00:00:00Z"),
    updatedAt = OffsetDateTime.parse("2000-01-01T00:00:00Z")
)

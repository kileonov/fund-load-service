package com.kleonov.fundservice.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.kleonov.fundservice.configuration.CurrencyBigDecimalJsonDeserializer
import java.math.BigDecimal
import java.time.OffsetDateTime

data class FundRequest(
    val id: String,
    @field:JsonProperty("customer_id")
    val customerId: String,
    @field:JsonProperty("load_amount")
    @field:JsonDeserialize(using = CurrencyBigDecimalJsonDeserializer::class)
    val loadAmount: BigDecimal,
    val time: OffsetDateTime,
) {
    init {
        verifyLoadAmount()
    }

    private fun verifyLoadAmount() {
        if (loadAmount <= BigDecimal.ZERO) {
            throw NonPositiveLoadAmountRequestException("Fund request cannot have non-positive loadAmount")
        }
        if (loadAmount.scale() != 2) {
            throw InvalidDecimalPlacesRequestException("Fund request can have only 2 decimal numbers")
        }
    }
}

class NonPositiveLoadAmountRequestException(msg: String) : RuntimeException(msg)
class InvalidDecimalPlacesRequestException(msg: String) : RuntimeException(msg)
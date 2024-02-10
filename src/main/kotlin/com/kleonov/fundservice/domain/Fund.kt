package com.kleonov.fundservice.domain

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Fund(
    val id: String,
    val customerId: String,
    val loadAmount: BigDecimal,
    val time: OffsetDateTime,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
) {
    init {
        verifyLoadAmount()
    }

    private fun verifyLoadAmount() {
        if (loadAmount <= BigDecimal.ZERO) {
            throw NonPositiveLoadAmountException("Fund cannot have non-positive loadAmount")
        }
        if (loadAmount.scale() != 2) {
            throw InvalidDecimalPlacesException("Fund request can have only 2 decimal numbers")
        }
    }
}

class NonPositiveLoadAmountException(msg: String) : RuntimeException(msg)
class InvalidDecimalPlacesException(msg: String) : RuntimeException(msg)
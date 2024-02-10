package com.kleonov.fundservice.port.model

import com.fasterxml.jackson.annotation.JsonProperty

data class FundResponse(
    val id: String,
    @field:JsonProperty("customer_id")
    val customerId: String,
    val accepted: Boolean
)
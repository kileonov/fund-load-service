package com.kleonov.fundservice.mapper

import com.kleonov.fundservice.domain.Fund
import com.kleonov.fundservice.model.FundRequest
import com.kleonov.fundservice.port.model.FundResponse

fun Fund.mapToResponse() : FundResponse =
    FundResponse(
        id = id,
        customerId = customerId,
        accepted = true
    )

fun FundRequest.mapToResponse() : FundResponse =
    FundResponse(
        id = id,
        customerId = customerId,
        accepted = false
    )
package com.kleonov.fundservice.service

import com.kleonov.fundservice.model.FundRequest
import org.springframework.stereotype.Service

@Service
class FundService {

    fun loadFund(fundRequest: FundRequest) {
        println(fundRequest)
    }
}
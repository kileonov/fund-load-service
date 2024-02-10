package com.kleonov.fundservice.service

import com.kleonov.fundservice.model.FundRequest
import com.kleonov.fundservice.repository.FundRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class FundService(private val fundRepository: FundRepository) {

    fun loadFund(fundRequest: FundRequest) {
        transaction {
            println(fundRequest)
            val createFund = fundRepository.createFund(fundRequest)

            println(createFund)
        }
    }
}
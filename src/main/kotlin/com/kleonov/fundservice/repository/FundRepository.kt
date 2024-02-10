package com.kleonov.fundservice.repository

import com.kleonov.fundservice.domain.Fund
import com.kleonov.fundservice.model.FundRequest
import com.kleonov.fundservice.repository.tables.Funds
import com.kleonov.fundservice.repository.tables.toOffsetDateTime
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class FundRepository {

    fun createFund(fund: FundRequest): Fund {
        val insertedValues = Funds.insert {
            it[id] = fund.id
            it[customer_id] = fund.customerId
            it[load_amount] = fund.loadAmount.toString()
            it[time] = fund.time.toInstant().toKotlinInstant()
        }.resultedValues!!.first()

        return with(insertedValues) {
            Fund(
                id = this[Funds.id],
                customerId =  this[Funds.customer_id],
                loadAmount = BigDecimal(this[Funds.load_amount]),
                time = toOffsetDateTime(this[Funds.time]),
                createdAt = toOffsetDateTime(this[Funds.createdAt]),
                updatedAt = toOffsetDateTime(this[Funds.updatedAt]),
            )
        }
    }
}
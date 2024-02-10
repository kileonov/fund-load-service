package com.kleonov.fundservice.repository

import arrow.core.some
import com.kleonov.fundservice.domain.Fund
import com.kleonov.fundservice.model.FundRequest
import com.kleonov.fundservice.repository.filter.FundFilter
import com.kleonov.fundservice.repository.filter.FundInRange
import com.kleonov.fundservice.repository.tables.Funds
import com.kleonov.fundservice.repository.tables.mapToDomain
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository

@Repository
class FundRepository {

    fun createFund(fund: FundRequest): Fund {
        val insertedValues = Funds.insert {
            it[id] = fund.id
            it[customerId] = fund.customerId
            it[loadAmount] = fund.loadAmount.toString()
            it[time] = fund.time.toInstant().toKotlinInstant()
        }.resultedValues!!.first()

        return insertedValues.mapToDomain()
    }

    fun getById(id: String, customerId: String): Fund? {
        return getByFilter(FundFilter(id = id.some(), customerId = customerId.some())).firstOrNull()
    }

    fun getByFilter(filter: FundFilter, fundInRange: FundInRange? = null): List<Fund> {
        val select  = Funds.selectAll()

        filter.id.map { select.andWhere { Funds.id eq it } }
        filter.customerId.map { select.andWhere { Funds.customerId eq it } }
        fundInRange?.let { select.andWhere { Funds.time greaterEq it.getRange().toKotlinInstant() } }

        return select.map { it.mapToDomain() }
    }
}
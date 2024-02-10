package com.kleonov.fundservice.repository.tables

import com.kleonov.fundservice.domain.Fund
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.ZoneId

object Funds : Table("fund") {
    val id: Column<String> = varchar("id", 255)
    val customerId: Column<String> = varchar("customer_id", 255)
    val loadAmount: Column<String> = varchar("load_amount", 255)
    val time: Column<Instant> = timestamp("time")
    val createdAt: Column<Instant> = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt: Column<Instant> = timestamp("updated_at").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(id, customerId)
}

fun toOffsetDateTime(instant: Instant): OffsetDateTime = with(instant.toJavaInstant()) {
    this.atOffset(ZoneId.systemDefault().rules.getOffset(this))
}

fun ResultRow.mapToDomain(): Fund =
    Fund(
        id = this[Funds.id],
        customerId = this[Funds.customerId],
        loadAmount = BigDecimal(this[Funds.loadAmount]),
        time = toOffsetDateTime(this[Funds.time]),
        createdAt = toOffsetDateTime(this[Funds.createdAt]),
        updatedAt = toOffsetDateTime(this[Funds.updatedAt]),
    )
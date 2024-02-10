package com.kleonov.fundservice.repository.tables

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.time.OffsetDateTime
import java.time.ZoneId

object Funds : Table("fund") {
    val id: Column<String> = varchar("id", 255)
    val customer_id: Column<String> = varchar("customer_id", 255)
    val load_amount: Column<String> = varchar("load_amount", 255)
    val time: Column<Instant> = timestamp("time")
    val createdAt: Column<Instant> = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt: Column<Instant> = timestamp("updated_at").clientDefault { Clock.System.now() }
}

fun toOffsetDateTime(instant: Instant): OffsetDateTime = with(instant.toJavaInstant()) {
    this.atOffset(ZoneId.systemDefault().rules.getOffset(this))
}
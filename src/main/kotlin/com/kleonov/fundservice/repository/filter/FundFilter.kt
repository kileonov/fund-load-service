package com.kleonov.fundservice.repository.filter

import arrow.core.Option
import arrow.core.none
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.temporal.TemporalAdjusters

sealed interface FundInRange {
    fun getRange(): Instant
}
data class FundInADayRange(private val from: OffsetDateTime) : FundInRange {
    override fun getRange(): Instant {
        return OffsetDateTime.of(from.toLocalDate(), LocalTime.MIN, from.offset).toInstant()
    }
}

data class FundInAWeekRange(private val from: OffsetDateTime) : FundInRange {
    override fun getRange(): Instant {
        val weekStartMidnight = from.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        return weekStartMidnight.toInstant()
    }
}

data class FundFilter(
    val id: Option<String> = none(),
    val customerId: Option<String> = none()
)
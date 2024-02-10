package com.kleonov.fundservice.service

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.some
import com.kleonov.fundservice.domain.Fund
import com.kleonov.fundservice.model.FundRequest
import com.kleonov.fundservice.properties.LoadFundProperty
import com.kleonov.fundservice.repository.FundRepository
import com.kleonov.fundservice.repository.filter.FundFilter
import com.kleonov.fundservice.repository.filter.FundInADayRange
import com.kleonov.fundservice.repository.filter.FundInAWeekRange
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

sealed class LoadFundException(msg: String): RuntimeException(msg)
class IdempotenceLoadFundException(msg: String): LoadFundException(msg)
class DailyNumOfTransactionsLoadFundException(msg: String): LoadFundException(msg)
class DailyMoneyAmountLimitLoadFundException(msg: String): LoadFundException(msg)
class WeeklyMoneyAmountLoadFundException(msg: String): LoadFundException(msg)

@Service
@Transactional
class FundService(
    private val fundRepository: FundRepository,
    private val loadFundProperty: LoadFundProperty
) {

    private val log = KotlinLogging.logger {  }

    fun loadFund(fundRequest: FundRequest): Either<LoadFundException, Fund> = either {
        ensure(fundRepository.getById(id = fundRequest.id, customerId = fundRequest.customerId) == null) {
            IdempotenceLoadFundException("Fund with id ${fundRequest.id} " +
                "for customerId ${fundRequest.customerId} has already been loaded in the past"
            )
        }

        val fundsInADayRange = fundRepository.getByFilter(
            filter = FundFilter(customerId = fundRequest.customerId.some()),
            fundInRange = FundInADayRange(fundRequest.time)
        )
        ensure(fundsInADayRange.size < loadFundProperty.dailyTransactionsLimit) {
            DailyNumOfTransactionsLoadFundException("Fund with id ${fundRequest.id} " +
                "for customerId ${fundRequest.customerId} has already reached max number of daily transactions"
            )
        }

        val sumInADay = computeSum(fundsInADayRange)
        ensure(sumInADay + fundRequest.loadAmount < loadFundProperty.dailyMoneyLimit) {
            DailyMoneyAmountLimitLoadFundException("Fund with id ${fundRequest.id} " +
                "for customerId ${fundRequest.customerId} has already reached max amount of daily money"
            )
        }

        val fundsInAWeekRange = fundRepository.getByFilter(
            filter = FundFilter(customerId = fundRequest.customerId.some()),
            fundInRange = FundInAWeekRange(fundRequest.time)
        )
        val sumInAWeek = computeSum(fundsInAWeekRange)
        ensure(sumInAWeek + fundRequest.loadAmount < loadFundProperty.weeklyMoneyLimit) {
            WeeklyMoneyAmountLoadFundException("Fund with id ${fundRequest.id} " +
                "for customerId ${fundRequest.customerId} has already reached max amount of weekly money"
            )
        }

        fundRepository.createFund(fundRequest).also {
            log.info { "Fund with id ${fundRequest.id} for customerId ${fundRequest.customerId} has been loaded" }
        }
    }

    private fun computeSum(fundsInADayRange: List<Fund>) =
        fundsInADayRange.map { it.loadAmount }
            .fold(BigDecimal.ZERO) { acc, value -> acc + value }
}
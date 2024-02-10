package com.kleonov.fundservice.service

import arrow.core.some
import com.kleonov.fundservice.datautil.FundLoadTestUtil.createLoadFundRequest
import com.kleonov.fundservice.datautil.createLoadFund
import com.kleonov.fundservice.properties.LoadFundProperty
import com.kleonov.fundservice.repository.FundRepository
import com.kleonov.fundservice.repository.filter.FundFilter
import com.kleonov.fundservice.repository.filter.FundInADayRange
import com.kleonov.fundservice.repository.filter.FundInAWeekRange
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
internal class FundServiceTest {

    @Mock
    private lateinit var loadFundProperty: LoadFundProperty
    @Mock
    private lateinit var fundRepository: FundRepository
    @InjectMocks
    private lateinit var fundService: FundService

    @Test
    fun `test loadFund loads fund successfully`() {
        val fundRequest = createLoadFundRequest().copy(loadAmount = BigDecimal("10.00"))
        val response = createLoadFund().copy(loadAmount = BigDecimal("10.00"))

        `when`(loadFundProperty.dailyTransactionsLimit).thenReturn(3)
        `when`(loadFundProperty.dailyMoneyLimit).thenReturn(BigDecimal("5000.00"))
        `when`(loadFundProperty.weeklyMoneyLimit).thenReturn(BigDecimal("20000"))
        `when`(fundRepository.getById(id = fundRequest.id, customerId = fundRequest.customerId)).thenReturn(null)
        `when`(
            fundRepository.getByFilter(
                filter = FundFilter(customerId = fundRequest.customerId.some()),
                fundInRange = FundInADayRange(fundRequest.time)
            )
        ).thenReturn(listOf(createLoadFund().copy(loadAmount = BigDecimal("1000.00"))))
        `when`(
            fundRepository.getByFilter(
                filter = FundFilter(customerId = fundRequest.customerId.some()),
                fundInRange = FundInAWeekRange(fundRequest.time)
            )
        ).thenReturn(listOf(createLoadFund().copy(loadAmount = BigDecimal("10000.00"))))
        `when`(fundRepository.createFund(fundRequest)).thenReturn(response)

        val result = fundService.loadFund(fundRequest)

        Assertions.assertTrue(result.isRight())
        result.onRight {
            Assertions.assertEquals(response, it)
        }
    }

    @Test
    fun `test loadFund returns IdempotenceLoadFundException`() {
        val fundRequest = createLoadFundRequest()

        `when`(fundRepository.getById(id = fundRequest.id, customerId = fundRequest.customerId)).thenReturn(
            createLoadFund()
        )

        val result = fundService.loadFund(fundRequest)

        Assertions.assertTrue(result.isLeft())
        result.onLeft { Assertions.assertTrue(it is IdempotenceLoadFundException) }
    }

    @Test
    fun `test loadFund returns DailyNumOfTransactionsLoadFundException`() {
        val fundRequest = createLoadFundRequest()

        `when`(loadFundProperty.dailyTransactionsLimit).thenReturn(3)
        `when`(fundRepository.getById(id = fundRequest.id, customerId = fundRequest.customerId)).thenReturn(null)
        `when`(
            fundRepository.getByFilter(
                filter = FundFilter(customerId = fundRequest.customerId.some()),
                fundInRange = FundInADayRange(fundRequest.time)
            )
        ).thenReturn(
            listOf(
                createLoadFund().copy(id = "1"),
                createLoadFund().copy(id = "2"),
                createLoadFund().copy(id = "3"),
            )
        )

        val result = fundService.loadFund(fundRequest)

        Assertions.assertTrue(result.isLeft())
        result.onLeft { Assertions.assertTrue(it is DailyNumOfTransactionsLoadFundException) }
    }

    @Test
    fun `test loadFund returns DailyMoneyAmountLimitLoadFundException`() {
        val fundRequest = createLoadFundRequest().copy(loadAmount = BigDecimal("0.01"))

        `when`(loadFundProperty.dailyTransactionsLimit).thenReturn(3)
        `when`(loadFundProperty.dailyMoneyLimit).thenReturn(BigDecimal("5000.00"))
        `when`(fundRepository.getById(id = fundRequest.id, customerId = fundRequest.customerId)).thenReturn(null)
        `when`(
            fundRepository.getByFilter(
                filter = FundFilter(customerId = fundRequest.customerId.some()),
                fundInRange = FundInADayRange(fundRequest.time)
            )
        ).thenReturn(listOf(createLoadFund().copy(loadAmount = BigDecimal("5000.00"))))

        val result = fundService.loadFund(fundRequest)

        Assertions.assertTrue(result.isLeft())
        result.onLeft { Assertions.assertTrue(it is DailyMoneyAmountLimitLoadFundException) }
    }

    @Test
    fun `test loadFund returns WeeklyMoneyAmountLoadFundException`() {
        val fundRequest = createLoadFundRequest().copy(loadAmount = BigDecimal("0.01"))

        `when`(loadFundProperty.dailyTransactionsLimit).thenReturn(3)
        `when`(loadFundProperty.dailyMoneyLimit).thenReturn(BigDecimal("5000.00"))
        `when`(loadFundProperty.weeklyMoneyLimit).thenReturn(BigDecimal("20000"))
        `when`(fundRepository.getById(id = fundRequest.id, customerId = fundRequest.customerId)).thenReturn(null)
        `when`(
            fundRepository.getByFilter(
                filter = FundFilter(customerId = fundRequest.customerId.some()),
                fundInRange = FundInADayRange(fundRequest.time)
            )
        ).thenReturn(listOf(createLoadFund().copy(loadAmount = BigDecimal("1000.00"))))
        `when`(
            fundRepository.getByFilter(
                filter = FundFilter(customerId = fundRequest.customerId.some()),
                fundInRange = FundInAWeekRange(fundRequest.time)
            )
        ).thenReturn(listOf(createLoadFund().copy(loadAmount = BigDecimal("20000.00"))))

        val result = fundService.loadFund(fundRequest)

        Assertions.assertTrue(result.isLeft())
        result.onLeft { Assertions.assertTrue(it is WeeklyMoneyAmountLoadFundException) }
    }
}
package com.kleonov.fundservice.port.eventlistener

import arrow.core.left
import arrow.core.right
import com.fasterxml.jackson.databind.ObjectMapper
import com.kleonov.fundservice.adapter.SystemFileAdapter
import com.kleonov.fundservice.datautil.*
import com.kleonov.fundservice.datautil.FundLoadTestUtil.createLoadAcceptedFundResponse
import com.kleonov.fundservice.datautil.FundLoadTestUtil.createLoadDeclinedFundResponse
import com.kleonov.fundservice.datautil.FundLoadTestUtil.createLoadFundRequest
import com.kleonov.fundservice.datautil.FundLoadTestUtil.inputLoadFundJson
import com.kleonov.fundservice.datautil.FundLoadTestUtil.outputAcceptedFundJson
import com.kleonov.fundservice.datautil.FundLoadTestUtil.outputDeclinedFundJson
import com.kleonov.fundservice.model.FundRequest
import com.kleonov.fundservice.service.DailyNumOfTransactionsLoadFundException
import com.kleonov.fundservice.service.FundService
import com.kleonov.fundservice.service.IdempotenceLoadFundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ApplicationEventListenerTest {

    @Mock
    private lateinit var systemFileAdapter: SystemFileAdapter
    @Mock
    private lateinit var fundService: FundService
    @Mock
    private lateinit var objectMapper: ObjectMapper
    @InjectMocks
    private lateinit var applicationEventListener: ApplicationEventListener

    @Test
    fun `test consumePayload passes request to fund loads and writes response to file successfully`() {
        val inputJson = inputLoadFundJson
        val request = createLoadFundRequest()
        val fund = createLoadFund()
        val response = createLoadAcceptedFundResponse()
        val outputJson = outputAcceptedFundJson

        `when`(systemFileAdapter.readFileLines("input.txt")).thenReturn(listOf(inputJson))
        `when`(objectMapper.readValue(inputJson, FundRequest::class.java)).thenReturn(request)
        `when`(fundService.loadFund(request)).thenReturn(fund.right())
        `when`(objectMapper.writeValueAsString(response)).thenReturn(outputJson)

        applicationEventListener.consumePayload()

        verify(systemFileAdapter).writeLinesToFile(
            lines = listOf(outputJson),
            fileName = "output.txt"
        )
    }

    @Test
    fun `test consumePayload passes request to fund loads returns validation error and writes response to file`() {
        val inputJson = inputLoadFundJson
        val request = createLoadFundRequest()
        val response = createLoadDeclinedFundResponse()
        val outputJson = outputDeclinedFundJson

        `when`(systemFileAdapter.readFileLines("input.txt")).thenReturn(listOf(inputJson))
        `when`(objectMapper.readValue(inputJson, FundRequest::class.java)).thenReturn(request)
        `when`(fundService.loadFund(request)).thenReturn(DailyNumOfTransactionsLoadFundException("").left())
        `when`(objectMapper.writeValueAsString(response)).thenReturn(outputJson)

        applicationEventListener.consumePayload()

        verify(systemFileAdapter).writeLinesToFile(
            lines = listOf(outputJson),
            fileName = "output.txt"
        )
    }

    @Test
    fun `test consumePayload passes request to fund loads does not write response due to Idempotence error`() {
        val inputJson = inputLoadFundJson
        val request = createLoadFundRequest()

        `when`(systemFileAdapter.readFileLines("input.txt")).thenReturn(listOf(inputJson))
        `when`(objectMapper.readValue(inputJson, FundRequest::class.java)).thenReturn(request)
        `when`(fundService.loadFund(request)).thenReturn(IdempotenceLoadFundException("").left())

        applicationEventListener.consumePayload()

        verify(systemFileAdapter, never()).writeLinesToFile(anyList(), anyString())
    }
}
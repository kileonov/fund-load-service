package com.kleonov.fundservice.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import java.math.BigDecimal

@ConfigurationProperties(prefix = "app.fund.load")
data class LoadFundProperty @ConstructorBinding constructor(
    val dailyTransactionsLimit: Int,
    val dailyMoneyLimit: BigDecimal,
    val weeklyMoneyLimit: BigDecimal,
)
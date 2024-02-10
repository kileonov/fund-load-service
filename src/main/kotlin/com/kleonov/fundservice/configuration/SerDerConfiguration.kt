package com.kleonov.fundservice.configuration

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal

@Configuration
class SerDerConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper {
        return jacksonMapperBuilder()
            .addModule(JavaTimeModule())
            .build()
    }
}

class CurrencyBigDecimalJsonDeserializer : JsonDeserializer<BigDecimal>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): BigDecimal {
        val tree = p.codec.readTree<JsonNode>(p)
        return BigDecimal(tree.asText().substring(1))
    }
}
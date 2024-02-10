package com.kleonov.fundservice

import com.kleonov.fundservice.port.eventlistener.ApplicationEventListener
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.util.concurrent.TimeUnit

@SpringBootTest
class FundserviceApplicationTests {

	@Autowired
	lateinit var applicationEventListener: ApplicationEventListener

	@Test
	fun `test processes input should compute output similar to provided`() {
		applicationEventListener.latch.await(10, TimeUnit.SECONDS)

		val expectedOutputFile = ClassPathResource("output.txt").file
		val actualOutputFile = File("output.txt")

		Assertions.assertEquals(
			expectedOutputFile.readText(),
			actualOutputFile.readText(),
		)
	}
}

package com.vinotech.sommelier_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "CLERK_ISSUER_URI=https://clerk.mock.test")
class SommelierApiApplicationTests {

	@Test
	void contextLoads() {
	}

}

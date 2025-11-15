package com.spring.chat.application.websocketwithdragonflydb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


//During tests, do NOT use application.properties.
//Instead load application-test.properties.
@ActiveProfiles("test")
@SpringBootTest
class WebSocketWithDragonFlyDbApplicationTests {

	@Test
	void contextLoads() {
	}

}

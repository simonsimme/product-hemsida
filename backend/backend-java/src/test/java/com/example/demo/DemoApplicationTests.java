package com.example.demo;

import com.example.demo.config.TestDatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestDatabaseConfig.class)
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}

package com.example.demo;

import com.example.demo.security.*;
import com.example.demo.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.liquibase.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
class DemoApplicationTests {
	@MockBean
	private UserService userService;
	@MockBean
	private TaskService taskService;
	@MockBean
	private CommentService commentService;
	@MockBean
	private NotificationService notificationService;
	@Test
	void contextLoads() {
	}

}

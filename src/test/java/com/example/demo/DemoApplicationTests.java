package com.example.demo;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dao.ScheduleRepository;
import com.example.demo.entity.Schedule;

@SpringBootTest
class DemoApplicationTests {
	
	@Autowired
	ScheduleRepository scheduleRepository;

	@Test
	void contextLoads() {
		
	}

}

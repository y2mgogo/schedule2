package com.example.demo;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dao.ScheduleRepository;
import com.example.demo.entity.Schedule;

@SpringBootTest
public class ScheduleTest {
	@Autowired
    ScheduleRepository scheduleRepository;
 
    @Test
    public void InsertDummies() {
 
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Schedule schedule = Schedule.builder().title("sample.."+i).build();
            //Create!
            scheduleRepository.save(schedule);
        });
    }
}

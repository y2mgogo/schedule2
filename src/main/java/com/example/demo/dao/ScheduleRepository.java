package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Schedule;

import jakarta.transaction.Transactional;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
	@Transactional
	@Modifying(clearAutomatically = true)
    @Query(value = "UPDATE schedule_table_3 p SET p.title = :title, p.contents = :contents WHERE p.id = :id", nativeQuery = true)
    int updateTitle(@Param(value="title") String title, @Param(value="contents") String contents, @Param(value="id") int id);
	
	@Transactional
	@Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM schedule_table_3 p WHERE p.id = :id", nativeQuery = true)
    int deleteTitle(@Param(value="id") int id);

}

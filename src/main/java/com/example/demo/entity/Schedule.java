package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule_table_3")
@Entity
public class Schedule {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //MySQL의 AUTO_INCREMENT를 사용
    private Integer id;
 
    @Column(length = 20, nullable = true)
    private String title;
    
    @Column(length = 200, nullable = true)
    private String contents;
    
    @Column(length = 8, nullable = true)
    private String schedule_date;
    
    @Column(length = 8, nullable = true)
    private String reg_date;
}

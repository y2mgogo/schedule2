package com.example.demo.sample.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.ScheduleRepository;
import com.example.demo.entity.Schedule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SampleController {
	
	private final ScheduleRepository scheduleRepository2;
	
	//@Autowired
	//ScheduleRepository scheduleRepository;
	
	//조회
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String index() {
		System.out.println("인덱스 페이지 호출");
		return "index";
	}
	//저장, 수정
	@RequestMapping(value="/schedule/save.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> param) {
		System.out.println("############## save로직 호출");
		String title = (String) param.get("title");
		String content = (String) param.get("content");
		String date = (String) param.get("date");
		String schedule_date = (String) param.get("schedule_date");
		String schedule_id = (String) param.get("schedule_id");
		
		System.out.println("############## title: " + title);
		System.out.println("############## content: " + content);
		
		HashMap<String, Object> map = new HashMap<>();
		
		//오늘날짜
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String reg_date = sdf.format(c1.getTime());
        List<Schedule> scheduleList = new ArrayList<>();
        if ( schedule_id.equals(null) || schedule_id.equals("") ) {
        	
        	Schedule schedule = Schedule.builder()
        			.title(title)
        			.contents(content)
        			.schedule_date(schedule_date)
        			.reg_date(reg_date).build();
        	Schedule result = scheduleRepository2.save(schedule);
        	if ( !result.equals(null) ) {
        		scheduleList = scheduleRepository2.findAll();
        	}
        	map.put("result", result);
     
        } else {

        	int updateResult = scheduleRepository2.updateTitle(title, content, Integer.parseInt(schedule_id));
        	
        	if ( updateResult > 0 ) {
        		scheduleList = scheduleRepository2.findAll();
        	}
        	map.put("result", updateResult);
        }
    	map.put("scheduleList", scheduleList);
		
		System.out.println("############## map: " + map);	
		return map;
	}
	//삭제
	@RequestMapping(value="/schedule/delete.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@RequestBody Map<String, Object> param) {
		System.out.println("############## delete로직 호출");
		
		String schedule_id = (String) param.get("schedule_id");
		
		System.out.println("############## schedule_id: " + schedule_id);
		
		HashMap<String, Object> map = new HashMap<>();
		List<Schedule> scheduleList = new ArrayList<>();
		try {
			int result = scheduleRepository2.deleteTitle(Integer.parseInt(schedule_id));
			if ( result > 0 ) {
				scheduleList = scheduleRepository2.findAll();
			}
			map.put("result", result);
			map.put("scheduleList", scheduleList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("############## map: " + map);	
		return map;
	}
	
	//리스트조회
	@RequestMapping(value="/schedule/selectList.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectList(@RequestBody Map<String, Object> param) {
		System.out.println("############## selectList로직 호출");
		
		HashMap<String, Object> map = new HashMap<>();
		List<Schedule> scheduleList = new ArrayList<>();
		try {
			scheduleList = scheduleRepository2.findAll();
			map.put("scheduleList", scheduleList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("############## map: " + map);	
		return map;
	}
}
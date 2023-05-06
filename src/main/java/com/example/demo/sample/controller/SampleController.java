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
	
	@Autowired
	ScheduleRepository scheduleRepository;
	
	//조회
	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView index() {
		System.out.println("인덱스 페이지 호출");
		
		ModelAndView mav = new ModelAndView();
		List<Map<String, Object>> result = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<>();
		
		mav.setViewName("index");
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		
		et.begin();
		
		try{
           // MemberInfo 리스트 조회 방법
           // JPQL을 이용함.
           // 쿼리를 보면, 우리가 알고있는 일반적인 쿼리가 아닌 것을 알 수 있음.
           // 멤버인포를 다 가져오라는 뜻.
           List<Schedule> scheduleList = (List<Schedule>) em.createQuery("select m from Schedule as m", Schedule.class).getResultList();
           //int cnt = 0;
           for (Schedule schedule : scheduleList) {
               System.out.println("아이디 : " + schedule.getId() + " / 제목 : " + schedule.getTitle());
               resultMap.put("id", schedule.getId());
               resultMap.put("title", schedule.getTitle());
               resultMap.put("contents", schedule.getContents());
               resultMap.put("reg_date", schedule.getReg_date());
               resultMap.put("schedule_date", schedule.getSchedule_date());
               result.add(resultMap);
               resultMap = new HashMap<>();
           }
           // 정상작동 시, 커밋
           et.commit();

       // 오류 발생 시, 롤백
       } catch(Exception e){
    	   e.printStackTrace();
           et.rollback();
       // 성공 / 오류 상관없이 마지막에 거치는 영역
       } finally {
           // EntityManager를 닫아준다.
           em.close();
       }
		emf.close();
		
		//List<Schedule> result = scheduleRepository.findAll();
		System.out.println("########## result: " + result);
		mav.addObject("list", result);
		return mav;
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
        	Schedule result = scheduleRepository.save(schedule);
        	if ( !result.equals(null) ) {
        		scheduleList = scheduleRepository.findAll();
        	}
        	map.put("result", result);
     
        } else {

        	int updateResult = scheduleRepository.updateTitle(title, content, Integer.parseInt(schedule_id));
        	
        	if ( updateResult > 0 ) {
        		scheduleList = scheduleRepository.findAll();
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
			int result = scheduleRepository.deleteTitle(Integer.parseInt(schedule_id));
			if ( result > 0 ) {
				scheduleList = scheduleRepository.findAll();
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
			scheduleList = scheduleRepository.findAll();
			map.put("scheduleList", scheduleList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("############## map: " + map);	
		return map;
	}
}
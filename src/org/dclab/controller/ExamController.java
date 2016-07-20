package org.dclab.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dclab.model.ExamBean;
import org.dclab.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam")
public class ExamController {

	@Autowired
	private ExamService examService;
	public void setExamService(ExamService service){
		examService=service;
	}
	
	@RequestMapping("/start")
	public Object showDefaultExamPage(@RequestParam(value = "token") UUID token,
			@RequestParam(value="typeId")int typeId){
		ExamBean exambean=examService.getExambeanByToken(token);
		long startTime=System.currentTimeMillis();
		exambean.setStartTime(startTime);//将考生开始考试的时间写入ExamBean
		return examService.getFirstTopic(exambean, typeId);
	}
	
	@RequestMapping("/nextTopic")
	public Object getNextTopic(@RequestParam(value = "token") UUID token,
			@RequestParam(value="typeId")int typeId,
			@RequestParam(value="id")int id,
			@RequestParam(value="choiceId")List<Integer> choiceId,
			@RequestParam(value="ifCheck")boolean ifCheck){
		ExamBean exambean=examService.getExambeanByToken(token);
		return examService.getNextTopic(exambean, typeId, id, choiceId, ifCheck);
	}
	
	@RequestMapping("/lastTopic")
	public Object getLastTopic(@RequestParam(value = "token") UUID token,
			@RequestParam(value="typeId")int typeId,
			@RequestParam(value="id")int id,
			@RequestParam(value="choiceId")List<Integer> choiceId,
			@RequestParam(value="ifCheck")boolean ifCheck){
		ExamBean exambean=examService.getExambeanByToken(token);
		return examService.getLastTopic(exambean, typeId, id, choiceId, ifCheck);
	}
	
	@RequestMapping("/check")
	public Map<String, List<Integer>> showCheckPage(@RequestParam(value="token")UUID token,
			@RequestParam(value="typeId")int typeId){
		ExamBean exambean=examService.getExambeanByToken(token);
		List checkList=examService.getCheckList(exambean, typeId);
		List answeredList=examService.getAnsweredList(exambean, typeId);
		Map<String,List<Integer>> map=new HashMap<String, List<Integer>>();
		map.put("checkList", checkList);
		map.put("answeredList", answeredList);
		return map;
	}
	
	@RequestMapping("/toTopic")
	public Object toTopic(@RequestParam(value="token")UUID token,
			@RequestParam(value="typeId")int typeId,
			@RequestParam(value="id")int id){
		ExamBean exambean=examService.getExambeanByToken(token);
		return examService.getTopic(exambean, typeId, id);
	}


}

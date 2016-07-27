package org.dclab.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dclab.User;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.SuperBean;
import org.dclab.model.SupervisorOperator;
import org.dclab.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alvis
 *监考相关的controller
 */

@RestController
@RequestMapping("/supervise")
public class SuperviseController {
	@Autowired
	private SupervisorService supervisorService;
	public void setSupervisorService(SupervisorService service){
		supervisorService=service;
	}
	
	@RequestMapping("/start")
	public Map<String, Object> showDefaultPage(@RequestParam(value="Uid")int Uid){
		UUID token=SupervisorOperator.idTokenMap.get(Uid);
		SuperBean superbean=SupervisorOperator.tokenSuperMap.get(token);
		List<User> userList=supervisorService.getUserList(superbean);

		List<Integer> loginedList=supervisorService.getLoginedList(superbean);
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("userList", userList);
		map.put("loginedList", loginedList);
		map.put("roomId", superbean.getRoomId());
		map.put("token", token);
		
		return map;
	}
	
	@RequestMapping("/refresh")
	public List<Integer> refreshLogin(@RequestParam(value="token")UUID token){
		
		SuperBean superbean=SupervisorOperator.tokenSuperMap.get(token);
		return supervisorService.getLoginedList(superbean);
	}
	

	@RequestMapping("/operation")
	public Object individualOperation(@RequestParam(value="token")UUID token,
			@RequestParam(value="seatNum")int seatNum){
		SuperBean superbean=SupervisorOperator.tokenSuperMap.get(token);
		return supervisorService.getUserList(superbean).get(seatNum-1);
	}
	
	@RequestMapping("/delay")
	public void delay(@RequestParam(value="token")UUID token,
			@RequestParam(value="Uid")int Uid){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{ExamBean exambean=ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(Uid));
		supervisorService.delay(exambean);
		}
		else{
			System.out.println("没有对应的token");
		}
	}
	
	@RequestMapping("/restart")
	public void restart(@RequestParam(value="token")UUID token,
			@RequestParam(value="Uid")int Uid){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null){
			ExamBean exambean=ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(Uid));
			supervisorService.returnToExam(exambean);
		}
		else{System.out.println("没有对应的token");}
	}
	
	@RequestMapping("/manualAssign")
	public void manualAssign(@RequestParam(value="token")UUID token,
			@RequestParam(value="Uid")int Uid){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{
			ExamBean exambean=ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(Uid));
			supervisorService.manualAssign(exambean);
		}
		else{
			System.out.println("没有对应的token");
		}
	}
	
	@RequestMapping("/allowStart")
	public void allowStart(@RequestParam(value="token")UUID token,
			@RequestParam(value="Uid")int Uid){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{
			ExamBean exambean=ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(Uid));
			supervisorService.allowStart(exambean);
		}
		else
			System.out.println("没有对应的token");
	}
	
	@RequestMapping("/allowStop")
	public void allowStop(@RequestParam(value="token")UUID token,
			@RequestParam(value="Uid")int Uid){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{
			ExamBean exambean=ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(Uid));
			supervisorService.allowTerminate(exambean);
		}
		else
			System.out.println("没有对应的token");
	}
	
	@RequestMapping("/deleteExam")
	public void deleteExam(@RequestParam(value="token")UUID token,
			@RequestParam(value="Uid")int Uid){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{
			ExamBean exambean=ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(Uid));
			supervisorService.deleteExamInfo(exambean);
		}
		else
			System.out.println("没有对应的token");
	}
	
}


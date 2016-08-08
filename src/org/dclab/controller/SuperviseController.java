package org.dclab.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dclab.User;
import org.dclab.model.CandidateBean;
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
	
	@RequestMapping("/refresh")
	public Collection<CandidateBean> refreshLogin(@RequestParam(value="token")UUID token){
		
		SuperBean superbean=SupervisorOperator.tokenSuperMap.get(token);
		return supervisorService.getInfo(superbean);
	}
	
	@RequestMapping("/seatChange")
	public List<Integer> returnFreeList(@RequestParam(value="token")UUID token){
		SuperBean superBean=SupervisorOperator.tokenSuperMap.get(token);
		return superBean.getFreeSeatList();
	}
	
	@RequestMapping("/seatChange/confirm")
	public boolean seatChange(@RequestParam(value="token")UUID token,
			@RequestParam(value="Uid")int Uid,@RequestParam(value="seatNum")int seatNum){
		SuperBean superBean=SupervisorOperator.tokenSuperMap.get(token);
		return supervisorService.seatChange(superBean, Uid, seatNum);
	}

/*	@RequestMapping("/operation")
	public Object individualOperation(@RequestParam(value="token")UUID token,
			@RequestParam(value="seatNum")int seatNum){
		SuperBean superbean=SupervisorOperator.tokenSuperMap.get(token);
		return supervisorService.getUserList(superbean).get(seatNum-1);
	}*/
	
	@RequestMapping("/delay")
	public void delay(@RequestParam(value="token")UUID token,
			@RequestParam(value="uidList")List<Integer> uidList,
			@RequestParam(value="delayTime")int delayTime){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{
		    supervisorService.delay(uidList,delayTime);
		}
		else{
			System.out.println("没有对应的token");
		}
	}
	
	@RequestMapping("/restart")
	public void restart(@RequestParam(value="token")UUID token,
			@RequestParam(value="uidList")List<Integer> uidList){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null){
			supervisorService.returnToExam(uidList);
		}
		else{System.out.println("没有对应的token");}
	}
	
	@RequestMapping("/manualAssign")
	public void manualAssign(@RequestParam(value="token")UUID token,
			@RequestParam(value="uidList")List<Integer> uidList){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{
			SuperBean superBean=SupervisorOperator.tokenSuperMap.get(token);
			supervisorService.forceTerminate(superBean, uidList);
		}
		else{
			System.out.println("没有对应的token");
		}
	}
	
	@RequestMapping("/allowStart")
	public void allowStart(@RequestParam(value="token")UUID token,
			@RequestParam(value="uidList")List<Integer> uidList){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{
			supervisorService.allowStart(uidList);
		}
		else
			System.out.println("没有对应的token");
	}
	
	@RequestMapping("/allowStop")
	public void allowStop(@RequestParam(value="token")UUID token,
			@RequestParam(value="uidList")List<Integer> uidList){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{
			supervisorService.allowTerminate(uidList);
		}
		else
			System.out.println("没有对应的token");
	}
	
	@RequestMapping("/deleteExam")
	public void deleteExam(@RequestParam(value="token")UUID token,
			@RequestParam(value="uidList")List<Integer> uidList){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null)
		{
			SuperBean superBean=SupervisorOperator.tokenSuperMap.get(token);
			supervisorService.deleteExamInfo(superBean,uidList);
		}
		else
			System.out.println("没有对应的token");
	}
	
}


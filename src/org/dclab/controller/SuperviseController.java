package org.dclab.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dclab.User;
import org.dclab.mapping.SessionMapperI;
import org.dclab.model.CandidateBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.SuperBean;
import org.dclab.model.SuperRequest;
import org.dclab.model.SuperRespond;
import org.dclab.model.SupervisorOperator;
import org.dclab.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.web.bind.annotation.RequestMethod.*;

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
	
	@Autowired
	private SessionMapperI sessionMapperI;
	public void setSessionMapperI(SessionMapperI sessionMapperI) {
		this.sessionMapperI = sessionMapperI;
	}
	
	@RequestMapping("/Refresh")
	public Collection<CandidateBean> refreshLogin(@RequestParam(value="token")UUID token){
		
		SuperBean superbean=SupervisorOperator.tokenSuperMap.get(token);
		return supervisorService.getInfo(superbean);
	}
	
	@RequestMapping(value="/seatChange",method=GET)
	public List<Integer> returnFreeList(@RequestParam(value="token")UUID token){
		SuperBean superBean=SupervisorOperator.tokenSuperMap.get(token);
		return superBean.getFreeSeatList();
	}
	
	@RequestMapping(value="/seatChange",method=POST)
	public SuperRespond seatChange(@RequestBody Map<String, Map> map){
		UUID token=UUID.fromString((String) map.get("params").get("token"));
		SuperBean superBean=SupervisorOperator.tokenSuperMap.get(token);
		return supervisorService.seatChange(superBean, (int)map.get("params").get("uid"),(int)map.get("params").get("seatNum"));
	}
	@RequestMapping("/delay")
	public SuperRespond delay(SuperRequest superRequest){
		if(SupervisorOperator.tokenSuperMap.get(superRequest.getToken())!=null)
		{
		   if(superRequest.getUidList()==null)
			   return new SuperRespond(false, "请选择至少一名考生");
		   return supervisorService.delay(superRequest.getUidList(),superRequest.getDelayTime());
		}
		else
			return new SuperRespond(false,"错误的监考token");
	}
	
	@RequestMapping("/restart")
	public SuperRespond restart(SuperRequest superRequest){
		if(SupervisorOperator.tokenSuperMap.get(superRequest.getToken())!=null){
			if(superRequest.getUidList()==null)
				return new SuperRespond(false, "请选择至少一名考生");
			return supervisorService.returnToExam(superRequest.getUidList());
		}
		else{
			return new SuperRespond(false,"错误的监考token");
		}
	}
	
	@RequestMapping("/release")//释放座位
	public SuperRespond seatRelease(SuperRequest superRequest){
		if(SupervisorOperator.tokenSuperMap.get(superRequest.getToken())!=null){
			if(superRequest.getUidList()==null)
				return new SuperRespond(false, "请选择至少一名考生");
			SuperBean superBean = SupervisorOperator.tokenSuperMap.get(superRequest.getToken());
			return supervisorService.releaseSeat(superBean, superRequest.getUidList());
		}
		else {
			return new SuperRespond(false, "错误的监考token");
		}
	}
	
	@RequestMapping("/manualAssign")
	public SuperRespond manualAssign(SuperRequest superRequest){
		if(SupervisorOperator.tokenSuperMap.get(superRequest.getToken())!=null)
		{
			if(superRequest.getUidList()==null)
				return new SuperRespond(false, "请选择至少一名考生");
			SuperBean superBean=SupervisorOperator.tokenSuperMap.get(superRequest.getToken());
			return supervisorService.forceTerminate(superBean, superRequest.getUidList());
		}
		else{
			return new SuperRespond(false,"错误的监考token");
		}
	}
	
	@RequestMapping("/forceStop")
	public SuperRespond forceStop(SuperRequest superRequest){
		if(SupervisorOperator.tokenSuperMap.get(superRequest.getToken())!=null)
		{
			if(superRequest.getUidList()==null)
				return new SuperRespond(false, "请选择至少一名考生");
			SuperBean superBean=SupervisorOperator.tokenSuperMap.get(superRequest.getToken());
			return supervisorService.forceTerminate(superBean, superRequest.getUidList());
		}
		else{
			return new SuperRespond(false,"错误的监考token");
		}
	}
	
	@RequestMapping("/allowStart")
	public SuperRespond allowStart(SuperRequest superRequest){
		if(SupervisorOperator.tokenSuperMap.get(superRequest.getToken())!=null)
		{
			if(superRequest.getUidList()==null)
				return new SuperRespond(false, "请选择至少一名考生");
			return supervisorService.allowStart(superRequest.getUidList());
		}
		else
			return new SuperRespond(false,"错误的监考token");
	}
	
	@RequestMapping("/allowStop")
	public SuperRespond allowStop(SuperRequest superRequest){
		if(SupervisorOperator.tokenSuperMap.get(superRequest.getToken())!=null)
		{
			if(superRequest.getUidList()==null)
				return new SuperRespond(false, "请选择至少一名考生");
			return supervisorService.allowTerminate(superRequest.getUidList());
		}
		else
			return new SuperRespond(false,"错误的监考token");
	}
	
	@RequestMapping("/deleteExam")
	public SuperRespond deleteExam(SuperRequest superRequest){
		if(SupervisorOperator.tokenSuperMap.get(superRequest.getToken())!=null)
		{
			if(superRequest.getUidList()==null)
				return new SuperRespond(false, "请选择至少一名考生");
			SuperBean superBean=SupervisorOperator.tokenSuperMap.get(superRequest.getToken());
			return supervisorService.deleteExamInfo(superBean,superRequest.getUidList());
		}
		else
			return new SuperRespond(false,"错误的监考token");
	}
	
	@RequestMapping("/endExam")
	public SuperRespond endExam(@RequestParam UUID token, @RequestParam int id ){
		if(SupervisorOperator.tokenSuperMap.get(token)!=null){
			if(sessionMapperI.cancelStartFlag(id)==0)
				return new SuperRespond(false, "错误的考场id");
			else
				return new SuperRespond(true);
		}
		else
			return new SuperRespond(false,"错误的监考token");
	}
	
}


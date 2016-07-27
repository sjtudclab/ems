package org.dclab.controller;

import java.util.List;
import java.util.UUID;

import org.dclab.model.CheckBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.RequestBean;
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
	public Object showDefaultExamPage(RequestBean request){
		if(examService.getExambeanByToken(request.getToken())==null)
		{
			String respond="错误的token";
			return respond;
		}
/*		else if(examService.getExambeanByToken(token).isFinished()==true||(examService.getExambeanByToken(token).isAllowStart()==false&&
				examService.getExambeanByToken(token).getEXAM_TIME()+examService.getExambeanByToken(token).getDuration()*1000>System.currentTimeMillis()))
		{
			String respond="考试已结束或已经交卷";
			return respond;
		}*/
		ExamBean exambean=examService.getExambeanByToken(request.getToken());
		if(exambean.isFinished()==true){
			String respond="已经交卷了";
			return respond;
		}
		else if(exambean.getStartTime()==0)
		{
			long startTime=System.currentTimeMillis();
			exambean.setStartTime(startTime);//将考生开始考试的时间写入ExamBean
			return examService.getFirstTopic(exambean, request.getTypeId());
		}
		else if(System.currentTimeMillis()-exambean.getStartTime()<exambean.getDuration()*1000)
		{
			return examService.getFirstTopic(exambean, request.getTypeId());
		}
		else
		{
			String respond="考试时间结束了";
			return respond;
		}
	}
	
	@RequestMapping("/nextTopic")
	public Object getNextTopic(RequestBean request){

		ExamBean exambean=examService.getExambeanByToken(request.getToken());
		if(request.getChoiceIdMap()!=null)
		{
			return examService.getNextTopic(exambean, request.getTypeId(), request.getId(), request.getChoiceIdMap(), request.isIfCheck());
		}
		else
			return  examService.getNextTopic(exambean, request.getTypeId(), request.getId(), request.getChoiceIdList(), request.isIfCheck());	
	}
	
	@RequestMapping("/lastTopic")
	public Object getLastTopic(RequestBean requset){
		
		ExamBean exambean=examService.getExambeanByToken(requset.getToken());
		if(requset.getChoiceIdMap()!=null){
			return examService.getLastTopic(exambean, requset.getTypeId(), requset.getId(), requset.getChoiceIdMap(), requset.isIfCheck());
		}
		else
			return examService.getLastTopic(exambean, requset.getTypeId(), requset.getId(), requset.getChoiceIdList(), requset.isIfCheck());
	}
	
	@RequestMapping("/check")
	public List<CheckBean> showCheckPage(@RequestParam(value="token")UUID token,
			@RequestParam(value="typeId")Integer typeId){
		ExamBean exambean=examService.getExambeanByToken(token);
		return examService.getCheckList(exambean, typeId);
	}
	
	@RequestMapping("/toTopic")
	public Object toTopic(@RequestParam(value="token")UUID token,
			@RequestParam(value="typeId")Integer typeId,
			@RequestParam(value="id")Integer id){
		ExamBean exambean=examService.getExambeanByToken(token);
		id=id-1;
		return examService.getTopic(exambean, typeId, id);
	}
	
	@RequestMapping("/handExam")
	public boolean handin(@RequestParam(value="token")UUID token){
		if(ExamOperator.tokenExamMap.get(token)!=null){
			ExamBean exambean=examService.getExambeanByToken(token);
			exambean.setFinished(true);
			return true;
		}
		else
			return false;
	}
	
	@RequestMapping("/getTime")
	public int getTime(@RequestParam(value="token")UUID token){
		ExamBean exambean=examService.getExambeanByToken(token);
		return examService.getTime(exambean);
	}


}


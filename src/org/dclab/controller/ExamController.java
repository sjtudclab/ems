package org.dclab.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dclab.model.CheckBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.RequestBean;
import org.dclab.model.SuperRespond;
import org.dclab.service.ExamService;
import org.dclab.service.GradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam")
public class ExamController {

	@Autowired
	private ExamService examService;
	private GradingService gradingService;
	public void setExamService(ExamService service){
		examService=service;
	}
	@Autowired
	public void setGradingService(GradingService gradingService) {
		this.gradingService = gradingService;
	}


	@RequestMapping("/start")
	public Object showDefaultExamPage(RequestBean request){

		ExamBean exambean=examService.getExambeanByToken(request.getToken());
		if(exambean==null||exambean.isFinished()==true)
			return new SuperRespond(false, "考试已经结束");

		if(exambean.getStartTime()==0)
		{
			long startTime=System.currentTimeMillis();
			exambean.setStartTime(startTime);//将考生开始考试的时间写入ExamBean
			return examService.getFirstTopic(exambean, request.getTypeId());
		}
		else /*if(System.currentTimeMillis()-exambean.getStartTime()<exambean.getDuration()*1000)*/
		{
			return examService.getFirstTopic(exambean, request.getTypeId());
		}
/*		else
		{
			String respond="考试时间结束了";
			return respond;
		}*/
	}
	
	@RequestMapping("/getTopic")
	public Object getTopic(RequestBean request)//写入本题状态，返回请求的题目
	{
		ExamBean exambean=examService.getExambeanByToken(request.getToken());
		if(exambean==null||exambean.isFinished()==true)
			return new SuperRespond(false, "考试已经结束");
		
		int id=request.getId()-1;//list中保存的题是从0开始的
		int requestId=request.getRequestId()-1;
		
	
		switch (request.getTypeId()) {
		case 0:
			examService.storeTopic(exambean, request.getTypeId(), id, request.getChoiceId(), request.isIfCheck());
			break;
		case 1:
			System.out.println((id+1)+" "+request.getChoiceIdList());
			examService.storeTopic(exambean, request.getTypeId(), id, request.getChoiceIdList(), request.isIfCheck());
			
			break;
		case 3:
			Map<Integer, Integer> map=examService.stringTomap(request.getChoiceIdMap());
			examService.storeTopic(exambean, request.getTypeId(), id, map, request.isIfCheck());
			break;
		case 2:
			examService.storeTopic(exambean, request.getTypeId(), id, request.getChoiceId(), request.isIfCheck());
			break;
		case 4:
			examService.storeTopic(exambean, request.getTypeId(), id, request.getAnswer(), request.isIfCheck());
			break;
		default:
			System.out.println("controller getTopic 出错");
			break;
		}
		return examService.getTopic(exambean, request.getTypeId(), requestId);
	}
	@RequestMapping("/check")
	public Object showCheckPage(@RequestParam(value="token")UUID token,
			@RequestParam(value="typeId")Integer typeId){
		ExamBean exambean=examService.getExambeanByToken(token);
		
		if(exambean==null||exambean.isFinished()==true)
			return new SuperRespond(false, "考试已经结束");
		
		Map<String, Object> map=new HashMap<String, Object>();
		List<CheckBean> checkList=examService.getCheckList(exambean, typeId);
		map.put("checkList", checkList);
		System.out.println("传给前端的num "+exambean.getTopicNum());
		map.put("topicNum", exambean.getTopicNum());
		map.put("finishNum", exambean.getFinishTopic().size());
		return map;
	}
	
	@RequestMapping("/toTopic")
	public Object toTopic(@RequestParam(value="token")UUID token,
			@RequestParam(value="typeId")int typeId,
			@RequestParam(value="id")int id){
		ExamBean exambean=examService.getExambeanByToken(token);
		
		if(exambean==null||exambean.isFinished()==true)
			return new SuperRespond(false, "考试已经结束");
		
		return examService.getTopic(exambean, typeId, --id);
	}
	
	@RequestMapping("/handExam")
	public Object handin(@RequestParam(value="token")UUID token){
			ExamBean exambean=examService.getExambeanByToken(token);
			
			if(exambean==null)
				return 0;
			
			ExamOperator.persist(token);
			
			
			if(exambean.isFinished())//处理考生被老师强制终止之后的成绩返回
				return gradingService.gradePaper(exambean);
			
			if(exambean.isAllowTerminate()||
					( exambean.getEXAM_TIME()-(System.currentTimeMillis()-exambean.getStartTime())/1000  )<exambean.getEarliestSubmit())
			{
				exambean.setFinished(true);
				int mark = gradingService.gradePaper(exambean);
				return mark;
			}
			else
				return new SuperRespond(false, "还没到交卷时间");
				
	}
	
	@RequestMapping("/getTime")
	public int getTime(@RequestParam(value="token")UUID token){
		ExamBean exambean=examService.getExambeanByToken(token);
		
		if(exambean==null||exambean.isFinished()==true)
			return 0;
		
		return examService.getTime(exambean);
	}


}


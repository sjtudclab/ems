package org.dclab.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dclab.model.CheckBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.RequestBean;
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
		/*if(examService.getExambeanByToken(request.getToken())==null)
		{
			String respond="错误的token";
			return respond;
		}*/
/*		else if(examService.getExambeanByToken(token).isFinished()==true||(examService.getExambeanByToken(token).isAllowStart()==false&&
				examService.getExambeanByToken(token).getEXAM_TIME()+examService.getExambeanByToken(token).getDuration()*1000>System.currentTimeMillis()))
		{
			String respond="考试已结束或已经交卷";
			return respond;
		}*/
		ExamBean exambean=examService.getExambeanByToken(request.getToken());
		System.out.println(exambean.getSingleChoiceById(request.getId()).getImg());
		/*if(exambean.isFinished()==true){
			String respond="已经交卷了";
			return respond;
		}*/
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
		int id=request.getId()-1;
		int requestId=request.getRequestId()-1;
		
		
		switch (request.getTypeId()) {
		case 0:
			examService.storeTopic(exambean, request.getTypeId(), id, request.getChoiceId(), request.isIfCheck());
			break;
		case 1:
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
	public Map<String, Object> showCheckPage(@RequestParam(value="token")UUID token,
			@RequestParam(value="typeId")Integer typeId){
		ExamBean exambean=examService.getExambeanByToken(token);
		Map<String, Object> map=new HashMap<String, Object>();
		List<CheckBean> checkList=examService.getCheckList(exambean, typeId);
		map.put("checkList", checkList);
		map.put("topicNum", exambean.getTopicNum());
		map.put("finishNum", exambean.getFinishTopic().size());
		return map;
	}
	
	@RequestMapping("/toTopic")
	public Object toTopic(@RequestParam(value="token")UUID token,
			@RequestParam(value="typeId")int typeId,
			@RequestParam(value="id")int id){
		ExamBean exambean=examService.getExambeanByToken(token);
		return examService.getTopic(exambean, typeId, --id);
	}
	
	@RequestMapping("/handExam")
	public int handin(@RequestParam(value="token")UUID token){
			ExamBean exambean=examService.getExambeanByToken(token);
			exambean.setFinished(true);
			int mark = gradingService.gradePaper(exambean);
			System.out.println("mark: "+mark);
			return mark;
	}
	
	@RequestMapping("/getTime")
	public int getTime(@RequestParam(value="token")UUID token){
		ExamBean exambean=examService.getExambeanByToken(token);
		return examService.getTime(exambean);
	}


}


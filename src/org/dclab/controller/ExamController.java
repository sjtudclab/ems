package org.dclab.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.dclab.common.Constants;
import org.dclab.model.CheckBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.RequestBean;
import org.dclab.model.SuperRespond;
import org.dclab.service.ExamService;
import org.dclab.service.GradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
		case 5:
			examService.storeTopic(exambean, request.getTypeId(), id, request.getAnswerList(), request.isIfCheck());
			break;
		case 6:
			examService.storeTopic(exambean, request.getTypeId(), id, request.getFileName(), request.isIfCheck());
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
	
	@RequestMapping("/getPdf")
	public void getPdf(@RequestParam UUID token,@RequestParam int typeId,@RequestParam int id,HttpServletResponse response){
		ExamBean examBean = ExamOperator.tokenExamMap.get(token);
		if(examBean==null||examBean.isFinished()==true)
			System.err.println("考试已经结束");
		String name= new String();
		switch (typeId) {
		case 4:
			name = examBean.getShortAnswerById(--id).getPdf();
			break;
		case 5:
			name = examBean.getFillBlankById(--id).getPdf();
			break;
		case 6:
			name = examBean.getMachineTestById(--id).getPdf();
			break;
		default:
			System.err.println("getPdf错误的typeId");
			break;
		}
		
		String path = System.getProperty("project.root")+Constants.multiMediaDir;
		
		System.out.println("pad的path地址是："+path);
/*		String myfileName = typeId+"-"+id+".pdf";*/
/*		response.addHeader("Content-Disposition", "attachment;filename=" + myfileName ); */
		response.setHeader("Content-type", "application/pdf");
        
        try {
			// get your file as InputStream
			InputStream is = new FileInputStream(new File(
					path+name));
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@RequestMapping("/handExam")
	public Object handin(@RequestParam(value="token")UUID token){
			ExamBean exambean=examService.getExambeanByToken(token);
			
			if(exambean==null)
				return 0;
			
			
			if(exambean.isFinished())//处理考生被老师强制终止之后的成绩返回
				return gradingService.gradePaper(exambean);
			
			if(exambean.isAllowTerminate()||
					( exambean.getEXAM_TIME()-(System.currentTimeMillis()-exambean.getStartTime())/1000  )<exambean.getEarliestSubmit())
			{
				exambean.setFinished(true);
				int mark = gradingService.gradePaper(exambean);
				Thread thread = new persist(token,mark);
				thread.start();
				return mark;
			}
			else
				return new SuperRespond(false, "还没到交卷时间");
				
	}
	
	@PostMapping("/machineForm")
	public Map<String, String> machineFormUpload(@RequestParam("file") MultipartFile file) {
		String path=System.getProperty("project.root")+"/files/import/";
		Map<String, String> map = new HashMap<String, String>();
		try {
			FileOutputStream fos = new FileOutputStream(path + file.getOriginalFilename());
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			map.put("info", "上传失败");
			return map;
		}
		map.put("info", "上传成功");
		return map;

	}
	
	@RequestMapping("/getTime")
	public int getTime(@RequestParam(value="token")UUID token){
		ExamBean exambean=examService.getExambeanByToken(token);
		
		if(exambean==null||exambean.isFinished()==true)
			return 0;
		
		return examService.getTime(exambean);
	}


}

class persist extends Thread{//将考生答题情况写会数据库
	private UUID token ;
	private int mark;
	
	public persist(UUID token,int mark){
		this.token=token;
		this.mark=mark;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ExamOperator.persist(token,mark);
	}
	
	
}


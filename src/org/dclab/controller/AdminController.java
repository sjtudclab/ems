package org.dclab.controller;

import java.awt.print.Paper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.CanAnswerMapperI;
import org.dclab.mapping.ChoiceMapperI;
import org.dclab.mapping.MatchItemMapperI;
import org.dclab.mapping.PaperMapperI;
import org.dclab.mapping.SessionCanMapperI;
import org.dclab.mapping.SessionMapperI;
import org.dclab.mapping.TopicMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.AdminBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.Paper4PDF;
import org.dclab.model.RoomInfoBean;
import org.dclab.model.SessionBean;
import org.dclab.model.ShortAnswerBean;
import org.dclab.model.SingleChoiceBean;
import org.dclab.model.SubjectRow;
import org.dclab.model.SuperBean;
import org.dclab.model.SuperRespond;
import org.dclab.model.SupervisorOperator;
import org.dclab.model.Topic4PDF;
import org.dclab.service.AdminService;
import org.dclab.service.ExportService;
import org.dclab.service.ImportService;
import org.dclab.utils.ExcelExporter;
import org.dclab.utils.ExcelImporter;
import org.dclab.utils.MyBatisUtil;
import org.dclab.utils.PDFWriter;
import org.junit.runners.Parameterized.Parameters;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Import;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	@Autowired
	private ImportService importService;
	public void setImportService(ImportService importService) {
		this.importService = importService;
	}
	
	@Autowired
	private ExportService exportService;
	public void setExportService(ExportService exportService) {
		this.exportService = exportService;
	}
	
	@RequestMapping("/examClear")
	public Map<String, String> clearExam(@RequestParam UUID token){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		PaperMapperI paperMapperI = sqlSession.getMapper(PaperMapperI.class);
		TopicMapperI topicMapperI = sqlSession.getMapper(TopicMapperI.class);
		ChoiceMapperI choiceMapperI = sqlSession.getMapper(ChoiceMapperI.class);
		MatchItemMapperI matchItemMapperI = sqlSession.getMapper(MatchItemMapperI.class);
		
		Map<String, String> map= new HashMap<>();
		if(AdminBean.adminTokenMap.containsValue(token)){
			paperMapperI.deleteAll();
			topicMapperI.delteAll();
			choiceMapperI.deleteAll();
			matchItemMapperI.deleteAll();
			map.put("info", "清空成功");
			System.out.println("清空成功");
		}
		else
			map.put("info", "无此权限");
		return map;
	}
	
	@RequestMapping("/stuClear")
	public Map<String, String> clearStu(@RequestParam UUID token){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		SessionMapperI sessionMapperI =  sqlSession.getMapper(SessionMapperI.class);
		SessionCanMapperI sessionCanMapperI = sqlSession.getMapper(SessionCanMapperI.class);
		
		Map<String, String> map= new HashMap<>();
		if(AdminBean.adminTokenMap.containsValue(token)){
			userMapperI.deleteAll();
			sessionMapperI.deleteAll();
			sessionCanMapperI.deleteAll();
			map.put("info", "清空成功");
		}
		else
			map.put("info", "无此权限");
		return map;
	}
	
	@RequestMapping("/sumDownloadClear")//导出考生成绩对应的清空
	public Map<String, String> clearSumDownload(@RequestParam UUID token){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		
		Map<String, String> map= new HashMap<>();
		if(AdminBean.adminTokenMap.containsValue(token)){
			userMapperI.deleteAll();
			map.put("info", "清空成功");
		}
		else
			map.put("info", "无此权限");
		return map;
	}

	@RequestMapping("/stuDownloadClear")//导出考生试卷对应的清空
	public Map<String, String> clearStuDownload(@RequestParam UUID token){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		CanAnswerMapperI canAnswerMapperI = sqlSession.getMapper(CanAnswerMapperI.class);
		
		Map<String, String> map= new HashMap<>();
		if(AdminBean.adminTokenMap.containsValue(token)){
			canAnswerMapperI.deleteAll();
			map.put("info", "清空成功");
		}
		else
			map.put("info", "无此权限");
		return map;
	}
	
	@RequestMapping("/load")
	public SuperRespond loadBean(@RequestParam UUID token){
		if(AdminBean.adminTokenMap.containsValue(token))
		{
			//**************************
			SqlSession sqlSession = MyBatisUtil.getSqlSession();
			SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
		/*	Timestamp timestamp = sessionMapperI.getStartTimeById(6);*/
			//*********************************
			List<Timestamp> list = sessionMapperI.getStartTime();
			ExamOperator.newLoad(list);
			SupervisorOperator.load();
			return new SuperRespond(true);
		}
		else
			return new SuperRespond(false, "无此权限");
	}
	
	@RequestMapping("/Refresh")
	public List<RoomInfoBean> getRoomInfo(@RequestParam UUID token){
		if(AdminBean.adminTokenMap.containsValue(token))
			return adminService.getRoomInfo();
		else
			return null;
	}
	
	@RequestMapping("roomConfirm")//现在这个控制器是建立在进入某个考场是新打开网页
	public Object enterRoom(@RequestParam UUID token,@RequestParam int roomId)
	{
		if(AdminBean.adminTokenMap.containsValue(token))
		{
			if(AdminBean.roomSuperBeanMap.size()==0)
				return new SuperRespond(false, "请先装载superbean和exambean");
			SuperBean superBean=AdminBean.roomSuperBeanMap.get(roomId);
			if(superBean==null)
				return new SuperRespond(false, "该考场的superbean尚未装载");
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("token", superBean.getToken());
			map.put("authorityList", superBean.getAuthorityList());
			map.put("roomName", superBean.getRoomName());
			return map;
		}
		else
			return new SuperRespond(false, "无此权限");
	}
	
	@RequestMapping("/addSubject")
	public SuperRespond addSubject(@RequestParam UUID token, @RequestParam String name,@RequestParam int duration,
			@RequestParam int earliestSubmit,@RequestParam int latestLogin,@RequestParam String map)
	{

		if(AdminBean.adminTokenMap.containsValue(token))
		{
			ObjectMapper mapper = new ObjectMapper();
			Map<Integer, String> map1=new HashMap<>();
			try {
				map1=mapper.readValue(map, new TypeReference<Map<Integer, String>>(){});
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			return adminService.subjectAdd(name, duration, earliestSubmit, latestLogin, map1);
		}
		else
			return new SuperRespond(false, "无此权限");
	}
	
	@RequestMapping("/addTopic")
	public SuperRespond addTopic(@RequestParam UUID token, @RequestParam String content,@RequestParam String choice,
			@RequestParam List<Integer> List,@RequestParam int typeId)
	{
		if(AdminBean.adminTokenMap.containsValue(token))
		{
			ObjectMapper mapper = new ObjectMapper();
			Map<Integer, String> map=new HashMap<>();
			try {
				map=mapper.readValue(choice, new TypeReference<Map<Integer, String>>(){});
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			
			return adminService.TopicAdd(content, map, List, typeId);
		}
		else
			return new SuperRespond(false, "无此权限");
	}
	@PostMapping("/examForm")
	public Map<String, String> handleFormUpload(@RequestParam("file") MultipartFile file) {

		String path=System.getProperty("project.root")+"files\\import\\";
		Map<String, String> map = new HashMap<String, String>();
		String fileName = path+file.getOriginalFilename();
		System.out.println(fileName);
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			map.put("info", "上传失败");
			return map;
		}
		
		Thread thread = new examImportThread(fileName);
		thread.start();
		map.put("info", "上传成功");
		return map;

	}
	
	@RequestMapping("/roomLists")
	public List<SessionBean> getSessionList(@RequestParam UUID token){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
		List<SessionBean> list = sessionMapperI.getSessionList();
		 
		return list;
	}
	
	@PostMapping("/stuForm")
	public Map<String, String> relationsUpload(@RequestParam("file") MultipartFile file) {
		String path= System.getProperty("project.root")+"files\\import\\";
		Map<String, String> map = new HashMap<>();
		String fileName = path +file.getOriginalFilename();
		
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("info", "上传失败");
			return map;
		}
		
		Thread thread = new relationImportThread(fileName);
		thread.start();
		map.put("info", "上传成功");
		return map;
	}
	
	@RequestMapping("/sumDownload")
	public void getFile(@RequestParam String token, @RequestParam int id ,HttpServletResponse response) {
		System.out.println(token);
		
		String path = System.getProperty("project.root")+"files\\export\\test.xls";
		
		ExcelExporter excel = new ExcelExporter(path);
		
		excel.exportMarks(exportService.getScoreCollect(id), "成绩汇总");
		String myfileName = path.substring(path.lastIndexOf('\\')+1);
		response.addHeader("Content-Disposition", "attachment;filename=" + myfileName ); 
		try {
			// get your file as InputStream
			InputStream is = new FileInputStream(new File(
					path));
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@RequestMapping("/stuDownload")
	public void getStuFile(@RequestParam UUID token , HttpServletResponse response){

		String path = System.getProperty("project.root")+"files\\export\\";
		
		String uid= "a70012";
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		String statement = "org.dclab.mapping.paperMapper.getSubName";
		
		int paperId= userMapperI.getPaperIdByUid(uid);
		
		String title = "科目："+sqlSession.selectOne(statement, paperId)+" 姓名："+userMapperI.getNmaeByUid(uid)+" 准考证号："+uid;
		List<List<Topic4PDF>> topicList	=	new ArrayList<>();
		List<String> topicNameList	=	new ArrayList<String>();
		topicNameList.add("选择题");
		topicNameList.add("简答题");
		
		UUID token1 = ExamOperator.idTokenMap.get(uid);
		ExamBean examBean = ExamOperator.tokenExamMap.get(token1);
		List<Topic4PDF>	singleChoiceList	=	new ArrayList<>();
		for(SingleChoiceBean bean : examBean.getSingleChoiceList()){
			List<String> item = new ArrayList<>();
			String answer = "";
			for(int i=0 ;i<bean.getChoiceList().size();i++)
			{
				item.add(bean.getChoiceList().get(i).getContent());
				if(bean.getChoiceList().get(i).getChoiceId()==bean.getChoiceId())
					answer=String.valueOf(i+1);
			}
			Topic4PDF topic4pdf = new Topic4PDF(bean.getContent(), answer, item);
			singleChoiceList.add(topic4pdf);
		}
		topicList.add(singleChoiceList);
		
		List<Topic4PDF> shortAnswerList		=	new ArrayList<>();
		for(ShortAnswerBean bean : examBean.getShortAnswerList()){
			List<String> item = new ArrayList<>();
			item.add(bean.getAnswer());
			
			Topic4PDF topic4pdf = new Topic4PDF(bean.getContent(), "", item);
			shortAnswerList.add(topic4pdf);
		}
		topicList.add(shortAnswerList);
		
		Paper4PDF	paper	=	new Paper4PDF(uid, title, topicList, topicNameList);
        PDFWriter.writePaper(paper, path);
        System.out.println(path+uid);
        
        String myfileName = uid+".pdf";
		response.addHeader("Content-Disposition", "attachment;filename=" + myfileName ); 
        
        try {
			// get your file as InputStream
			InputStream is = new FileInputStream(new File(
					path+uid+".pdf"));
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		
	}
	
}
 class examImportThread extends Thread{
	private String fileName;
	 
	public examImportThread(String fileName) {
		super();
		this.fileName = fileName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ExcelImporter excel = new ExcelImporter(this.fileName);
        excel.parseSubjectSheet();
        excel.parseSingleChoice();
        excel.parseMultiChoice();
        excel.parseJudgement();
        excel.parseMatching();
        excel.parseShortAnswer();
        excel.parseFillBlank();
        excel.parseMachineTest();
	}	
}
 
 class relationImportThread extends Thread{
	 private String fileName;

	public relationImportThread(String fileName) {
		super();
		this.fileName = fileName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
        ExcelImporter	excel	=	new ExcelImporter(this.fileName);
        excel.parseCandidatePaper();
        excel.parseCanidateRoom();
	}
	 
	
 }
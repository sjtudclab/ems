package org.dclab.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dclab.common.Constants;
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
import org.dclab.model.PaperInfoBean;
import org.dclab.model.RoomInfoBean;
import org.dclab.model.SessionBean;
import org.dclab.model.StudentInfoBean;
import org.dclab.model.SubjectRow;
import org.dclab.model.SuperBean;
import org.dclab.model.SuperRespond;
import org.dclab.model.SupervisorOperator;
import org.dclab.model.TopicBeanExport;
import org.dclab.service.AdminService;
import org.dclab.service.ExportService;
import org.dclab.service.ImportService;
import org.dclab.utils.ExcelExporter;
import org.dclab.utils.ExcelImporter;
import org.dclab.utils.MyBatisUtil;
//import org.dclab.utils.MyBatisUtil;
import org.dclab.utils.ZipTool;
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

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private PaperMapperI paperMapperI;
	@Autowired
	private TopicMapperI topicMapperI;
	@Autowired
	private ChoiceMapperI choiceMapperI;
	@Autowired
	private MatchItemMapperI matchItemMapperI;
	@Autowired
	private UserMapperI userMapperI ;
	@Autowired
	private SessionMapperI sessionMapperI;
	@Autowired
	private SessionCanMapperI sessionCanMapperI;
	@Autowired
	private CanAnswerMapperI canAnswerMapperI;
	
	public void setPaperMapperI(PaperMapperI paperMapperI) {
		this.paperMapperI = paperMapperI;
	}

	public void setTopicMapperI(TopicMapperI topicMapperI) {
		this.topicMapperI = topicMapperI;
	}

	public void setMatchItemMapperI(MatchItemMapperI matchItemMapperI) {
		this.matchItemMapperI = matchItemMapperI;
	}

	public void setUserMapperI(UserMapperI userMapperI) {
		this.userMapperI = userMapperI;
	}

	public void setSessionMapperI(SessionMapperI sessionMapperI) {
		this.sessionMapperI = sessionMapperI;
	}

	public void setSessionCanMapperI(SessionCanMapperI sessionCanMapperI) {
		this.sessionCanMapperI = sessionCanMapperI;
	}
	
	public static byte[] lock = new byte[1];
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
	
	public static String info = new String();
	public static boolean loadFlag = false;
	public static String info1= new String();//这两个是考生考场关系导入时用到的
	public static volatile boolean flag1 = false;
	
	@RequestMapping("/examClear")
	public Map<String, String> clearExam(@RequestParam UUID token){
		
		/*SqlSession sqlSession = MyBatisUtil.getSqlSession();
		PaperMapperI paperMapperI = sqlSession.getMapper(PaperMapperI.class);
		TopicMapperI topicMapperI = sqlSession.getMapper(TopicMapperI.class);
		ChoiceMapperI choiceMapperI = sqlSession.getMapper(ChoiceMapperI.class);
		MatchItemMapperI matchItemMapperI = sqlSession.getMapper(MatchItemMapperI.class);*/
		
		Map<String, String> map= new HashMap<>();
		if(AdminBean.adminTokenMap.containsValue(token)){
			synchronized (lock) {
				paperMapperI.deleteAll();
				topicMapperI.delteAll();
				choiceMapperI.deleteAll();
				matchItemMapperI.deleteAll();
			}
			map.put("info", "清空成功");
			System.out.println("清空成功");
		}
		else
			map.put("info", "无此权限");
		//sqlSession.close();
		return map;
	}
	

	
	@RequestMapping("/stuClear")
	public Map<String, String> clearStu(@RequestParam UUID token){
		/*SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		SessionMapperI sessionMapperI =  sqlSession.getMapper(SessionMapperI.class);
		SessionCanMapperI sessionCanMapperI = sqlSession.getMapper(SessionCanMapperI.class);*/
		
		Map<String, String> map= new HashMap<>();
		if(AdminBean.adminTokenMap.containsValue(token)){
			synchronized (lock) {
				userMapperI.deleteAll();
				sessionMapperI.deleteAll();
				sessionCanMapperI.deleteAll();
				map.put("info", "清空成功");
			}
		}
		else
			map.put("info", "无此权限");
		//sqlSession.close();
		return map;
	}
	
	@RequestMapping("/sumDownloadClear")//导出考生成绩对应的清空
	public Map<String, String> clearSumDownload(@RequestParam UUID token){
		//SqlSession sqlSession = MyBatisUtil.getSqlSession();
		//UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		
		Map<String, String> map= new HashMap<>();
		if(AdminBean.adminTokenMap.containsValue(token)){
			userMapperI.deleteAll();
			map.put("info", "清空成功");
		}
		else
			map.put("info", "无此权限");
		//sqlSession.close();
		return map;
	}

	@RequestMapping("/stuDownloadClear")//导出考生试卷对应的清空
	public Map<String, String> clearStuDownload(@RequestParam UUID token){
		//SqlSession sqlSession = MyBatisUtil.getSqlSession();
		//CanAnswerMapperI canAnswerMapperI = sqlSession.getMapper(CanAnswerMapperI.class);
		
		Map<String, String> map= new HashMap<>();
		if(AdminBean.adminTokenMap.containsValue(token)){
			canAnswerMapperI.deleteAll();
			map.put("info", "清空成功");
		}
		else
			map.put("info", "无此权限");
		//sqlSession.close();
		return map;
	}
	
	@RequestMapping("/load")
	public SuperRespond loadBean(@RequestParam UUID token){
		if(AdminBean.adminTokenMap.containsValue(token))
		{
			//**************************
			//SqlSession sqlSession = MyBatisUtil.getSqlSession();
			//SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
		/*	Timestamp timestamp = sessionMapperI.getStartTimeById(6);*/
			//*********************************
			List<Timestamp> list = sessionMapperI.getStartTime();
			ExamOperator.newLoad(list);
			SupervisorOperator.load();

			loadFlag = true ;
			
			return new SuperRespond(true);
		}
		else
			return new SuperRespond(false, "无此权限");
	}
	
	@RequestMapping("/start")
	public SuperRespond allowStart(@RequestParam UUID token,@RequestParam List<Integer> list){//管理员点击开考
		if(Constants.CanGetRoomInfo==false){
			return new SuperRespond(false, "请先装填试卷");
		}
		else if(AdminBean.adminTokenMap.containsValue(token)){
			for(int i:list){
				sessionMapperI.updateStartFlag(i);
			}
			return new SuperRespond(true);
		}
		else
			return new SuperRespond(false, "无此权限");
	}
	
	@RequestMapping("/Refresh")
	public List<RoomInfoBean> getRoomInfo(@RequestParam UUID token){
		if(Constants.CanGetRoomInfo == true){
			if(AdminBean.adminTokenMap.containsValue(token)){
				synchronized(lock){
					return adminService.getRoomInfo();
				}
			}
			else
				return null;
			}
		else
			return null;
	}
	
	@RequestMapping("/roomConfirm")//现在这个控制器是建立在进入某个考场是新打开网页
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
	
	/*@RequestMapping("/addSubject")
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
	}*/
	
	/*@RequestMapping("/addTopic")
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
	}*/
	@PostMapping("/examForm")
	public Map<String, String> handleFormUpload(@RequestParam("file") MultipartFile file) {

		String path=System.getProperty("project.root")+"files"+File.separator+"import"+File.separator;
		Map<String, String> map = new HashMap<String, String>();
		String fileName = path+file.getOriginalFilename();
		System.out.println(fileName);
		try {
			File dir = new File(path);
			if(!dir.exists())
				dir.mkdirs();
			
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			map.put("info", "上传失败");
			return map;
		}
		//每一次上传把 excel中的所有的文件名都放入hashset
		HashSet<String> fileSet	=	new HashSet<>();
		
		File file1 = new File(fileName);
		String unZipDir = System.getProperty("project.root")+"files"+File.separator+"import"+File.separator+file1.getName().replaceAll("[.][^.]+$", "");
		ZipTool.unzip(fileName,unZipDir);
		
		File files = new File(unZipDir);//创建File对象，指向文件解压后的根目录
		String[] names = files.list();//获取解压文件后文件夹中的文件名
		System.out.println("解压目录下的所有文件："+names.toString());
		String excelName = new String();
		//找到需要导入数据库的excel文件
		for(String str : names ){
			System.out.println("解压目录下的文件名："+str);
			if(str.endsWith("xls")||str.endsWith("xlsx")/*&&(new File(files.getAbsolutePath()+str).isFile())*/)
			{
				System.out.println("加了抽象路径的文件名："+files.getAbsolutePath()+str);
				excelName = str;
				break;
			}
		}
		//获取多媒体文件在服务器的相对路径
		int i = unZipDir.indexOf("files");
		Constants.multiMediaDir = unZipDir.substring(i)+File.separator;
		System.out.println("多媒体文件位置的前缀"+Constants.multiMediaDir);
		
		if(excelName.length()==0)
		{	
			map.put("info", "压缩文件中无可用excel文件");
		}
		else
		{
			String AbsolutExcelName = unZipDir+File.separator+excelName;
		
			ExcelImporter excel = new ExcelImporter(AbsolutExcelName);
			excel.setFileSet(fileSet);	//每一次上传都使用新的 HashSet装载
			excel.parseSubjectSheet();
			excel.parseSingleChoice();
			excel.parseMultiChoice();
			excel.parseJudgement();
			excel.parseMatching();
			excel.parseShortAnswer();
			excel.parseFillBlank();
			excel.parseMachineTest();
        
			boolean allExit = true;
			
			List<String> list = Arrays.asList(names);
			//检测题目附带的多媒体文件是否都存在
			for(String str : excel.getFileSet())
			{
				if(!list.contains(str))
				{
					map.put("info","多媒体文件"+str+"不存在");
					allExit=false;
					break;
				}
			}
			if(allExit==true)
				map.put("info","excel文件:"+excelName+"导入数据库成功");
			excel.close();		//关闭文件
		}
/*		AdminController.flag=true;*/

	return map;
	}
	
	@RequestMapping("/roomLists")
	public List<SessionBean> getSessionList(@RequestParam UUID token){
		//SqlSession sqlSession = MyBatisUtil.getSqlSession();
		//SqlSession sqlSession = sqlSessionFactory.openSession();
		//SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
		List<SessionBean> list = sessionMapperI.getSessionList();
		
		for(SessionBean sBean : list){
			if(sessionMapperI.getStartFlag(sBean.getId())==1)
				sBean.setStatus(3);
			else if(sessionMapperI.getStartFlag(sBean.getId())==2)
				sBean.setStatus(4);
			else if(loadFlag == true)
				sBean.setStatus(1);
			else if (loadFlag == false)
				sBean.setStatus(0);
			else 
				sBean.setStatus(2);
			int count = 0,sum;
			
			List<String> userList = sessionMapperI.getUidById(sBean.getId());
			
			sum = userList.size();
			
			for(String uid : userList){
				UUID token1 = ExamOperator.idTokenMap.get(uid);
				ExamBean examBean = ExamOperator.tokenExamMap.get(token1);
				
				if(examBean!=null&&examBean.isIfLogin() == true){
					count++;
				}
			}
			
			sBean.setTestNum(sum);
			sBean.setRegisteredNum(count);
			sBean.setUnRegisteredNum(sum-count);
		}
		//sqlSession.close();
		return list;
	}
	
	@RequestMapping("/paperInfo")
	public List<PaperInfoBean> getPaperInfo(){
		return adminService.returnPaperInfo();
	}
	
	@RequestMapping("/stuInfo")
	public List<StudentInfoBean> getStuInfo(){
		return adminService.getStuInfo();
	}
	
	@PostMapping("/stuForm")
	public Map<String, String> relationsUpload(@RequestParam("file") MultipartFile file) {
		String path= System.getProperty("project.root")+"files"+File.separator+"import"+File.separator;
		Map<String, String> map = new HashMap<>();
		String fileName = path +file.getOriginalFilename();
		
		try {
			File dir = new File(path);
			if(!dir.exists())
				dir.mkdirs();
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
		
		//每一次上传把 excel中的所有的文件名都放入hashset
		HashSet<String> fileSet	=	new HashSet<>();
		
		File file1 = new File(fileName);
		String unZipDir = System.getProperty("project.root")+"files"+File.separator+"import"+File.separator+file1.getName().replaceAll("[.][^.]+$", "");
		ZipTool.unzip(fileName,unZipDir);
		
		File files = new File(unZipDir);//创建File对象，指向文件解压后的根目录
		String[] names = files.list();//获取解压文件后文件夹中的文件名
		System.out.println("解压目录下的所有文件："+names.toString());
		String excelName = new String();
		//找到需要导入数据库的excel文件
		for(String str : names ){
			System.out.println("解压目录下的文件名："+str);
			if(str.endsWith("xls")||str.endsWith("xlsx")/*&&(new File(files.getAbsolutePath()+str).isFile())*/)
			{
				System.out.println("加了抽象路径的文件名："+files.getAbsolutePath()+str);
				excelName = str;
				break;
			}
		}
		Constants.photoDir = unZipDir;
		if(excelName.length()==0)
		{	
			AdminController.info="压缩文件中无可用excel文件";
		}
		else
		{
			String AbsolutExcelName = unZipDir+File.separator+excelName;
		
	        ExcelImporter	excel	=	new ExcelImporter(AbsolutExcelName);
	        excel.setFileSet(fileSet);	//每一次上传都使用新的 HashSet装载
	        excel.parseCandidatePaper();
	        excel.parseCanidateRoom();
	        excel.close();//关闭文件
        
			boolean allExit = true;
			
			List<String> list = Arrays.asList(names);
			//检测题目附带的多媒体文件是否都存在
			for(String str : excel.getFileSet())
			{
				if(!list.contains(str))
				{
					map.put("info","文件"+str+"不存在");
					allExit=false;
					break;
				}
			}
			if(allExit==true)
				map.put("info","excel文件:"+excelName+"导入数据库成功");
			excel.close();		//关闭文件
		}
		
		return map;
		
		
}
	
/*	@RequestMapping("uploadCheck")
	public Map<String, String> uploadCheckInfoReturn(){
		System.out.println("进入info返回函数");
		while(AdminController.flag==false){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Map<String, String> map = new HashMap<>();
		map.put("info", AdminController.info);
		AdminController.flag=false;
		AdminController.info = new String();
		return map;
	}
	*/
	@RequestMapping("/sumDownload")
	public void getFile(@RequestParam String token,HttpServletResponse response,@RequestParam int...id ) throws IOException {
		System.out.println("进入SUm函数");
		if(ExamOperator.tokenExamMap.isEmpty())
		{
			 response.setHeader("Content-type","text/html;charset=UTF-8");
			 String data = "试卷尚未装载，不能导出";
			 OutputStream stream = response.getOutputStream();
			 stream.write(data.getBytes("UTF-8"));
		}
		else if(id.length==0)
		{
			response.setHeader("Content-type","text/html;charset=UTF-8");
			 String data = "请先选择考场再导出";
			 OutputStream stream = response.getOutputStream();
			 stream.write(data.getBytes("UTF-8"));
		}
		else{
		String path = System.getProperty("project.root")+"files"+File.separator+"export";
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
		String fileName = path + File.separator+"test.xls";
		ExcelExporter excel = new ExcelExporter(fileName);
		
		excel.exportMarks(exportService.getScoreCollect(id[0]), "成绩汇总");
		String myfileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
		response.addHeader("Content-Disposition", "attachment;filename=" + myfileName ); 
		try {
			// get your file as InputStream
			InputStream is = new FileInputStream(new File(fileName));
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		}
	}
	
	@RequestMapping("/stuDownload")
	public void getStuFile(@RequestParam UUID token , HttpServletResponse response, @RequestParam int...id ) throws IOException{
		System.out.println("进入stu函数");
		if(ExamOperator.tokenExamMap.isEmpty())
		{
			 response.setHeader("Content-type","text/html;charset=UTF-8");
			 String data = "试卷尚未装载，不能导出";
			 OutputStream stream = response.getOutputStream();
			 stream.write(data.getBytes("UTF-8"));
		}
		else if(id.length==0)
		{
			response.setHeader("Content-type","text/html;charset=UTF-8");
			 String data = "请先选择考场再导出";
			 OutputStream stream = response.getOutputStream();
			 stream.write(data.getBytes("UTF-8"));
		}
		else{
			
		/*SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
		SessionCanMapperI sessionCanMapperI = sqlSession.getMapper(SessionCanMapperI.class);
		PaperMapperI paperMapperI = sqlSession.getMapper(PaperMapperI.class);
		TopicMapperI topicMapperI = sqlSession.getMapper(TopicMapperI.class);
		String statement = "org.dclab.mapping.paperMapper.getSubName";*/
		
		String roomName = sessionMapperI.getRoomNameById(id[0]);
		long  startTime =sessionMapperI.getStartTimeById(id[0]).getTime();
		String path = System.getProperty("project.root")+"files"+File.separator+"export"+File.separator+roomName+"-"+startTime;//放置该考场考生考卷的文件夹。
		System.out.println("放置考生考卷的文件夹:"+path);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		Map<Integer, List<Object>> subjectMap = new HashMap<>();//存储科目信息的map
		Map<Integer, TopicBeanExport> topicMap = new HashMap<>();//存储topicId和points，correctanswer的map
		
		List<SubjectRow> subjectRows = paperMapperI.getSubjectrowById();
		for(SubjectRow subjectRow : subjectRows){
			subjectMap.put(subjectRow.getPaperId(), subjectRow.toList());//这样subjectmap中保存paperid和导出科目sheet需要的数据的映射
		}
		
		List<TopicBeanExport> topicList = topicMapperI.getPointsAndCorrect();
		for(TopicBeanExport topicBeanExport : topicList){
			topicMap.put(topicBeanExport.getId(), topicBeanExport);
		}
		
		List<String> uidList = sessionCanMapperI.getUidListBySid(id[0]);
		for(String str : uidList){
			String excelPath = path+File.separator+str+".xls";
			adminService.exportPaper(excelPath, str, topicMap, subjectMap);
		}
		String zipName = path+".zip";
		ZipTool.compress(path, zipName);
		
        
        response.addHeader("Content-Disposition", "attachment;filename=" + "123.zip" );  
        try {
			// get your file as InputStream
			InputStream is = new FileInputStream(new File(
					zipName));
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) 
        {
			ex.printStackTrace();
        }
		
	}
	}
	}
/* class examImportThread extends Thread{
	private String fileName;
	private HashSet<String> fileSet;
	 
	public examImportThread(String fileName, HashSet<String> fileSet) {
		super();
		this.fileName = fileName;
		this.fileSet  =	fileSet;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		File file = new File(fileName);
		String unZipDir = System.getProperty("project.root")+"files\\import\\"+file.getName().replaceAll("[.][^.]+$", "");
		ZipTool.unzip(fileName,unZipDir);
		
		File files = new File(unZipDir);//创建File对象，指向文件解压后的根目录
		String[] names = files.list();//获取解压文件后文件夹中的文件名
		System.out.println("解压目录下的所有文件："+names.toString());
		String excelName = new String();
		//找到需要导入数据库的excel文件
		for(String str : names ){
			System.out.println("解压目录下的文件名："+str);
			if(str.endsWith("xls")&&(new File(files.getAbsolutePath()+str).isFile()))
			{
				System.out.println("加了抽象路径的文件名："+files.getAbsolutePath()+str);
				excelName = str;
				break;
			}
		}
		//获取多媒体文件在服务器的相对路径
		int i = unZipDir.indexOf("files");
		Constants.multiMediaDir = unZipDir.substring(i)+"\\";
		System.out.println("多媒体文件位置的前缀"+Constants.multiMediaDir);
		
		if(excelName.length()==0)
		{	
			AdminController.info="压缩文件中无可用excel文件";
		}
		else
		{
			String AbsolutExcelName = unZipDir+"\\"+excelName;
		
			ExcelImporter excel = new ExcelImporter(AbsolutExcelName);
			excel.setFileSet(this.fileSet);	//每一次上传都使用新的 HashSet装载
			excel.parseSubjectSheet();
			excel.parseSingleChoice();
			excel.parseMultiChoice();
			excel.parseJudgement();
			excel.parseMatching();
			excel.parseShortAnswer();
			excel.parseFillBlank();
			excel.parseMachineTest();
        
			boolean allExit = true;
			
			List<String> list = Arrays.asList(names);
			//检测题目附带的多媒体文件是否都存在
			for(String str : excel.getFileSet())
			{
				if(!list.contains(str))
				{
					AdminController.info="多媒体文件"+str+"不存在";
					allExit=false;
					break;
				}
			}
			if(allExit==true)
				AdminController.info="excel文件:"+excelName+"导入数据库成功";
			excel.close();		//关闭文件
		}
		AdminController.flag=true;
	}
	
}*/
 
 /*class relationImportThread extends Thread{
	private String fileName;
	private HashSet<String> fileSet; 

	public relationImportThread(String fileName, HashSet<String> fileSet) {
		super();
		this.fileName = fileName;
		this.fileSet  = fileSet;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		File file = new File(fileName);
		String unZipDir = System.getProperty("project.root")+"files\\import\\"+file.getName().replaceAll("[.][^.]+$", "");
		ZipTool.unzip(fileName,unZipDir);
		
		File files = new File(unZipDir);//创建File对象，指向文件解压后的根目录
		String[] names = files.list();//获取解压文件后文件夹中的文件名
		System.out.println("解压目录下的所有文件："+names.toString());
		String excelName = new String();
		//找到需要导入数据库的excel文件
		for(String str : names ){
			System.out.println("解压目录下的文件名："+str);
			if(str.endsWith("xls")&&(new File(files.getAbsolutePath()+str).isFile()))
			{
				System.out.println("加了抽象路径的文件名："+files.getAbsolutePath()+str);
				excelName = str;
				break;
			}
		}
		Constants.photoDir = unZipDir;
		if(excelName.length()==0)
		{	
			AdminController.info="压缩文件中无可用excel文件";
		}
		else
		{
			String AbsolutExcelName = unZipDir+"\\"+excelName;
		
	        ExcelImporter	excel	=	new ExcelImporter(AbsolutExcelName);
	        excel.setFileSet(this.fileSet);	//每一次上传都使用新的 HashSet装载
	        excel.parseCandidatePaper();
	        excel.parseCanidateRoom();
	        excel.close();//关闭文件
        
			boolean allExit = true;
			
			List<String> list = Arrays.asList(names);
			//检测题目附带的多媒体文件是否都存在
			for(String str : excel.getFileSet())
			{
				if(!list.contains(str))
				{
					AdminController.info="文件"+str+"不存在";
					allExit=false;
					break;
				}
			}
			if(allExit==true)
				AdminController.info="excel文件:"+excelName+"导入数据库成功";
			excel.close();		//关闭文件
		}
		AdminController.flag=true;
		

	}
 }}*/
/* class infoReturnThread extends Thread{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(AdminController.flag==false)
		{
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	 
 }*/
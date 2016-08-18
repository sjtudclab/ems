package org.dclab.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.SessionMapperI;
import org.dclab.model.AdminBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.RoomInfoBean;
import org.dclab.model.SubjectRow;
import org.dclab.model.SuperBean;
import org.dclab.model.SuperRespond;
import org.dclab.model.SupervisorOperator;
import org.dclab.service.AdminService;
import org.dclab.service.ImportService;
import org.dclab.utils.ExcelImporter;
import org.dclab.utils.MyBatisUtil;
import org.junit.runners.Parameterized.Parameters;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
/*	@Autowired
	private ImportService importService;
	public void setImportService(ImportService importService) {
		this.importService = importService;
	}*/

	@RequestMapping("/load")
	public SuperRespond loadBean(@RequestParam UUID token){
		if(AdminBean.adminTokenMap.containsValue(token))
		{
			//**************************
			SqlSession sqlSession = MyBatisUtil.getSqlSession();
			SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
			Timestamp timestamp = sessionMapperI.getStartTimeById(6);
			//*********************************
			ExamOperator.newLoad(timestamp);
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
	
	@RequestMapping("/import")
	public boolean Import(@RequestParam UUID token){
		
		String path=System.getProperty("project.root");
		System.out.println("根目录  ："+path);
		String fileName=path+"/files/import/试卷模板_final.xls";
		System.out.println("文件路径  ："+fileName);
		ExcelImporter excel = new ExcelImporter(fileName);
        //System.out.println(excel.readLine(0));
        excel.parseSubjectSheet();
        excel.parseSingleChoice();
        excel.parseMultiChoice();
        excel.parseJudgement();
        excel.parseMatching();
        excel.parseShortAnswer();
        excel.parseFillBlank();
        excel.parseMachineTest();
        return true;
	}
	
	@RequestMapping("/test")
	public int test(@RequestParam UUID token){
		ImportService importService=new ImportService();
		SubjectRow subjectRow=new SubjectRow();
		subjectRow.setProName("ruanjian");
		subjectRow.setProId("123");
		subjectRow.setSubName("suanfa");
		subjectRow.setSubId("1234");
		subjectRow.setPaperNum("12345");
		subjectRow.setDuration(7200);
		subjectRow.setEarliestSubmit(1800);
		subjectRow.setLatestLogin(1800);
		subjectRow.setShowMark(0);
		
		return importService.importSubject(subjectRow);
	}

}

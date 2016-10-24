package org.dclab.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.WriteAbortedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.common.Constants;
import org.dclab.controller.AdminController;
import org.dclab.mapping.CorrectAnswerMapperI;
import org.dclab.mapping.PaperMapperI;
import org.dclab.mapping.RoomMapperI;
import org.dclab.mapping.SessionCanMapperI;
import org.dclab.mapping.SessionMapperI;
import org.dclab.mapping.SubTypeMapperI;
import org.dclab.mapping.SubjectMapperI;
import org.dclab.mapping.TopicMapperI;
import org.dclab.model.ChoicesBean;
import org.dclab.model.ContentBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.FillBlankBean;
import org.dclab.model.JudgementBean;
import org.dclab.model.MachineTestBean;
import org.dclab.model.MatchingBean;
import org.dclab.model.MultiChoicesBean;
import org.dclab.model.PaperInfoBean;
import org.dclab.model.RoomInfoBean;
import org.dclab.model.ShortAnswerBean;
import org.dclab.model.SingleChoiceBean;
import org.dclab.model.StudentInfoBean;
import org.dclab.model.SuperRespond;
import org.dclab.model.SupervisorOperator;
import org.dclab.model.TopicBean;
import org.dclab.model.TopicBeanExport;
import org.dclab.utils.ExcelExporter;
import org.dclab.utils.MyBatisUtil;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Service;

import com.sun.corba.se.spi.ior.Writeable;

/**
 * @author alvis
 *管理员的一些操作
 */

@Service
public class AdminService {
	public static int currentSubjectId=0;
	
	/*public SuperRespond subjectAdd(String name,int duration,int earliestSubmit,int latestLogin,Map<Integer, String> map){
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		SubjectMapperI smapper=sqlSession.getMapper(SubjectMapperI.class);
		//把分钟转化为妙
		duration=duration*60;
		earliestSubmit=earliestSubmit*60;
		latestLogin=latestLogin*60;
		
		
		int sign1=smapper.add(name, duration, earliestSubmit, latestLogin);
		sqlSession.commit();//没有这个语句就算上条语句执行成功数据库也不会更新的兄弟
		if(sign1!=1)
			return new SuperRespond(false, "添加科目失败");
		
		try {
			smapper.getSubIdByName(name);
		} catch (org.apache.ibatis.exceptions.TooManyResultsException e) {
			// TODO: handle exception
			return new SuperRespond(false, "数据库中已有相同科目");
		}
		int subId=smapper.getSubIdByName(name);
		currentSubjectId=subId;
		
		SubTypeMapperI stmapper=sqlSession.getMapper(SubTypeMapperI.class);
		
		for(int typeId : map.keySet())
		{
			String points=map.get(typeId);
			int sign2=stmapper.add(typeId, subId, points);
			sqlSession.commit();
			if(sign2!=1)
				return new SuperRespond(false,"添加科目题型关联失败");
		}

		sqlSession.close();
		return new SuperRespond(true);
	}*/
	
/*	public SuperRespond TopicAdd(String content,Map<Integer, String> choice,List<Integer> answer,int typeId){
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.topicMapper.add";
		TopicBean topicBean=new TopicBean(content, typeId, currentSubjectId);
		sqlSession.insert(statement, topicBean);
		sqlSession.commit();
		
		int topicId=topicBean.getId();
		String choiceId=new String();//这个list存放正确选项的数据库id
		for(int str : choice.keySet())
		{
			ChoicesBean choicesBean=new ChoicesBean();
			choicesBean.setContent(choice.get(str));
			choicesBean.setTopicId(topicId);
			String statement1 = "org.dclab.mapping.choiceMapper.add";
			sqlSession.insert(statement1, choicesBean);
			sqlSession.commit();
			if(answer.contains(str))
			{
				if(choiceId.isEmpty())
					choiceId=String.valueOf(choicesBean.getChoiceId());
				else
					choiceId=choiceId+","+String.valueOf(choicesBean.getChoiceId());
			}
		}
		
		SubTypeMapperI stmapper=sqlSession.getMapper(SubTypeMapperI.class);
		
		String points=stmapper.getPointsByType(typeId,currentSubjectId);
		
		CorrectAnswerMapperI camapper=sqlSession.getMapper(CorrectAnswerMapperI.class);
		
		camapper.add(topicId, choiceId, points);
		sqlSession.commit();
		
		sqlSession.close();
		return new SuperRespond(true);
	}*/
	
	public List<RoomInfoBean> getRoomInfo()
	{
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
		SessionCanMapperI sessionCanMapperI = sqlSession.getMapper(SessionCanMapperI.class);
		
		List<RoomInfoBean> list=sessionMapperI.getRoomInfo();
		for(RoomInfoBean bean : list){
			System.out.println(bean.getUid());
			UUID token=SupervisorOperator.idTokenMap.get(bean.getUid());
			bean.setSize(sessionCanMapperI.getSizeOfSession(bean.getId()));
			//检测监考老师是否登录,可以考虑删除，关联的位置有superbean，userservice登录时
			if(SupervisorOperator.tokenSuperMap.get(token).getSign()==1)
				bean.setStatus(1);
			else
				bean.setStatus(0);
			if(AdminController.loadFlag==true)
				bean.setLoadStatus(1);
			else
				bean.setLoadStatus(0);
			if(sessionMapperI.getStartFlag(bean.getId())==1)
				bean.setExamStatus(1);
			else if(sessionMapperI.getStartFlag(bean.getId())==2)
				bean.setExamStatus(2);
			else
				bean.setExamStatus(0);
			
		}
		sqlSession.close();
		return list;
	}
	
	public void exportPaper(String excelPath,String str,Map<Integer, TopicBeanExport> topicMap,Map<Integer, List<Object>> subjectMap){
		ExcelExporter excel =   new ExcelExporter(excelPath);
		Map<String, List<List<? extends Object>>> map = new HashMap<>();
		
		ExamBean examBean = ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(str));
		int paperId = examBean.getPaperId();
		
        List<List<? extends Object>> subject  = new ArrayList<>();//考生答卷list中的科目sheet
        subject.add(subjectMap.get(paperId));
        
        String examNum = (String) subjectMap.get(paperId).get(4);//试卷号
        
        List<List<? extends Object>> singleList  = new ArrayList<>();
        for(SingleChoiceBean singleChoiceBean : examBean.getSingleChoiceList()){
        	List<Object> list = new ArrayList<>();
        	list.add(examNum);
        	list.add(singleChoiceBean.getNumber());
        	list.add(singleChoiceBean.getId());
        	list.add(singleChoiceBean.getContent());
        	list.add((topicMap.get(singleChoiceBean.getId())).getCorrectAnswer());
        	//可能会慢，一种解决方案是给考生装载exambean时，将正确答案填入。
        	list.add(singleChoiceBean.getChoiceId());
        	list.add(topicMap.get(singleChoiceBean.getId()).getPoints());//和上面一样的问题
        	list.add(singleChoiceBean.getImg());
        	list.add(singleChoiceBean.getAudio());
        	list.add(singleChoiceBean.getVideo());
        	list.add(singleChoiceBean.getChoiceList().size());
        	for(ChoicesBean choicesBean : singleChoiceBean.getChoiceList()){
        		list.add(choicesBean.getContent());
        	}
        	singleList.add(list);
        }
        List<List<? extends Object>> multiList  = new ArrayList<>();
        for(MultiChoicesBean multiChoicesBean :examBean.getMultiChoicesList()){
        	List<Object> list = new ArrayList<>();
        	list.add(examNum);
        	list.add(multiChoicesBean.getNumber());
        	list.add(multiChoicesBean.getId());
        	list.add(multiChoicesBean.getContent());
        	list.add(topicMap.get(multiChoicesBean.getId()).getCorrectAnswer());
        	list.add(listToString(multiChoicesBean.getChoiceIdList()));
        	String point = topicMap.get(multiChoicesBean.getId()).getPoints();
        	int index = point.indexOf(",");
        	list.add(point.substring(0, index));
        	list.add(point.substring(index+1, point.length()));
        	list.add(multiChoicesBean.getImg());
        	list.add(multiChoicesBean.getAudio());
        	list.add(multiChoicesBean.getVideo());
        	list.add(multiChoicesBean.getChoiceList().size());
        	for(ChoicesBean choicesBean : multiChoicesBean.getChoiceList()){
        		list.add(choicesBean.getContent());
        	}
        	multiList.add(list);
        }
        	
        List<List<? extends Object>> judgeList  = new ArrayList<>();
        for(JudgementBean judgementBean : examBean.getJudgementList()){
        	List<Object> list = new ArrayList<>();
        	list.add(examNum);//试卷号
        	list.add(judgementBean.getNumber());
        	list.add(judgementBean.getId());
        	list.add(judgementBean.getContent());
        	list.add(topicMap.get(judgementBean.getId()).getCorrectAnswer());
        	list.add(judgementBean.getChoiceId());
        	list.add(topicMap.get(judgementBean.getId()).getPoints());//此处可修改，因为每种题型的points一般来说是一样的
        	list.add(judgementBean.getImg());
        	list.add(judgementBean.getAudio());
        	list.add(judgementBean.getVideo());
        	
        	judgeList.add(list);
        }
        
        List<List<? extends Object>> matchList = new ArrayList<>();
        for(MatchingBean matchingBean : examBean.getMatchingList()){
        	List<Object> list = new ArrayList<>();
        	list.add(examNum);
        	list.add(matchingBean.getNumber());
        	list.add(matchingBean.getId());
        	list.add(matchingBean.getContent());
        	list.add(topicMap.get(matchingBean.getId()).getCorrectAnswer());
        	list.add(matchingBean.getChoiceIdMap().toString());
        	list.add(topicMap.get(matchingBean.getId()).getPoints());
        	list.add(matchingBean.getImg());
        	list.add(matchingBean.getAudio());
        	list.add(matchingBean.getVideo());
        	list.add(matchingBean.getContentList().size());
        	for(ContentBean contentBean : matchingBean.getContentList()){
        		list.add(contentBean.getContent());
        	}
        	for(ChoicesBean choicesBean : matchingBean.getChoiceList()){
        		list.add(choicesBean.getContent());
        	}
        	
        	matchList.add(list);
        }
        
        List<List<? extends Object>> shortList = new ArrayList<>();
        for(ShortAnswerBean shortAnswerBean : examBean.getShortAnswerList()){
        	List<Object> list = new ArrayList<>();
        	list.add(examNum);
        	list.add(shortAnswerBean.getNumber());
        	list.add(shortAnswerBean.getId());
        	list.add(shortAnswerBean.getContent());
        	list.add(topicMap.get(shortAnswerBean.getId()).getCorrectAnswer());
        	list.add(shortAnswerBean.getAnswer());
        	list.add(topicMap.get(shortAnswerBean.getId()).getPoints());
        	list.add(shortAnswerBean.getImg());
        	list.add(shortAnswerBean.getAudio());
        	list.add(shortAnswerBean.getVideo());
        	list.add(shortAnswerBean.getPdf());
        	
        	shortList.add(list);
        }
        
        List<List<? extends Object>> fillList = new ArrayList<>();
        for(FillBlankBean fillBlankBean : examBean.getFillBlankList()){
        	List<Object> list = new ArrayList<>();
        	list.add(examNum);
        	list.add(fillBlankBean.getNumber());
        	list.add(fillBlankBean.getId());
        	list.add(fillBlankBean.getContent());
        	list.add(topicMap.get(fillBlankBean.getId()).getCorrectAnswer());
        	list.add(strListToString(fillBlankBean.getAnswerList()));
        	list.add(topicMap.get(fillBlankBean.getId()).getPoints());
        	list.add(fillBlankBean.getImg());
        	list.add(fillBlankBean.getAudio());
        	list.add(fillBlankBean.getVideo());
        	list.add(fillBlankBean.getFillNum());
        	list.add(fillBlankBean.getPdf());
        	
        	fillList.add(list);
        }
        
        List<List<? extends Object>> machineList = new ArrayList<>();
        for(MachineTestBean machineTestBean : examBean.getMachineList()){
        	List<Object> list = new ArrayList<>();
        	list.add(examNum);
        	list.add(machineTestBean.getNumber());
        	list.add(machineTestBean.getId());
        	list.add(machineTestBean.getContent());
        	list.add(topicMap.get(machineTestBean.getId()).getCorrectAnswer());
        	list.add(machineTestBean.getFileName());
        	list.add(topicMap.get(machineTestBean.getId()).getPoints());
        	list.add(machineTestBean.getImg());
        	list.add(machineTestBean.getAudio());
        	list.add(machineTestBean.getVideo());
        	list.add(machineTestBean.getPdf());
        	
        	machineList.add(list);
        }
        Map<String, List<List<? extends Object>>> map1 = new HashMap<>();
        map1.put("科目", subject);
        map1.put("单选题", singleList);
        map1.put("多选题", multiList);
        map1.put("判断题", judgeList);
        map1.put("匹配题", matchList);
        map1.put("简答题", shortList);
        map1.put("填空题", fillList);
        map1.put("上机题", machineList);
        excel.exportStudentPaper(map1);
        
	}
	
	public String listToString(List<Integer> list){
		String string = new String();
		if(list.size()>0)
		{
			for(int i=0;i<list.size()-1;i++)
				string=string+list.get(i)+",";
			string = string +list.get(list.size()-1);
		}
		return string;
	}
	
	public String strListToString(List<String> list){
		String string = new String();
		if(list!=null&&list.size()>0)
		{
			for(int i=0;i<list.size()-1;i++)
				string=string+list.get(i)+",";
			string = string +list.get(list.size()-1);
		}
		return string;
	}
	
	public List<PaperInfoBean> returnPaperInfo(){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		PaperMapperI pMapperI = sqlSession.getMapper(PaperMapperI.class);
		TopicMapperI tMapperI = sqlSession.getMapper(TopicMapperI.class);
		
		List<PaperInfoBean> list = pMapperI.getPaperInfo();
		
		int num,mark,totalScore=0;
		int typeId,paperId;
		String string = new String();
		String[] typeName = {"单选题","多选题","判断题","匹配题","简答题","填空题","上机题"};
		
		for(PaperInfoBean paperInfoBean : list){
			paperId = paperInfoBean.getPaperId();
			List<Integer> list2 = tMapperI.getTypeList(paperId);
			int index=0;
			for(int i=0;i<7;i++){
				if(i==list2.get(index)){
					typeId = i;
					mark = tMapperI.getSumOfType(typeId, paperId);
					num = tMapperI.getNumOfType(typeId, paperId);
					string = string+typeName[i]+num+"题共"+mark+"分，";
					totalScore = totalScore+mark;
					index++;
				}
			}
			paperInfoBean.setTotalScore(totalScore);
			paperInfoBean.setTypeNumScore(string);
			totalScore = 0;
			string = new String();
		}
		sqlSession.close();
		return list;
	}
	
	public List<StudentInfoBean> getStuInfo(){
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		SessionMapperI sMapperI = sqlSession.getMapper(SessionMapperI.class);
		
		List<StudentInfoBean> list = sMapperI.getStuInfoAll();
		
		for(StudentInfoBean sInfoBean : list){
			UUID token = ExamOperator.idTokenMap.get(sInfoBean.getUid());
			ExamBean examBean = ExamOperator.tokenExamMap.get(token);
			
			if(examBean==null || examBean.isIfLogin() == false)
				sInfoBean.setStatus(0);
			else if(examBean.isFinished())
				sInfoBean.setStatus(2);
			else 
				sInfoBean.setStatus(1);
		}
		
		return list;
	}
	
	/*public String choiceMapToString(Map<Integer, Integer> map){
		String string = new String();
		if(map!=null){
			Set<Integer> keyList =  map.keySet();
			Collections.sort(keyList, new Comparator<Integer>() {

				@Override
				public int compare(Integer arg0, Integer arg1) {
					// TODO Auto-generated method stub
					return arg0.compareTo(arg1);
				}
				
			});
			
			for(int i : keyList){
				string = string + map.get(i)+",";
			}
		}
		return string;
	}*/
	

	
	
}

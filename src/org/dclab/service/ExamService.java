package org.dclab.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dclab.model.CheckBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.MatchingBean;
import org.dclab.model.MultiChoicesBean;
import org.dclab.model.SingleChoiceBean;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 试卷操作
 * （拿到ExamBean，当前tab代表试题类型，默认是单选
 * 考试页面：
 * 1. 根据题目类型和ID得到这一题
 * 2. 下一题 （注意获取检查标志状态 ，答案写入内存； （灾备方案：此次操作距离上次超过一分钟，将用户ExamBean写入commit log
 * 3. 上一题 
 * 
 * 检查页面：
 * 1. 得到所有检查的题目，
 * 2. 点击进入该题目
 * 3. （下一题）
 * 
 * 交卷
 * 1. 写入数据库
 * @author alvis
 *
 */
@Service
public class ExamService {

	//获取第一题
	public Object getFirstTopic(ExamBean exambean,int typeId)
	{
		switch(typeId)
		{
		case 0:
			return exambean.getSingleChoiceById(0);
		case 1:
			return exambean.getMultiChoiceById(0);
		case 3:
			return exambean.getMatchingById(0);
		case 2:
			return exambean.getJudgementById(0);
		case 4:
			return exambean.getShortAnswerById(0);
		case 5:
			return exambean.getFillBlankById(0);
		case 6:
			return exambean.getMachineTestById(0);
		default:
			System.out.println("获取第一题出错");
		return null;
		}
	}
	//由token获取exambean
	public ExamBean getExambeanByToken(UUID token)
	{
		return ExamOperator.tokenExamMap.get(token);
	}
	//存储当前题目的状态
	public void storeTopic(ExamBean exambean,int typeId,int id,int choiceId,boolean ifCheck)
	{
		switch (typeId) {
		case 0:
			exambean.getSingleChoiceById(id).setChoiceId(choiceId);
			if(exambean.getSingleChoiceById(id).getChoiceId()!=0)
				exambean.getFinishTopic().add(exambean.getSingleChoiceById(id).getId());
			exambean.getSingleChoiceById(id).setIfCheck(ifCheck);
			break;
		case 2:
			exambean.getJudgementById(id).setChoiceId(choiceId);
			if(exambean.getJudgementById(id).getChoiceId()!=0)
				exambean.getFinishTopic().add(exambean.getJudgementById(id).getId());
			exambean.getJudgementById(id).setIfCheck(ifCheck);
			break;
		default:
			System.out.println("storeTopic运行出错");
			break;
		}
	}
	//多选题存储，重载
	public void storeTopic(ExamBean exambean,int typeId,int id,List<?> List,boolean ifCheck)
	{
		if(typeId==1)
		{
		if(List!=null)
			exambean.getMultiChoiceById(id).setChoiceIdList((java.util.List<Integer>) List);
		if(exambean.getMultiChoiceById(id).getChoiceIdList()!=null&&
				exambean.getMultiChoiceById(id).getChoiceIdList().size()!=0)
			exambean.getFinishTopic().add(exambean.getMultiChoiceById(id).getId());
		exambean.getMultiChoiceById(id).setIfCheck(ifCheck);
		}
		else{
			if(List!=null)
				exambean.getFillBlankById(id).setAnswerList((java.util.List<String>) List);
			if(exambean.getFillBlankById(id).getAnswerList()!=null&&exambean.getFillBlankById(id).getAnswerList().size()!=0)
				exambean.getFinishTopic().add(exambean.getFillBlankById(id).getId());
			exambean.getFillBlankById(id).setIfCheck(ifCheck);
		}
	}
	//匹配题
	public void storeTopic(ExamBean exambean,int typeId,int id,Map<Integer, Integer> choiceIdMap,boolean ifCheck)
	{
		exambean.getMatchingById(id).setChoiceIdMap(choiceIdMap);
		if(exambean.getMatchingById(id).getChoiceIdMap().size()!=0)
			exambean.getFinishTopic().add(exambean.getMatchingById(id).getId());
		exambean.getMatchingById(id).setIfCheck(ifCheck);
	}
	//简答题存储
	public void storeTopic(ExamBean exambean,int typeId,int id,String answer,boolean ifCheck){
		if(typeId==4)
		{
		exambean.getShortAnswerById(id).setAnswer(answer);
		if(exambean.getShortAnswerById(id).getAnswer()!=null)
			exambean.getFinishTopic().add(exambean.getShortAnswerById(id).getId());
		exambean.getShortAnswerById(id).setIfCheck(ifCheck);
		}
		else
		{
			exambean.getMachineTestById(id).setFileName(answer);
			if(exambean.getMachineTestById(id).getFileName()!=null)
				exambean.getFinishTopic().add(exambean.getMachineTestById(id).getId());
			exambean.getMachineTestById(id).setIfCheck(ifCheck);
		}
	}


	//根据typeid和id获取题目。
	public Object getTopic(ExamBean exambean,int typeId,int id)
	{
		switch(typeId)
		{
		case 0:
			if(id==exambean.getSingleChoiceList().size()||id<0)//判断是不是超出了该题型的范围
				return null;
			else
				return exambean.getSingleChoiceById(id);
		case 1:
			if(id==exambean.getMultiChoicesList().size()||id<0)
				return null;
			else
				return exambean.getMultiChoiceById(id);
		case 3:
			if(id==exambean.getMatchingList().size()||id<0)
				return null;
			else
				return exambean.getMatchingById(id);
		case 2:
			if(id==exambean.getJudgementList().size()||id<0)
				return null;
			else
				return exambean.getJudgementById(id);
		case 4:
			if(id==exambean.getShortAnswerList().size()||id<0)
				return null;
			else
				return exambean.getShortAnswerById(id);
		case 5:
			if(id==exambean.getFillBlankList().size()||id<0)
				return null;
			else {
				return exambean.getFillBlankById(id);
			}
		case 6:
			if(id==exambean.getMachineList().size()||id<0)
				return null;
			else {
				return exambean.getMachineTestById(id);
			}
		default:
			System.out.println("getTopic error");
			return null;
		}
	}
	
	//把前端传来的json的string转换成需要的答案id的list
	public List<Integer> stringToList(String str)
	{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Integer> map=new HashMap<>();
		try {
			map=mapper.readValue(str, new TypeReference<Map<String, Integer>>(){});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Integer> list=new ArrayList<>();
		for(String key : map.keySet()){
			list.add(map.get(key));
		}
		return list;
	}
	
	//把前端传来的json的string转换成需要的答案id的map
	public Map<Integer, Integer> stringTomap(String str)
	{
		ObjectMapper mapper = new ObjectMapper();
		Map<Integer, Integer> map=new HashMap<>();
		try {
			map=mapper.readValue(str, new TypeReference<Map<String, Integer>>(){});
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
		return map;
	}
	//返回下一题并将该题内容写入
/*	public Object getNextTopic(ExamBean exambean,int typeId,int id,int choiceId,boolean ifCheck)
	{
		id=id-1;//前端传来的id是这道题的标号，从1开始。存储在list中的题目下标从0开始，所以减一
		switch(typeId)
		{
		case 0:
				exambean.getSingleChoiceById(id).setChoiceId(choiceId);//单选题写入考生答案，以及检查标志
				exambean.getSingleChoiceById(id).setIfCheck(ifCheck);
			break;
		case 2:
				exambean.getJudgementById(id).setChoiceId(choiceId);
				exambean.getJudgementById(id).setIfCheck(ifCheck);
			break;
		default:
			System.out.println("getNextTopic error");
		}
		return getTopic(exambean,typeId, ++id);
	}*/
	//多选题返回下一题
	
	/*public Object getNextTopic(ExamBean exambean,int typeId,int id,List<Integer> choiceIdList,boolean ifCheck){
		id=id-1;
		exambean.getMultiChoiceById(id).setIfCheck(ifCheck);
		exambean.getMultiChoiceById(id).setChoiceIdList(choiceIdList);
		return getTopic(exambean, typeId, ++id);
	}
	//匹配题返回下一题
	public Object getNextTopic(ExamBean exambean, Integer typeId, Integer id, Map<Integer, Integer> choiceIdMap,
			boolean ifCheck) {
		id=id-1;
			exambean.getMatchingById(id).setIfCheck(ifCheck);
		exambean.getMatchingById(id).setChoiceIdMap(choiceIdMap);
		return getTopic(exambean, typeId, ++id);
	}
	*/
	//返回上一题并将该题内容写入
	/*public Object getLastTopic(ExamBean exambean,int typeId,int id,List<Integer> choiceId,boolean ifCheck)
	{
		id=id-1;
		switch(typeId)
		{
		case 0:
			if(choiceId.size()!=0)
				exambean.getSingleChoiceById(id).setChoiceId(choiceId.get(0));
			else
				exambean.getSingleChoiceById(id).setChoiceId(0);
			if(ifCheck==true)
				exambean.getSingleChoiceById(id).setIfCheck(true);
			else
				exambean.getSingleChoiceById(id).setIfCheck(false);
			break;
		case 1:
			exambean.getMultiChoiceById(id).setChoiceIdList(choiceId);
			if(ifCheck==true)
				exambean.getMultiChoiceById(id).setIfCheck(true);
			else
				exambean.getMultiChoiceById(id).setIfCheck(false);
			break;
		case 2:
			if(choiceId.size()!=0)
				exambean.getJudgementById(id).setChoiceId(choiceId.get(0));
			else
				exambean.getJudgementById(id).setChoiceId(0);
			if(ifCheck==true)
				exambean.getJudgementById(id).setIfCheck(true);
			else
				exambean.getJudgementById(id).setIfCheck(false);
			break;
		default:
			System.out.println("getlasttopic error");	
		}
		return getTopic(exambean,typeId, --id);
	}
	//匹配题返回上一题
	public Object getLastTopic(ExamBean exambean, Integer typeId, Integer id, Map<Integer, Integer> choiceIdMap,
			boolean ifCheck) {
		id=id-1;
		exambean.getMatchingById(id).setChoiceIdMap(choiceIdMap);
		if(ifCheck==true)
			exambean.getMatchingById(id).setIfCheck(true);
		else
			exambean.getMatchingById(id).setIfCheck(false);
		return getTopic(exambean, typeId, id);
	}*/
	//返回检查页面需要的list，下标加一为题号，0为待检查，1为已完成，2为未完成
	public List<CheckBean> getCheckList(ExamBean exambean,int typeId){
		List<CheckBean> list=new ArrayList<>();
		switch(typeId){
		case 0:
			for(int i=0;i<exambean.getSingleChoiceList().size();i++){
				if(exambean.getSingleChoiceById(i).isIfCheck()==true)
					list.add(new CheckBean(i+1,0));
				else if(exambean.getSingleChoiceById(i).getChoiceId()!=0)//是不是0还要想一想
					list.add(new CheckBean(i+1, 1));
				else
					list.add(new CheckBean(i+1, 2));
			}
			break;
		case 1:
			for(int i=0;i<exambean.getMultiChoicesList().size();i++){
				if(exambean.getMultiChoiceById(i).isIfCheck()==true)
					list.add(new CheckBean(i+1, 0));
				else if(exambean.getMultiChoiceById(i).getChoiceIdList()!=null&&
						exambean.getMultiChoiceById(i).getChoiceIdList().size()!=0)
					list.add(new CheckBean(i+1,1));
				else
					list.add(new CheckBean(i+1, 2));
			}
			break;
		case 3:
			for(int i=0;i<exambean.getMatchingList().size();i++){
				if(exambean.getMatchingById(i).isIfCheck()==true)
					list.add(new CheckBean(i+1, 0));
				else if(exambean.getMatchingById(i).getChoiceIdMap()!=null&&
						exambean.getMatchingById(i).getChoiceIdMap().size()!=0)
					list.add(new CheckBean(i+1, 1));
				else
					list.add(new CheckBean(i+1, 2));
			}
			break;
		case 2:
			for(int i=0;i<exambean.getJudgementList().size();i++)
			{
				if(exambean.getJudgementById(i).isIfCheck()==true)
					list.add(new CheckBean(i+1, 0));
				else if(exambean.getJudgementById(i).getChoiceId()!=0)
					list.add(new CheckBean(i+1, 1));
				else
					list.add(new CheckBean(i+1, 2));
			}
			break;
		case 4:
			for(int i=0;i<exambean.getShortAnswerList().size();i++)
			{
				if(exambean.getShortAnswerById(i).isIfCheck()==true)
					list.add(new CheckBean(i+1, 0));
				else if(exambean.getShortAnswerById(i).getAnswer()!=null)
					list.add(new CheckBean(i+1, 1));
				else
					list.add(new CheckBean(i+1, 2));
			}
			break;
		case 5:
			for(int i=0;i<exambean.getFillBlankList().size();i++){
				if(exambean.getFillBlankById(i).isIfCheck()==true)
					list.add(new CheckBean(i+1, 0));
				else if(exambean.getFillBlankById(i).getAnswerList()!=null&&
						exambean.getFillBlankById(i).getAnswerList().size()!=0)
					list.add(new CheckBean(i+1, 1));
				else {
					list.add(new CheckBean(i+1, 2));
				}
			}
			break;
		case 6:
			 for(int i =0; i<exambean.getMachineList().size();i++){
				 if(exambean.getMachineTestById(i).isIfCheck()==true)
					 list.add(new CheckBean(i+1, 0));
				 else if(exambean.getMachineTestById(i).getFileName()!=null)
					 list.add(new CheckBean(i+1, 1));
				 else {
					list.add(new CheckBean(i+1, 2));
				}
			 }
			 break;
		default:
			System.out.println("getchecklist error");
		}
		return list;
	}
	
	public int getTime(ExamBean exambean){
		if(exambean.getStartTime()!=0)
		{
			int time=(int) (exambean.getEXAM_TIME()-(System.currentTimeMillis()-exambean.getStartTime())/1000+exambean.getExtraTime());
			return time;
		}
		else
			return exambean.getEXAM_TIME();
	}
	
	public String getPdf(ExamBean examBean,int typeId,int id){
		switch (typeId) {
		case 4:
			return examBean.getShortAnswerById(id).getPdf();
		case 5:
			return examBean.getFillBlankById(id).getPdf();
		case 6:
			return examBean.getMachineTestById(id).getPdf();
		default:
			System.err.println("getPdf出错");
			return null;
		}
	}





}


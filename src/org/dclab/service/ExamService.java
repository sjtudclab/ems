package org.dclab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.springframework.stereotype.Service;

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
		case 2:
			return exambean.getMatchingById(0);
		case 3:
			return exambean.getJudgementById(0);
		default:
			System.out.println("typeid取值不正确："+typeId);
			return null;
		}
	}
	//由token获取exambean
	public ExamBean getExambeanByToken(UUID token)
	{
		return ExamOperator.tokenExamMap.get(token);
	}
	//根据typeid和id获取题目。
	public Object getTopic(ExamBean exambean,int typeId,int id)
	{
		switch(typeId)
		{
		case 0:
			if(id==exambean.getSingleChoiceList().size()+1||id==0)//判断是不是超出了该题型的范围
				return null;
			else
				return exambean.getSingleChoiceById(id);
		case 1:
			if(id==exambean.getMultiChoicesList().size()+1||id==0)
				return null;
			else
				return exambean.getMultiChoiceById(id);
		case 2:
			if(id==exambean.getMatchingList().size()+1||id==0)
				return null;
			else
				return exambean.getMatchingById(id);
		case 3:
			if(id==exambean.getJudgementList().size()+1||id==0)
				return null;
			else
				return exambean.getJudgementById(id);
		default:
			System.out.println("错误的typeid");
			return null;
		}
	}
	//返回下一题并将该题内容写入
	public Object getNextTopic(ExamBean exambean,int typeId,int id,List<Integer> choiceId,boolean ifCheck)
	{
		switch(typeId)
		{
		case 0:
			exambean.getSingleChoiceById(id).setChoiceId(choiceId.get(0));//单选题写入考生答案，以及检查标志
			if(ifCheck==true)
				exambean.getSingleChoiceById(id).setIfCheck(true);
/*			if(id==exambean.getSingleChoiceList().size())
				flag=true;*/
			break;
		case 1:
			exambean.getMultiChoiceById(id).setChoiceId(choiceId);
			if(ifCheck==true)
				exambean.getMultiChoiceById(id).setIfCheck(true);
			break;
		case 2:
			exambean.getMatchingById(id).setChoiceIdList(choiceId);
			if(ifCheck==true)
				exambean.getMatchingById(id).setIfCheck(true);
			break;
		case 3:
			exambean.getJudgementById(id).setChoiceId(choiceId.get(0));
			if(ifCheck==true)
				exambean.getJudgementById(id).setIfCheck(true);
			break;
		default:
				System.out.println("错误的typeId");
		}
		return getTopic(exambean,typeId, ++id);
	}
	
	//返回上一题并将该题内容写入
	public Object getLastTopic(ExamBean exambean,int typeId,int id,List<Integer> choiceId,boolean ifCheck)
	{
		switch(typeId)
		{
		case 0:
			exambean.getSingleChoiceById(id).setChoiceId(choiceId.get(0));
			if(ifCheck==true)
				exambean.getSingleChoiceById(id).setIfCheck(true);
			break;
		case 1:
			exambean.getMultiChoiceById(id).setChoiceId(choiceId);
			if(ifCheck==true)
				exambean.getMultiChoiceById(id).setIfCheck(true);
			break;
		case 2:
			exambean.getMatchingById(id).setChoiceIdList(choiceId);
			if(ifCheck==true)
				exambean.getMatchingById(id).setIfCheck(true);
			break;
		case 3:
			exambean.getJudgementById(id).setChoiceId(choiceId.get(0));
			if(ifCheck==true)
				exambean.getJudgementById(id).setIfCheck(true);
			break;
		}
		return getTopic(exambean,typeId, --id);
	}
	
	//获取需要检查的单选题的id的list
	public List<Integer> getSingleCheckList(ExamBean exambean){
		List<Integer> checkList=new ArrayList<Integer>();
		for(int i=0;i<exambean.getSingleChoiceList().size();i++)//获取单选题需要检查的题目的id的list
		{
			if(exambean.getSingleChoiceList().get(i).isIfCheck())
			{
				checkList.add(exambean.getSingleChoiceList().get(i).getId());
			}
		}
		return checkList;
	}
	//获取已作答且不需要检查的单选题的list
	public List<Integer> getSingleAnsweredList(ExamBean exambean){
		List<Integer> answeredList=new ArrayList<>();
		for(int i=0;i<exambean.getSingleChoiceList().size();i++)
		{
			if(exambean.getSingleChoiceList().get(i).getChoiceId()!=0&&exambean.getSingleChoiceList().get(i).isIfCheck()==false)
			{
				answeredList.add(exambean.getSingleChoiceList().get(i).getId());
			}
		}
		return answeredList;
	}
	//获取需要检查的多选题的id的list
	public List<Integer> getMultiCheckList(ExamBean exambean){
		List<Integer> checkList=new ArrayList<Integer>();
		for(int i=0;i<exambean.getMultiChoicesList().size();i++)//获取多选题需要检查的题目的id的list
		{
			if(exambean.getMultiChoicesList().get(i).isIfCheck())
			{
				checkList.add( exambean.getMultiChoicesList().get(i).getId());
			}
		}
		return checkList;
	}
	//获取已作答且不需要检查的多选题的list
		public List<Integer> getMultiAnsweredList(ExamBean exambean){
			List<Integer> answeredList=new ArrayList<>();
			for(int i=0;i<exambean.getMultiChoicesList().size();i++)
			{
				if(exambean.getMultiChoicesList().get(i).getChoiceId()!=null&&exambean.getMultiChoicesList().get(i).isIfCheck()==false)
				{
					answeredList.add(exambean.getMultiChoicesList().get(i).getId());
				}
			}
			return answeredList;
		}
	//获取需要检查的匹配题的id的list
	public List<Integer> getMatchCheckList(ExamBean exambean){
		List<Integer> checkList=new ArrayList<Integer>();
		for(int i=0;i<exambean.getMatchingList().size();i++)
		{
			if(exambean.getMatchingList().get(i).isIfCheck())
			{
				checkList.add(exambean.getMatchingList().get(i).getId());
			}
		}
		return checkList;
	}
	//获取已作答的且不需要检查的匹配题的list
	public List<Integer> getMatchAnsweredList(ExamBean exambean){
		List<Integer> answeredList=new ArrayList<>();
		for(int i=0;i<exambean.getMatchingList().size();i++)
		{
			if(exambean.getMatchingList().get(i).getChoiceIdList()!=null&&exambean.getMatchingList().get(i).isIfCheck()==false)
			{
				answeredList.add(exambean.getMatchingList().get(i).getId());
			}
		}
		return answeredList;
	}
	//获取需要检查的判断题的id的list
	public List<Integer> getJudgeCheckList(ExamBean exambean){
		List<Integer> checkList=new ArrayList<Integer>();
		for(int i=0;i<exambean.getJudgementList().size();i++)
		{
			if(exambean.getJudgementList().get(i).isIfCheck())
			{
				checkList.add( exambean.getJudgementList().get(i).getId());
			}
		}
		return checkList;
	}
	//获取已作答且不需要检查的判断题的list
	public List<Integer> getJudgeAnsweredList(ExamBean exambean){
		List<Integer> answeredList=new ArrayList<>();
		for(int i=0;i<exambean.getJudgementList().size();i++)
		{
			if(exambean.getJudgementList().get(i).getChoiceId()!=0&&exambean.getJudgementList().get(i).isIfCheck()==false)
			{
				answeredList.add(exambean.getJudgementList().get(i).getId());
			}
		}
		return answeredList;
	}
	//根据typeid获取检查list
	public List<Integer> getCheckList(ExamBean exambean,int typeId){
		switch(typeId)
		{
		case 0:
			return getSingleCheckList(exambean);
		case 1:
			return getMultiCheckList(exambean);
		case 2:
			return getMatchCheckList(exambean);
		case 3:
			return getJudgeCheckList(exambean);
		default:
			System.out.println("typeid错误："+typeId);
			return null;
		}
	}
	//根据typeid获取已答list
	public List<Integer> getAnsweredList(ExamBean exambean,int typeId){
		switch(typeId)
		{
		case 0:
			return getSingleAnsweredList(exambean);
		case 1:
			return getMultiAnsweredList(exambean);
		case 2:
			return getMatchAnsweredList(exambean);
		case 3:
			return getJudgeAnsweredList(exambean);
		default:
			System.out.println("typeid错误: "+typeId);
			return null;
		}
	}
	//监考操作之延时操作
	public void delay(ExamBean exambean){
		exambean.setDuration(exambean.getDuration()+100);
	}
	//监考操作之返回试卷
	public void returnToExam(ExamBean exambean){
		exambean.setFinished(false);
	}
	//监考操作之手动交卷
	public void manualAssign(ExamBean exambean){
		/*ExamBean exambean=ExamOperator.tokenExamMap.get(token);*/
		exambean.setFinished(true);
	}
	// 监考操作之强制终止
	public void forceTerminate(ExamBean exambean) {
		exambean.setAllowStart(true);
	}

	// 监考操作之允许开始
	public void allowStart(ExamBean exambean) {
		exambean.setAllowTerminate(true);
	}

	// 监考操作之允许终止
	public void allowTerminate(ExamBean exambean) {
		exambean.setAllowTerminate(true);
	}

	// 监考操作之删除试卷
	public void deleteExamInfo(ExamBean exambean) {
		exambean = null;
	}

}

package org.dclab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.dclab.User;
import org.dclab.mapping.RoomCanMapperI;
import org.dclab.model.CandidateBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.SuperBean;
import org.dclab.model.SupervisorOperator;
import org.dclab.utils.MyBatisUtil;
import org.springframework.stereotype.Service;

/**
 * @author alvis
 *监考老师相关操作
 */

@Service
public class SupervisorService {
	
/*	
	//获得对应考场的已登录的考生的座位号的list
	public List<Integer> getLoginedList(SuperBean superbean){
		List<Integer> uidSeatList=superbean.getUidSeatList();
		
		List<Integer> loginedList=new ArrayList<Integer>();//用于返回的已登录list，里面装的应该是已登录考生的座位号
		for(int i=0;i<uidSeatList.size();i++)
		{
			if(ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(uidSeatList.get(i))).isIfLogin()==true)
				loginedList.add(i+1);
		}
		return loginedList;
	}*/
	
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
		exambean.setFinished(true);
	}

	// 监考操作之允许开始
	public void allowStart(ExamBean exambean) {
		exambean.setAllowStart(true);
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

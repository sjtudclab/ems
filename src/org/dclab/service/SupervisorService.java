package org.dclab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.dclab.User;
import org.dclab.mapping.RoomCanMapperI;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.SuperBean;
import org.dclab.model.SupervisorOperator;
import org.dclab.utils.MyBatisUtil;

/**
 * @author alvis
 *监考老师相关操作
 */
public class SupervisorService {
	
	
	public List<User> getUserList(int Uid){

		SuperBean superBean=SupervisorOperator.idSuperMap.get(Uid);
		List<User> userList=superBean.getUserList();//这里的User对象可能后期修改！！！！
		return userList;
	}
	
	
	public List<Integer> getLoginedList(int id){
		/*
		 * id是考场号
		 */
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		RoomCanMapperI rMapper=sqlSession.getMapper(RoomCanMapperI.class);
		List<Integer> userList=rMapper.getUidByRoomId(id);
		List<Integer> loginedList=new ArrayList<Integer>();
		for(int i=0;i<userList.size();i++)
		{
			if(ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(userList.get(i))).isIfLogin()==true)
				loginedList.add(userList.get(i));
		}
		sqlSession.close();
		return loginedList;
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

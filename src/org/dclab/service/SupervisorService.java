package org.dclab.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	//获得对应考场的考生信息list
	public Collection<CandidateBean> getInfo(SuperBean superBean){
		for(CandidateBean cbean : superBean.getCanMap().values()){
			if(ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid()))!=null&&
					ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid())).getStartTime()==0)
				cbean.setStatus(0);
			else if(ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid()))!=null&&
					ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid())).isFinished()==true)
				cbean.setStatus(2);
			else
				cbean.setStatus(1);
		}
	return superBean.getCanMap().values();
	}
	//更换座位
	public boolean seatChange(SuperBean superBean,int Uid,Integer seatNum){
		if(superBean.getFreeSeatList().remove(seatNum)){//从空闲座位list中删去目标座位
			for(CandidateBean cbean: superBean.getCanMap().values()){//更新考生信息list
				if(cbean.getUid()==Uid)
					cbean.setSeatNum(seatNum);
			}
			return true;
		}
		else
			return false;
	}
	//监考操作之延时操作
	public void delay(List<Integer> uidList,int delayTime){//delayTime是延迟的秒数
		for(int i : uidList)
		{
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamBean examBean=ExamOperator.tokenExamMap.get(token);
			examBean.setDuration(examBean.getDuration()+delayTime);
		}
	}
	//监考操作之撤销交卷
	public void returnToExam(List<Integer> uidList){
		for(int i : uidList)
		{
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.get(token).setFinished(false);
		}
	}
	// 监考操作之强制终止
	public void forceTerminate(SuperBean superBean,List<Integer> uidList) {
		for(int i: uidList){
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.get(token).setFinished(true);//把ExamBean置为空
			superBean.getCanMap().get(i).setStatus(2);//把该考生的状态置为已交卷
		}
	}

	// 监考操作之允许开始
	public void allowStart(List<Integer> uidList) {
		for(int i: uidList){
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.get(token).setAllowStart(true);
		}
	}

	// 监考操作之允许终止
	public void allowTerminate(List<Integer> uidList) {
		for(int i: uidList){
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.get(token).setAllowTerminate(true);
		}
	}

	// 监考操作之删除试卷
	public void deleteExamInfo(SuperBean superBean,List<Integer> uidList) {
		for(int i: uidList){
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.put(token, null);//把ExamBean置为空
			superBean.getCanMap().get(i).setStatus(0);//把该考生的状态置为未登录
		}
	}
}

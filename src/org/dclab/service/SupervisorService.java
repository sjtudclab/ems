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
import org.dclab.model.SuperRespond;
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
			
			
			UUID token = ExamOperator.idTokenMap.get(cbean.getUid());
			ExamBean examBean = ExamOperator.tokenExamMap.get(token);
			
			cbean.setFinishTime(cbean.getFinishTime()+examBean.getExtraTime()*1000);
			
			if(examBean==null || examBean.isIfLogin() == false)
				cbean.setStatus(0);
			else if(examBean.isFinished())
				cbean.setStatus(2);
			else 
				cbean.setStatus(1);
		}
	return superBean.getCanMap().values();
	}
	//更换座位--+
	public SuperRespond seatChange(SuperBean superBean,int Uid,Integer seatNum){
		System.out.println(superBean);
		if(superBean.getFreeSeatList()!=null&&superBean.getFreeSeatList().remove(seatNum)==true){//从空闲座位list中删去目标座位
			superBean.getCanMap().get(Uid).setSeatNum(seatNum);
			return new SuperRespond(true);
		}
		else
			return new SuperRespond(false, "目标座位已有人");
	}
	
	
	public SuperRespond releaseSeat(SuperBean superBean,List<String> uidList){
		for(String string : uidList){
			superBean.getFreeSeatList().add(superBean.getCanMap().get(string).getSeatNum());
			superBean.getCanMap().remove(string);
			
		}
		return new SuperRespond(true);
	}
	
	//监考操作之延时操作
	public SuperRespond delay(List<String> uidList,int delayTime){//delayTime是延迟的分钟
		for(String i : uidList)
		{
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamBean examBean=ExamOperator.tokenExamMap.get(token);
			examBean.setExtraTime(examBean.getExtraTime()+delayTime*60);
		}
		return new SuperRespond(true);
	}
	//监考操作之撤销交卷
	public SuperRespond returnToExam(List<String> uidList){
		for(String i : uidList)
		{
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.get(token).setFinished(false);
		}
		return new SuperRespond(true);
	}
	// 监考操作之强制终止
	public SuperRespond forceTerminate(SuperBean superBean,List<String> uidList) {
		for(String i: uidList){
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.get(token).setFinished(true);//把ExamBean置为空
/*			superBean.getCanMap().get(i).setStatus(2);//把该考生的状态置为已交卷
*/		}
		return new SuperRespond(true);
	}

	// 监考操作之允许开始
	public SuperRespond allowStart(List<String> uidList) {
		for(String i: uidList){
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.get(token).setAllowStart(true);
		}
		return new SuperRespond(true);
	}

	// 监考操作之允许终止
	public SuperRespond allowTerminate(List<String> uidList) {
		for(String i: uidList){
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.get(token).setAllowTerminate(true);
		}
		return new SuperRespond(true);
	}

	// 监考操作之删除试卷
	public SuperRespond deleteExamInfo(SuperBean superBean,List<String> uidList) {
		for(String i: uidList){
			UUID token=ExamOperator.idTokenMap.get(i);
			ExamOperator.tokenExamMap.put(token, null);//把ExamBean置为空
/*			superBean.getCanMap().get(i).setStatus(0);//把该考生的状态置为未登录
*/		}
		return new SuperRespond(true);
	}
}

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
	//获得对应考场的考生信息list
	public List<CandidateBean> getInfo(SuperBean superBean){
		for(CandidateBean cbean : superBean.getCanList()){
			if(ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid()))!=null&&
					ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid())).getStartTime()==0)
				cbean.setStatus(0);
			else if(ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid()))!=null&&
					ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid())).isFinished()==true)
				cbean.setStatus(2);
			else
				cbean.setStatus(1);
		}
	return superBean.getCanList();
	}
	//更换座位
	public boolean seatChange(SuperBean superBean,int Uid,Integer seatNum){
		if(superBean.getFreeSeatList().remove(seatNum)){//从空闲座位list中删去目标座位
			for(CandidateBean cbean: superBean.getCanList()){//更新考生信息list
				if(cbean.getUid()==Uid)
					cbean.setSeatNum(seatNum);
			}
			return true;
		}
		else
			return false;
	}
	//监考操作之延时操作
	public void delay(ExamBean exambean){
		exambean.setDuration(exambean.getDuration()+100);
	}
	//监考操作之撤销交卷
	public void returnToExam(ExamBean exambean){
		exambean.setFinished(false);
	}
	//监考操作之手动交卷
	public void manualAssign(ExamBean exambean){
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

package org.dclab.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.common.Constants;
import org.dclab.mapping.AuthorityMapperI;
import org.dclab.mapping.RoomCanMapperI;
import org.dclab.mapping.RoomMapperI;
import org.dclab.mapping.SessionCanMapperI;
import org.dclab.mapping.SessionMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.utils.MyBatisUtil;
import org.dclab.utils.TokenGenerator;

/**
 * @author alvis
 *监考相关，主要是map
 */
public class SupervisorOperator {
	
	//监考老师的id对应一个token
	public static Map<String, UUID> idTokenMap=new HashMap<>(64);
	//监考老师的token对应一个superbean
	public static Map<UUID, SuperBean> tokenSuperMap=new HashMap<>(64);
	//从数据库加载信息装填map，以及superbean。
	public static void load(){
		
		SqlSession sqlsession=MyBatisUtil.getSqlSession();
		UserMapperI mapper=sqlsession.getMapper(UserMapperI.class);
		SessionMapperI sessionMapperI = sqlsession.getMapper(SessionMapperI.class);
		AuthorityMapperI amapper=sqlsession.getMapper(AuthorityMapperI.class);
		SessionCanMapperI sessionCanMapperI = sqlsession.getMapper(SessionCanMapperI.class);
		
		List<String> uidList=mapper.getUidByRid();//获取所有的监考老师的id的list
		System.out.println(uidList);
		for(String i:uidList){
			idTokenMap.put(i, TokenGenerator.generate());
			SuperBean superBean=new SuperBean();
			superBean.setRoomName(sessionMapperI.getRoomNameByUid(i));
			int sid= sessionMapperI.getIdByUid(i);//获得场次id
			
			Timestamp startTime = sessionMapperI.getStartTimeById(sid);
			superBean.setStartTime(startTime.getTime());
			
			List<String> uList=sessionCanMapperI.getUidListBySid(sid);//获取该考场的考生准考证号list
			Map<String, CandidateBean> map=new HashMap<>();
			for(String uid :uList){
				CandidateBean candidateBean = sessionCanMapperI.getCandidateBeanByUid(uid);
				ExamBean examBean = ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(uid));
				candidateBean.setFinishTime(superBean.getStartTime()+examBean.getEXAM_TIME()*1000);
				map.put(uid, candidateBean);
			}
			
			superBean.setName(mapper.getNmaeByUid(i));
			superBean.setCanMap(map);
			superBean.setToken(idTokenMap.get(i));
			superBean.setAuthorityList(amapper.getListByRid(1));
			superBean.setRid(1);
			superBean.setFreeSeatList(sessionCanMapperI.getFreeSeat());
			tokenSuperMap.put(idTokenMap.get(i), superBean);
			AdminBean.roomSuperBeanMap.put(sid, superBean);//给管理员装填考场号和superbean的对应map
		}
		
		sqlsession.close();
		
		Constants.CanGetRoomInfo=true;
	}
}


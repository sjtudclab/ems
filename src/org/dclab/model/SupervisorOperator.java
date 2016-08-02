package org.dclab.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.AuthorityMapperI;
import org.dclab.mapping.RoomCanMapperI;
import org.dclab.mapping.RoomMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.utils.MyBatisUtil;
import org.dclab.utils.TokenGenerator;

/**
 * @author alvis
 *监考相关，主要是map
 */
public class SupervisorOperator {
	
	//监考老师的id对应一个token
	public static Map<Integer, UUID> idTokenMap=new HashMap<>(64);
	//监考老师的token对应一个superbean
	public static Map<UUID, SuperBean> tokenSuperMap=new HashMap<>(64);
	//从数据库加载信息装填map，以及superbean。
	public static void load(){
		
		SqlSession sqlsession=MyBatisUtil.getSqlSession();
		UserMapperI mapper=sqlsession.getMapper(UserMapperI.class);
		RoomMapperI rmapper=sqlsession.getMapper(RoomMapperI.class);
		RoomCanMapperI rcmapper=sqlsession.getMapper(RoomCanMapperI.class);
		AuthorityMapperI amapper=sqlsession.getMapper(AuthorityMapperI.class);
		
		List<Integer> uidList=mapper.getUidByRid();//获取所有的监考老师的id的list
		
		for(int i:uidList){
			idTokenMap.put(i, TokenGenerator.generate());
			int roomId=rmapper.getRoomIdByUid(i);
			SuperBean superBean=new SuperBean();
			superBean.setRoomId(roomId);
			superBean.setCanList(rcmapper.getUserListByRoomId(roomId));
			for(CandidateBean cbean : superBean.getCanList()){
				if(ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid()))!=null&&
						ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid())).getStartTime()==0)
					cbean.setStatus("未登录");
				else if(ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid()))!=null&&
						ExamOperator.tokenExamMap.get(ExamOperator.idTokenMap.get(cbean.getUid())).isFinished()==true)
					cbean.setStatus("已交卷");
				else
					cbean.setStatus("已登录");
			}
			superBean.setToken(idTokenMap.get(i));
			superBean.setAuthorityList(amapper.getListByRid());
			superBean.setRid(1);
			tokenSuperMap.put(idTokenMap.get(i), superBean);
		}
		
		sqlsession.close();
	}
}


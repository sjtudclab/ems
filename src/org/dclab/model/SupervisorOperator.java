package org.dclab.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
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
		
		List<Integer> uidList=mapper.getUidByRid();
		
		for(int i:uidList){
			idTokenMap.put(i, TokenGenerator.generate());
		}
		
		sqlsession.close();
	}
}


package org.dclab.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
		
	}
}

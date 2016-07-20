package org.dclab.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author alvis
 *监考相关，主要是map
 */
public class SupervisorOperator {
	//监考老师的id对应一个superbean
	public static Map<Integer, SuperBean> idSuperMap=new HashMap<Integer, SuperBean>(64);
	//从数据库加载信息装填map，以及superbean。
	public static void load(){
		
	}
}

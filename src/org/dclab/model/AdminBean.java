package org.dclab.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdminBean {
	public static Map<String, SuperBean> roomSuperBeanMap=new HashMap<String, SuperBean>();
	public static Map<Integer, UUID> adminTokenMap=new HashMap<>();
}

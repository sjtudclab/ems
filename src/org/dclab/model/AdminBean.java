package org.dclab.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdminBean {
	public static Map<Integer, SuperBean> roomSuperBeanMap=new HashMap<Integer, SuperBean>();
	public static Map<String, UUID> adminTokenMap=new HashMap<>();
}

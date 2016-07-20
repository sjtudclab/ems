package org.dclab.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 所有考试过程中的操作都是对内存中此对象的读写
 * 主要是两个MAP
 * @author alvis
 *
 */
public class ExamOperator {
	/**
	 * 用户 id 到token 的MAP  
	 */
	public static Map<Integer, UUID> idTokenMap = new HashMap<>(64);
	
	/**
	 * 考生token 到 examBean的映射
	 */
	public static Map<UUID, ExamBean> tokenExamMap = new HashMap<>(256);
	
	/**
	 * 从数据库加载数据装填考生id与token及试卷
	 */
	public static void load(){
		
	}
	
	/**
	 * 交卷之后将考生答题信息写入数据库
	 */
	public static void persist(){
		
	}
}

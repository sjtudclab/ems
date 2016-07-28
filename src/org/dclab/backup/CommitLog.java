package org.dclab.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.dclab.model.ExamBean;

/**
 * 日志写入类
 * 1. 当用户上一次操作距离现在超过一分钟时将其试卷状态写入log
 * 2. 每个用户一个Log， 文件名为userId，内容是该用户的ExamBean序列化后的二进制形式
 * 3. 当客户端关闭连接之后，找到用户上次的token，重新生成token，链接到之前的数据
 * 4. 服务器宕机之后，从log文件中恢复，重组MAP<token, examBean>对象
 * 
 * @author zhaoz
 *
 */
public class CommitLog {
	private static String baseDir;
	static{
		baseDir = new Properties().getProperty("logDir"); //属性文件中配置log存储路径
	}

	/**
	 * 将考生考卷的状态写入log
	 * @param userId
	 * @param examBean
	 */
	public static void flush2Disk(String userId, ExamBean examBean){
		
		try {
			FileOutputStream outputStream = new FileOutputStream(baseDir+File.separator+userId);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(examBean);
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * 从commit log 中恢复所有数据
	 * @return
	 */
	public static Map<String, ExamBean> recover(){
		HashMap<String, ExamBean> examMap = new HashMap<>();
		FileInputStream inputStream;
		ObjectInputStream objectReader;
		File base = new File(baseDir);
		File[] logFiles = base.listFiles();
		for (File file : logFiles) {
			String filename = file.getName(); //need to be tested
			try {
				
				objectReader = new ObjectInputStream(new FileInputStream(baseDir+File.separator+filename));
				ExamBean examBean = (ExamBean)objectReader.readObject();
				examMap.put(filename, examBean);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return examMap;
	}
}

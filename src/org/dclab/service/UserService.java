<<<<<<< HEAD
package org.dclab.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.Subject;
import org.dclab.User;
import org.dclab.mapping.CanSubMapperI;
import org.dclab.mapping.SubjectMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.utils.MyBatisUtil;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;
/**
 * 用户信息提取
 * 主界面上用户信息显示
 * 1. login
 * 2. getUserInfo
 * 
 * @author alvis
 *
 *
 */
@Service
public class UserService {

	public Map<String,Object> login(int Uid)
	{
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		//得到UserMapperI接口的实现类对象，UserMapperI接口的实现类对象由sqlSession.getMapper(UserMapperI.class)动态构建出来
		UserMapperI mapper=sqlSession.getMapper(UserMapperI.class);
		
		User user=mapper.getByUid(Uid);

		CanSubMapperI csmapper=sqlSession.getMapper(CanSubMapperI.class);
		int sid=csmapper.getSubjectIdByUid(Uid);

		SubjectMapperI smapper=sqlSession.getMapper(SubjectMapperI.class);
		Subject subject=smapper.getById(sid);

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("name", user.getUname());
		map.put("id", user.getUid());
		map.put("cid", user.getCid());
		map.put("subject",subject.getName());
		map.put("time", subject.getDate());
		UUID token=ExamOperator.idTokenMap.get(Uid);
		map.put("token", token);
		map.put("gender", user.getGender());
		sqlSession.close();
		
		String dir=user.getPhoto();
		InputStream in=null;
		byte[] data=null;
		try{
			in=new FileInputStream(dir);
			data=new byte[in.available()];
			in.read(data);
			in.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		BASE64Encoder encoder=new BASE64Encoder();
		String photo=encoder.encode(data);
		
		map.put("photo", photo);
		return map;
	}
	
	public Map<String,Object> getUserInfo(UUID token){
		
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		
		ExamBean exambean=ExamOperator.tokenExamMap.get(token);
		int Uid=exambean.getUid();
		
		UserMapperI mapper=sqlSession.getMapper(UserMapperI.class);
		User user=mapper.getByUid(Uid);
		
		CanSubMapperI csmapper=sqlSession.getMapper(CanSubMapperI.class);
		int sid=csmapper.getSubjectIdByUid(Uid);

		SubjectMapperI smapper=sqlSession.getMapper(SubjectMapperI.class);
		String subjectName=smapper.getNameById(sid);
		
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("name",user.getUname());
		map.put("dir", user.getPhoto());
		map.put("id", user.getUid());
		map.put("cid", user.getCid());
		map.put("gender", user.getGender());
		map.put("subject", subjectName);
		sqlSession.close();
		return map;
		
	}

/*	public String getPhoto(UUID token){
		ExamBean examBean=ExamOperator.tokenExamMap.get(token);
		
	}*/
	
}
=======
package org.dclab.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.Subject;
import org.dclab.User;
import org.dclab.mapping.CanSubMapperI;
import org.dclab.mapping.RoomCanMapperI;
import org.dclab.mapping.SubjectMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.SuperBean;
import org.dclab.model.SupervisorOperator;
import org.dclab.utils.MyBatisUtil;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;
/**
 * 用户信息提取
 * 主界面上用户信息显示
 * 1. login
 * 2. getUserInfo
 * 
 * @author alvis
 *
 *
 */
@Service
public class UserService {

	public Map<String,Object> login(int Uid)
	{
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		//得到UserMapperI接口的实现类对象，UserMapperI接口的实现类对象由sqlSession.getMapper(UserMapperI.class)动态构建出来
		UserMapperI mapper=sqlSession.getMapper(UserMapperI.class);
		
		User user=mapper.getByUid(Uid);

		CanSubMapperI csmapper=sqlSession.getMapper(CanSubMapperI.class);
		int sid=csmapper.getSubjectIdByUid(Uid);

		SubjectMapperI smapper=sqlSession.getMapper(SubjectMapperI.class);
		Subject subject=smapper.getById(sid);

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("name", user.getUname());
		map.put("id", user.getUid());
		map.put("cid", user.getCid());
		map.put("subject",subject.getName());
		map.put("time", subject.getDate());
		UUID token=ExamOperator.idTokenMap.get(Uid);
		map.put("token", token);
		sqlSession.close();
		
		String dir=user.getPhoto();
		InputStream in=null;
		byte[] data=null;
		try{
			in=new FileInputStream(dir);
			data=new byte[in.available()];
			in.read(data);
			in.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		BASE64Encoder encoder=new BASE64Encoder();
		String photo=encoder.encode(data);
		
		map.put("photo", photo);
		return map;
	}
	
/*	public Map<String,Object> getUserInfo(UUID token){
		
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		
		ExamBean exambean=ExamOperator.tokenExamMap.get(token);
		int Uid=exambean.getUid();
		
		UserMapperI mapper=sqlSession.getMapper(UserMapperI.class);
		User user=mapper.getByUid(Uid);
		
		CanSubMapperI csmapper=sqlSession.getMapper(CanSubMapperI.class);
		int sid=csmapper.getSubjectIdByUid(Uid);

		SubjectMapperI smapper=sqlSession.getMapper(SubjectMapperI.class);
		String subjectName=smapper.getNameById(sid);
		
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("name",user.getUname());
		map.put("dir", user.getPhoto());
		map.put("id", user.getUid());
		map.put("cid", user.getCid());
		map.put("gender", user.getGender());
		map.put("subject", subjectName);
		sqlSession.close();
		return map;
		
	}*/

}
>>>>>>> 524cbd5ab31ddccd4df2c17be720c4f70f12a756

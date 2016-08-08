package org.dclab.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.Session;
import org.dclab.User;
import org.dclab.mapping.CanSubMapperI;
import org.dclab.mapping.SessionMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.ExamOperator;
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

	public Object login(int Uid)
	{
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		//得到UserMapperI接口的实现类对象，UserMapperI接口的实现类对象由sqlSession.getMapper(UserMapperI.class)动态构建出来
		UserMapperI mapper=sqlSession.getMapper(UserMapperI.class);
		User user=mapper.getByUid(Uid);
		if(user==null)
			return null;
		Map<String,Object> map=new HashMap<String,Object>();
		switch(user.getRid()){
		case 0:
			CanSubMapperI csmapper=sqlSession.getMapper(CanSubMapperI.class);
			SessionMapperI smapper=sqlSession.getMapper(SessionMapperI.class);
			int sessionId=csmapper.getSessionIdByUid(Uid);
			UUID token=ExamOperator.idTokenMap.get(Uid);
			Session session=smapper.getById(sessionId);
			sqlSession.close();
			if(!ExamOperator.tokenExamMap.get(token).isFinished()){//检测考生ExamBean中的考试结束标志是否为true
				if(ExamOperator.tokenExamMap.get(token).isAllowStart()==true)//检测该考生是否可在任意时间登录
				{
					map.put("name", user.getUname());
					map.put("id", user.getUid());
					map.put("cid", user.getCid());
					map.put("subject",session.getName());
					map.put("time", session.getStartTime());
					map.put("Rid",user.getRid());
					map.put("gender", user.getGender());
					map.put("token", token);
				
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
				else{
					if(System.currentTimeMillis()-session.getStartTime().getTime()<1800*1000)//1800代表半小时，以后可能会改为由数据库中取值
					{
						map.put("name", user.getUname());
						map.put("id", user.getUid());
						map.put("cid", user.getCid());
						map.put("subject",session.getName());
						map.put("time", session.getStartTime());
						map.put("Rid",user.getRid());
						map.put("gender", user.getGender());
						map.put("token", token);
					
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
					else
						return null;
				}
			}
			else
				return null;
		case 1:
			UUID token1=SupervisorOperator.idTokenMap.get(user.getUid());
			Map<String, Object> map1=new HashMap<>();
			map1.put("token", token1);
			map1.put("authorityList", SupervisorOperator.tokenSuperMap.get(token1).getAuthorityList());
			map1.put("roomId", SupervisorOperator.tokenSuperMap.get(token1).getRoomId());
			map1.put("Rid", SupervisorOperator.tokenSuperMap.get(token1).getRid());
			sqlSession.close();
			return map1;
		default:
			return null;
		}

		
	}

}


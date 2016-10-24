package org.dclab.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.Session;
import org.dclab.User;
import org.dclab.common.Constants;
import org.dclab.mapping.AuthorityMapperI;
import org.dclab.mapping.CanSubMapperI;
import org.dclab.mapping.SessionCanMapperI;
import org.dclab.mapping.SessionMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.AdminBean;
import org.dclab.model.ExamBean;
import org.dclab.model.ExamOperator;
import org.dclab.model.SuperBean;
import org.dclab.model.SuperRespond;
import org.dclab.model.SupervisorOperator;
import org.dclab.utils.MyBatisUtil;
import org.dclab.utils.TokenGenerator;
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
	private static final String imgPath="EMSdata\\img\\";
	public Object login(String Uid)
	{
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		//得到UserMapperI接口的实现类对象，UserMapperI接口的实现类对象由sqlSession.getMapper(UserMapperI.class)动态构建出来
		UserMapperI mapper=sqlSession.getMapper(UserMapperI.class);
		String statement = "org.dclab.mapping.paperMapper.getSubName";
		String statement1 = "org.dclab.mapping.paperMapper.getLatest";
		SessionCanMapperI sessionCanMapperI = sqlSession.getMapper(SessionCanMapperI.class);
		SessionMapperI sessionMapperI = sqlSession.getMapper(SessionMapperI.class);
		
		User user=mapper.getByUid(Uid);
		if(user==null)
			return new SuperRespond(false, "错误的用户名");
		Map<String,Object> map=new HashMap<String,Object>();
		switch(user.getRid()){
		case 0:
			

			UUID token=ExamOperator.idTokenMap.get(Uid);
			
			ExamBean examBean=ExamOperator.tokenExamMap.get(token);
			if(examBean==null)
				return new SuperRespond(false, "考试尚未开始，不允许登录");
			
			int sid = sessionCanMapperI.getSidByUid(user.getUid());
			Timestamp startTime = sessionMapperI.getStartTimeById(sid);
			int paperId= examBean.getPaperId();
			int latestLogin = sqlSession.selectOne(statement1, paperId);
			if(!examBean.isFinished()){//检测考生ExamBean中的考试结束标志是否为true
				
				if(examBean.isAllowStart()==true)//检测该考生是否可在任意时间登录
				{
					System.out.println("进入返回map1的分支");
					map.put("name", user.getUname());
					map.put("id", user.getUid());
					map.put("cid", user.getCid());
					map.put("subject",sqlSession.selectOne(statement, paperId));
					map.put("time", startTime);
					map.put("Rid",user.getRid());
					map.put("gender", user.getGender());
					map.put("token", token);
				
					String dir=Constants.photoDir+File.separator+user.getPhoto();
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
					
					examBean.setIfLogin(true);
					
					return map;
				}
				else{
					if(System.currentTimeMillis()-startTime.getTime()<latestLogin*1000)//1800代表半小时，以后可能会改为由数据库中取值
					{
						System.out.println("进入返回map2的分支");
						map.put("name", user.getUname());
						map.put("id", user.getUid());
						map.put("cid", user.getCid());
						map.put("subject",sqlSession.selectOne(statement, paperId));
						map.put("time", startTime);
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
						
						examBean.setIfLogin(true);
						
						return map;
					}
					else
						return new SuperRespond(false, "已经超过登录时间");
				}
			}
			else
				return new SuperRespond(false, "已经交卷");
		case 1:
			if(sessionMapperI.getStartFlagByUid(user.getUid())!=1){
				return new SuperRespond(false, "尚未开考");
			}
			else{
				UUID token1=SupervisorOperator.idTokenMap.get(user.getUid());
				SuperBean superBean=SupervisorOperator.tokenSuperMap.get(token1);
				if(superBean==null)
					return new SuperRespond(false, "管理员尚未装载，稍等");
				superBean.setSign(1);
				map.put("token", token1);
				map.put("authorityList", SupervisorOperator.tokenSuperMap.get(token1).getAuthorityList());
				map.put("roomId", SupervisorOperator.tokenSuperMap.get(token1).getRoomName());
				map.put("Rid", SupervisorOperator.tokenSuperMap.get(token1).getRid());
				sqlSession.close();
				return map;
			}
		case 2:
			UUID token2;
			if(AdminBean.adminTokenMap.containsKey(Uid))
				token2 = AdminBean.adminTokenMap.get(Uid);
			else {
				token2=TokenGenerator.generate();
				AdminBean.adminTokenMap.put(Uid, token2);
			}

			map.put("Rid",2);
			AuthorityMapperI amapper=sqlSession.getMapper(AuthorityMapperI.class);
			map.put("token", token2);
			map.put("authorityList", amapper.getListByRid(2));
			sqlSession.close();
			return map;
		default:
			return null;
		}

		
	}

}


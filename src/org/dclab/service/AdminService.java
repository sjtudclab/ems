package org.dclab.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.WriteAbortedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.CorrectAnswerMapperI;
import org.dclab.mapping.RoomMapperI;
import org.dclab.mapping.SubTypeMapperI;
import org.dclab.mapping.SubjectMapperI;
import org.dclab.mapping.TopicMapperI;
import org.dclab.model.ChoicesBean;
import org.dclab.model.RoomInfoBean;
import org.dclab.model.SuperRespond;
import org.dclab.model.SupervisorOperator;
import org.dclab.model.TopicBean;
import org.dclab.utils.MyBatisUtil;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Service;

import com.sun.corba.se.spi.ior.Writeable;

/**
 * @author alvis
 *管理员的一些操作
 */

@Service
public class AdminService {
	public static int currentSubjectId=0;
	
	public SuperRespond subjectAdd(String name,int duration,int earliestSubmit,int latestLogin,Map<Integer, String> map){
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		SubjectMapperI smapper=sqlSession.getMapper(SubjectMapperI.class);
		//把分钟转化为妙
		duration=duration*60;
		earliestSubmit=earliestSubmit*60;
		latestLogin=latestLogin*60;
		
		
		int sign1=smapper.add(name, duration, earliestSubmit, latestLogin);
		sqlSession.commit();//没有这个语句就算上条语句执行成功数据库也不会更新的兄弟
		if(sign1!=1)
			return new SuperRespond(false, "添加科目失败");
		
		try {
			smapper.getSubIdByName(name);
		} catch (org.apache.ibatis.exceptions.TooManyResultsException e) {
			// TODO: handle exception
			return new SuperRespond(false, "数据库中已有相同科目");
		}
		int subId=smapper.getSubIdByName(name);
		currentSubjectId=subId;
		
		SubTypeMapperI stmapper=sqlSession.getMapper(SubTypeMapperI.class);
		
		for(int typeId : map.keySet())
		{
			String points=map.get(typeId);
			int sign2=stmapper.add(typeId, subId, points);
			sqlSession.commit();
			if(sign2!=1)
				return new SuperRespond(false,"添加科目题型关联失败");
		}

		sqlSession.close();
		return new SuperRespond(true);
	}
	
	public SuperRespond TopicAdd(String content,Map<Integer, String> choice,List<Integer> answer,int typeId){
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.topicMapper.add";
		TopicBean topicBean=new TopicBean(content, typeId, currentSubjectId);
		sqlSession.insert(statement, topicBean);
		sqlSession.commit();
		
		int topicId=topicBean.getId();
		String choiceId=new String();//这个list存放正确选项的数据库id
		for(int str : choice.keySet())
		{
			ChoicesBean choicesBean=new ChoicesBean();
			choicesBean.setContent(choice.get(str));
			choicesBean.setTopicId(topicId);
			String statement1 = "org.dclab.mapping.choiceMapper.add";
			sqlSession.insert(statement1, choicesBean);
			sqlSession.commit();
			if(answer.contains(str))
			{
				if(choiceId.isEmpty())
					choiceId=String.valueOf(choicesBean.getChoiceId());
				else
					choiceId=choiceId+","+String.valueOf(choicesBean.getChoiceId());
			}
		}
		
		SubTypeMapperI stmapper=sqlSession.getMapper(SubTypeMapperI.class);
		
		String points=stmapper.getPointsByType(typeId,currentSubjectId);
		
		CorrectAnswerMapperI camapper=sqlSession.getMapper(CorrectAnswerMapperI.class);
		
		camapper.add(topicId, choiceId, points);
		sqlSession.commit();
		
		sqlSession.close();
		return new SuperRespond(true);
	}
	
	public List<RoomInfoBean> getRoomInfo()
	{
		SqlSession sqlSession=MyBatisUtil.getSqlSession();
		RoomMapperI rmapper=sqlSession.getMapper(RoomMapperI.class);
		
		List<RoomInfoBean> list=rmapper.getRoomInfo();
		
		for(RoomInfoBean bean : list){
			UUID token=SupervisorOperator.idTokenMap.get(bean.getUid());
			bean.setSupervisor(SupervisorOperator.tokenSuperMap.get(token).getName());
			
			//检测监考老师是否登录,可以考虑删除，关联的位置有superbean，userservice登录时
			if(SupervisorOperator.tokenSuperMap.get(token).getSign()==1)
				bean.setStatus(1);
			else
				bean.setStatus(0);
			
		}
		sqlSession.close();
		return list;
	}
	
	


	
	
}

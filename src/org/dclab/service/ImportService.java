package org.dclab.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.dclab.model.SubjectRow;
import org.dclab.model.TopicRow;
import org.dclab.utils.MyBatisUtil;

public class ImportService {
	
	
	//返回paperId
	public int importSubject(SubjectRow subjectRow){
		
		
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.paperMapper.add";
		if(sqlSession.insert(statement, subjectRow)!=1)//????????注意可能会出错
			System.err.println("插入数据库表paper失败");
		sqlSession.commit();
		sqlSession.close();
		
		return subjectRow.getPaperId();
		
	}
	
	public boolean importTopic(List<TopicRow> topicList){
		
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.topicMapper.addTopic";
		
		switch (topicList.get(0).getTYPE()) {
		case 0:
		case 2:
			for(TopicRow topicRow : topicList){
				sqlSession.insert(statement, topicRow);//先插入题干并获得topicId
				sqlSession.commit();

			}
			
			break;

		default:
			break;
		}
		
		return false;
	}
}

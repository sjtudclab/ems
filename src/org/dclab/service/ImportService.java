package org.dclab.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.dclab.model.SubjectRow;
import org.dclab.model.TopicRow;
import org.dclab.utils.MyBatisUtil;

public class ImportService {
	
	
	
	public int importSubject(SubjectRow subjectRow){
		
		
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.topicMapper.add";
		sqlSession.insert(statement, subjectRow);
		sqlSession.commit();
		
		
		return subjectRow.getPaperId();
		
	}
	
	public boolean importTopic(List<TopicRow> topicList){
		return false;
	}
}

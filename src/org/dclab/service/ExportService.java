package org.dclab.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.SessionCanMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.ScoreCollectBean;
import org.dclab.utils.MyBatisUtil;

/**
 * @author alvis
 *getScoreCollect返回导出成绩汇总所需的数据的list
 */
public class ExportService {
	
	
	public List<ScoreCollectBean> getScoreCollect(int sid)//sid是场次id
	{	
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		SessionCanMapperI sessionCanMapperI = sqlSession.getMapper(SessionCanMapperI.class);
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		String statement = "org.dclab.mapping.paperMapper.getSubName";
		
		List<String> uidList=new ArrayList<String>();
		uidList=sessionCanMapperI.getUidListBySid(sid);
		
		if(uidList==null||uidList.size()==0)
		{	
			sqlSession.close();
			return null;
		}
		
		List<ScoreCollectBean> list = new ArrayList<>();
		
		for(String str : uidList)
		{
			
			int mark = 0, paperId = 0;
			
			try {
				mark = userMapperI.getMarkByUid(str);//可能会影响性能，因为数据库查了两次
				paperId = userMapperI.getPaperIdByUid(str);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				continue;
			}

			
			String subName = sqlSession.selectOne(statement, paperId);
			
			ScoreCollectBean scoreCollectBean = new ScoreCollectBean(str, subName, mark);
			list.add(scoreCollectBean);
			
		}
		sqlSession.close();
		return list;
		
	}
	
	public static void main(String[] args){
		
		ExportService exportService = new ExportService();
		List<ScoreCollectBean> list = exportService.getScoreCollect(6);
		
		System.out.println(list);
	}
	
	
}

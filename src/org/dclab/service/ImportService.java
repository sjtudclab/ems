package org.dclab.service;

import java.nio.channels.SeekableByteChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.ChoiceMapperI;
import org.dclab.mapping.MatchItemMapperI;
import org.dclab.mapping.SessionCanMapperI;
import org.dclab.mapping.TopicMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.CandidatePaperRelationRow;
import org.dclab.model.CandidateRoomRelationRow;
import org.dclab.model.ChoicesBean;
import org.dclab.model.FillBlankRow;
import org.dclab.model.JudgementRow;
import org.dclab.model.MachineTestRow;
import org.dclab.model.MatchingRow;
import org.dclab.model.MultiChoicesRow;
import org.dclab.model.SessionBean;
import org.dclab.model.ShortAnswerRow;
import org.dclab.model.SingleChoiceRow;
import org.dclab.model.SubjectRow;
import org.dclab.model.TopicRow;
import org.dclab.utils.MyBatisUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.sun.corba.se.impl.protocol.giopmsgheaders.ReplyMessage_1_2;

import org.dclab.common.Constants;
@Service
public class ImportService {
	private static final String right = "对";
	private static final String error = "错";
	
	//返回paperId
	public int importSubject(SubjectRow subjectRow){
		
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.paperMapper.add";
		subjectRow.setDuration(subjectRow.getDuration()*60);
		subjectRow.setEarliestSubmit(subjectRow.getEarliestSubmit()*60);
		subjectRow.setLatestLogin(subjectRow.getLatestLogin()*60);
		if(sqlSession.insert(statement, subjectRow)!=1)
			System.err.println("插入数据库表paper失败");
		sqlSession.commit();
		sqlSession.close();
		/*subjectRow.setPaperId(123);//test
		System.out.println(subjectRow);*/
		System.out.println("subject row 是"+subjectRow);
		return subjectRow.getPaperId();
		
	}
	
	public boolean importTopic(List<TopicRow> topicList){
		System.out.println("size: "+topicList.size()+"  "+topicList);
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.topicMapper.addTopic";
		String statement1 = "org.dclab.mapping.choiceMapper.add";
		String statement2 = "org.dclab.mapping.topicMapper.addSFM";
		String statement3 = "org.dclab.mapping.choiceMapper.addJudge";
		TopicMapperI topicMapperI = sqlSession.getMapper(TopicMapperI.class);
		MatchItemMapperI matchItemMapperI = sqlSession.getMapper(MatchItemMapperI.class);
		ChoiceMapperI choiceMapperI = sqlSession.getMapper(ChoiceMapperI.class);
		
		//插入选择题的选项
		ChoicesBean choicesBean1 = new ChoicesBean(right);
		if(sqlSession.insert(statement3, choicesBean1)!=1)
			System.err.println("插入choice失败");
		sqlSession.commit();
		Constants.JUDGEMENT_TRUE = choicesBean1.getChoiceId();
		
		choicesBean1.setContent(error);
		if(sqlSession.insert(statement3, choicesBean1)!=1)
			System.err.println("插入choice失败");
		sqlSession.commit();
		Constants.JUDGEMENT_FALSE = choicesBean1.getChoiceId();
		
		
		switch (topicList.get(0).getTYPE()) {
		case Constants.SINGLE_CHOICE:
			for(TopicRow topicRow : topicList){
				if(sqlSession.insert(statement, topicRow)!=1)//先插入题干并获得topicId
					System.err.println("插入数据库表topic失败");
				sqlSession.commit();
	
				int topicId=topicRow.getId();
				
				SingleChoiceRow singleChoiceRow=(SingleChoiceRow)topicRow;
				
				String correctAnswer="";
				int index=singleChoiceRow.getCorrectAnswerIndex()-1;
				//开始插入选项
				for(int i=0;i<singleChoiceRow.getChoiceList().size();i++)
				{
					ChoicesBean choicesBean = new ChoicesBean(singleChoiceRow.getChoiceList().get(i), topicId);
					if(sqlSession.insert(statement1, choicesBean)!=1)
						System.err.println("插入数据库表choice失败");
					sqlSession.commit();
					
					if(i==index)//如果传过来的正确答案下标和i相等，就转换correAnswer
						correctAnswer=String.valueOf(choicesBean.getChoiceId());
				}
				//update topic 表，更新points和正确答案
				String points=String.valueOf(singleChoiceRow.getFullMark());
				if(topicMapperI.update(points, correctAnswer, topicId)!=1)
					System.err.println("更新数据库表topic失败");
				sqlSession.commit();

			}
			break;
		case Constants.MULTI_CHOICES:
			for(TopicRow topicRow : topicList){
				if(sqlSession.insert(statement, topicRow)!=1)//先插入题干并获得topicId
					System.err.println("插入数据库表topic失败");
				sqlSession.commit();
				
				int topicId=topicRow.getId();
				
				MultiChoicesRow multiChoicesRow = (MultiChoicesRow)topicRow;
				
				String correctAnswer="";
				
				String[] index = multiChoicesRow.getCorrectAnswerIndices().trim().split(",");

				//开始插入选项
				for(int i=0;i<multiChoicesRow.getChoiceList().size();i++){
					ChoicesBean choicesBean = new ChoicesBean(multiChoicesRow.getChoiceList().get(i), topicId);
					if(sqlSession.insert(statement1, choicesBean)!=1)
						System.err.println("插入数据库表choice失败");
					sqlSession.commit();
					
					if(Arrays.asList(index).contains(String.valueOf(i+1)))
						correctAnswer=correctAnswer+choicesBean.getChoiceId()+",";
				}
				
				String points = multiChoicesRow.getFullMark()+","+multiChoicesRow.getHalfMark();
				if(topicMapperI.update(points, correctAnswer, topicId)!=1)
					System.err.println("更新数据库表topic失败");
				sqlSession.commit();
				
			}
			break;
		case Constants.JUDGEMENT:
			for(TopicRow topicRow : topicList){
				if(sqlSession.insert(statement, topicRow)!=1)//先插入题干并获得topicId
					System.err.println("插入数据库表topic失败");
				sqlSession.commit();
				int topicId=topicRow.getId();
				
				JudgementRow judgementRow = (JudgementRow)topicRow;
				String correctAnswer = "";
				
				if(judgementRow.getCorrectAnswer()==1)
					correctAnswer=String.valueOf(Constants.JUDGEMENT_TRUE);
				else
					correctAnswer=String.valueOf(Constants.JUDGEMENT_FALSE);
				
				String points = judgementRow.getFullMark()+"";
				if(topicMapperI.update(points, correctAnswer, topicId)!=1)
					System.err.println("更新数据库表topic失败");
				sqlSession.commit();
			}
			break;
		case Constants.MATCHING:
			for(TopicRow topicRow : topicList){
				if(sqlSession.insert(statement, topicRow)!=1)//先插入题干并获得topicId
					System.err.println("插入数据库表topic失败");
				sqlSession.commit();
				int topicId=topicRow.getId();
				
				MatchingRow matchingRow = (MatchingRow) topicRow;
				String correctAnswer = "";
				
				String[] index = matchingRow.getCorrectAnswerIndices().trim().split(",");
				
				for(String str : matchingRow.getItemList()){//插入item
					if(matchItemMapperI.addItem(matchingRow.getContent(),topicId)!=1)
						System.err.println("插入matchitem失败");
					sqlSession.commit();
				}
				
				List<String> list=Arrays.asList(index);
				
				for(int i=0;i<matchingRow.getChoiceList().size();i++){//插入选项
					ChoicesBean choicesBean = new ChoicesBean(matchingRow.getChoiceList().get(i), topicId);
					if(sqlSession.insert(statement1, choicesBean)!=1)
						System.err.println("插入数据库表choice失败");
					sqlSession.commit();
					
					if(list.contains(String.valueOf(i+1)))
					{
						int index1=list.indexOf(String.valueOf(i+1));
						list.set(index1, String.valueOf(i+1));
					}
				}
				
				for(int i=0;i<list.size();i++)
				{
					correctAnswer=correctAnswer+list.get(i)+",";
				}
				
				String points=String.valueOf(matchingRow.getFullMark());
				if(topicMapperI.update(points, correctAnswer, topicId)!=1)
					System.err.println("更新数据库表topic失败");
				sqlSession.commit();
				
			}
			break;
		case Constants.SHORT_ANSWER:
			for(TopicRow topicRow : topicList){
				if(sqlSession.insert(statement, topicRow)!=1)
					System.err.println("插入数据库表topic失败");
				sqlSession.commit();
				
				int topicId = topicRow.getId();
				ShortAnswerRow shortAnswerRow = (ShortAnswerRow) topicRow;
				
				String points = String.valueOf(shortAnswerRow.getFullMark());
				if(topicMapperI.updateSFM(points, shortAnswerRow.getCorrectAnswer(), shortAnswerRow.getPdf(), topicId)!=1)
					System.err.println("更新数据库表topic失败");
				sqlSession.commit();
			}
			break;
		case Constants.FILL_BLANK:
			for(TopicRow topicRow : topicList){
				if(sqlSession.insert(statement,topicRow)!=1)
					System.err.println("插入数据库表topic失败");
				sqlSession.commit();
				int topicId=topicRow.getId();
				
				FillBlankRow fillBlankRow = (FillBlankRow) topicRow;
				
				for(int i=0;i<fillBlankRow.getBlankNum();i++)
				{
					if(choiceMapperI.addGapChoice(topicId)!=1)
						System.err.println("插入数据库表choice失败");
					sqlSession.commit();
				}
				String points=String.valueOf(fillBlankRow.getFullMark());
				
				if(topicMapperI.updateSFM(points, fillBlankRow.getCorrectAnswer(),fillBlankRow.getPdf(), topicId)!=1)
					System.err.println("更新数据库表topic失败");	
				sqlSession.commit();
			}
			break;
		case Constants.MACHINE_TEST:
			for(TopicRow topicRow : topicList){
				if(sqlSession.insert(statement, topicRow)!=1)
					System.err.println("插入数据库表topic失败");
				sqlSession.commit();
				
				int topicId = topicRow.getId();
				
				MachineTestRow machineTestRow = (MachineTestRow) topicRow;
				
				String points=String.valueOf(machineTestRow.getFullMark());
				String correctAnswer = machineTestRow.getCorrectAnswerFile();
				
				if(topicMapperI.updateSFM(points, correctAnswer,machineTestRow.getPdf(), topicId)!=1)
					System.err.println("更新数据库表topic失败");	
				sqlSession.commit();
			}
			break;
		default:
			break;
		}
		sqlSession.close();
		return true;
	}
	
	
	public boolean importCandidatePaper(List<CandidatePaperRelationRow> list){
		System.out.println("size: "+list.size()+"  "+list);
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		for(CandidatePaperRelationRow row : list)
		{
			if(userMapperI.addUser(row)!=1)
			{
				System.err.println("插入user表失败");
				sqlSession.close();
				return false;
			}
			sqlSession.commit();
		}
		sqlSession.close();
		return true;
	}
	
	public boolean importCandidateRoom(List<CandidateRoomRelationRow> list){
		System.out.println("size: "+list.size()+"  "+list);
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.sessionMapper.add";
		SessionCanMapperI sessionCanMapperI = sqlSession.getMapper(SessionCanMapperI.class);
		UserMapperI userMapperI = sqlSession.getMapper(UserMapperI.class);
		
		Map<String  , Integer> unique = new HashMap<>();
		
		List<String> superUserList = userMapperI.getUidByRid();
		
		for(CandidateRoomRelationRow row : list){
			int sid;
			String str = row.getRoomName()+row.getStartTime().toString();
			if(unique.containsKey(str))//检测是否已插入过该场次
				sid=unique.get(str);
			else
			{
				SessionBean sessionBean = new SessionBean(row.getRoomName(), row.getStartTime(),superUserList.remove(0));
				if(sqlSession.insert(statement, sessionBean)!=1){
					System.err.println("插入session表失败");
					sqlSession.close();
					return false;
				}
				sqlSession.commit();//插入场次
				sid=sessionBean.getId();
			}
			
			if(sessionCanMapperI.addSessionCan(sid, row.getSeatNum(), row.getIp(), row.getUid())!=1)
			{
				System.err.println("插入session_candidate表失败");
				sqlSession.close();
				return false;
			}
			sqlSession.commit();
		}
		sqlSession.close(); 
		return true;
	}
	
	
	public static void main (String [] args) {
		ShortAnswerRow shortAnswerRow = new ShortAnswerRow(26);
		shortAnswerRow.setContent("test");
		shortAnswerRow.setCorrectAnswer("success");
		shortAnswerRow.setPdf("pdf");
		shortAnswerRow.setFullMark(5);
		shortAnswerRow.setTYPE(Constants.SHORT_ANSWER);
		
		List<TopicRow> list = new ArrayList<>();
		list.add(shortAnswerRow);
		
		ImportService importService = new ImportService();
		importService.importTopic(list);
		
	}
}

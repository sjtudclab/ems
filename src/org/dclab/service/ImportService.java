package org.dclab.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.dclab.mapping.ChoiceMapperI;
import org.dclab.mapping.MatchItemMapperI;
import org.dclab.mapping.TopicMapperI;
import org.dclab.model.ChoicesBean;
import org.dclab.model.FillBlankRow;
import org.dclab.model.JudgementRow;
import org.dclab.model.MachineTestRow;
import org.dclab.model.MatchingRow;
import org.dclab.model.MultiChoicesRow;
import org.dclab.model.ShortAnswerRow;
import org.dclab.model.SingleChoiceRow;
import org.dclab.model.SubjectRow;
import org.dclab.model.TopicRow;
import org.dclab.utils.MyBatisUtil;
import org.dclab.common.Constants;

public class ImportService {
	
	
	//返回paperId
	public int importSubject(SubjectRow subjectRow){
		
		
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.paperMapper.add";
		if(sqlSession.insert(statement, subjectRow)!=1)
			System.err.println("插入数据库表paper失败");
		sqlSession.commit();
		sqlSession.close();
		
		return subjectRow.getPaperId();
		
	}
	
	public boolean importTopic(List<TopicRow> topicList){
		
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		String statement = "org.dclab.mapping.topicMapper.addTopic";
		String statement1 = "org.dclab.mapping.choiceMapper.add";
		TopicMapperI topicMapperI = sqlSession.getMapper(TopicMapperI.class);
		MatchItemMapperI matchItemMapperI = sqlSession.getMapper(MatchItemMapperI.class);
		ChoiceMapperI choiceMapperI = sqlSession.getMapper(ChoiceMapperI.class);
		
		switch (topicList.get(0).getTYPE()) {
		case Constants.SINGLE_CHOICE:
			for(TopicRow topicRow : topicList){
				if(sqlSession.insert(statement, topicRow)!=1)//先插入题干并获得topicId
					System.err.println("插入数据库表topic失败");
				sqlSession.commit();
				
				int topicId=topicRow.getId();
				
				SingleChoiceRow singleChoiceRow=(SingleChoiceRow)topicRow;
				
				String correctAnswer="";
				//开始插入选项
				for(int i=0;i<singleChoiceRow.getChoiceList().size();i++)
				{
					ChoicesBean choicesBean = new ChoicesBean(singleChoiceRow.getChoiceList().get(i), topicId);
					if(sqlSession.insert(statement1, choicesBean)!=1)
						System.err.println("插入数据库表choice失败");
					sqlSession.commit();
					
					if(i==singleChoiceRow.getCorrectAnswerIndex())//如果传过来的正确答案下标和i相等，就转换correAnswer
						correctAnswer=String.valueOf(choicesBean.getChoiceId());
				}
				//update topic 表，更新points和正确答案
				String points=String.valueOf(singleChoiceRow.getFullMark());
				if(topicMapperI.update(points, correctAnswer, topicId)!=1)
					System.err.println("更新数据库表topic失败");
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
					
					if(Arrays.asList(index).contains(String.valueOf(i)))
						correctAnswer=correctAnswer+choicesBean.getChoiceId()+",";
				}
				
				String points = multiChoicesRow.getFullMark()+","+multiChoicesRow.getHalfMark();
				if(topicMapperI.update(points, correctAnswer, topicId)!=1)
					System.err.println("更新数据库表topic失败");
				
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
				
				for(int i=0;i<matchingRow.getChoiceList().size();i++){//插入选项
					ChoicesBean choicesBean = new ChoicesBean(matchingRow.getChoiceList().get(i), topicId);
					if(sqlSession.insert(statement1, choicesBean)!=1)
						System.err.println("插入数据库表choice失败");
					sqlSession.commit();
					
					if(Arrays.asList(index).contains(String.valueOf(i)))
						correctAnswer=correctAnswer+choicesBean.getChoiceId()+",";
				}
				String points=String.valueOf(matchingRow.getFullMark());
				if(topicMapperI.update(points, correctAnswer, topicId)!=1)
					System.err.println("更新数据库表topic失败");
				
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
				if(topicMapperI.update(points, shortAnswerRow.getCorrectAnswer(), topicId)!=1)
					System.err.println("更新数据库表topic失败");
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
				
				if(topicMapperI.update(points, fillBlankRow.getCorrectAnswer(), topicId)!=1)
					System.err.println("更新数据库表topic失败");	
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
				
				if(topicMapperI.update(points, correctAnswer, topicId)!=1)
					System.err.println("更新数据库表topic失败");	
			}
			break;
		default:
			break;
		}
		sqlSession.close();
		return true;
	}
	
	
	public static void main (String [] args) {
		SingleChoiceRow s1=new SingleChoiceRow();
		s1.setNumber(1);
		s1.setTYPE(Constants.SINGLE_CHOICE);
		s1.setContent("测试题目导入1");
		List<String> list=new ArrayList<>();
		list.add("12312312");
		list.add("jdjsjdjdjd");
		s1.setChoiceList(list);
		s1.setChoiceNum(1);
		s1.setPaperId(123);
		
		MultiChoicesRow m1= new MultiChoicesRow();
		
		SingleChoiceRow s2=new SingleChoiceRow();
		s2.setNumber(1);
		s2.setTYPE(Constants.SINGLE_CHOICE);
		s2.setContent("测试题目导入2");
		List<String> list1=new ArrayList<>();
		list1.add("12312312sdas");
		list1.add("jdjsjdjdjd1231");
		s2.setChoiceList(list1);
		s2.setChoiceNum(1);
		s2.setPaperId(123);
		
		List<TopicRow> list2=new ArrayList<>();
		list2.add(s1);
		list2.add(s2);
		
		ImportService importService= new ImportService();
		boolean flag=importService.importTopic(list2);
		System.out.println(flag);
	}
}

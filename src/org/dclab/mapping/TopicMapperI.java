package org.dclab.mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.dclab.model.ContentBean;
import org.dclab.model.CorrectAnswerBean;
import org.dclab.model.FillBlankBean;
import org.dclab.model.JudgementBean;
import org.dclab.model.MachineTestBean;
import org.dclab.model.MatchingBean;
import org.dclab.model.MultiChoicesBean;
import org.dclab.model.SingleChoiceBean;
import org.dclab.model.TopicBeanExport;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.dclab.model.ShortAnswerBean;
import org.dclab.model.ShortAnswerRow;

public interface TopicMapperI {
	
	@Select("SELECT id,content,img,audio,video FROM `topic` WHERE typeId=0 AND subjectId=#{id} LIMIT 5")
	public ArrayList<SingleChoiceBean> getSingleBeanBySubId(int id);//获取单选题
	
	@Select("SELECT id,content,img,audio,video FROM `topic` WHERE typeId=1 AND subjectId=#{id} LIMIT 5")
	public ArrayList<MultiChoicesBean> getMultiBeanBySubId(int id);//获取多选题
	
	
	@Select("SELECT id FROM `topic` WHERE typeId=3 AND subjectId=#{id}")
	public ArrayList<Integer> getMatchTopicIdBySubId(int id);
	
	@Select("select id,content,img,audio,video from topic where typeId=2 and subjectID=#{id} limit 5")
	public ArrayList<JudgementBean> getJudgeBeanBySubId(int id);
	
	@Select("select id as contentId,content,img,audio,video from topic where typeId=3 && number=#{num}")
	public List<ContentBean> getMatchContent(int num);
	
	@Select("select id,content from topic where typeId=4 and subjectId=#{id}")
	public ArrayList<ShortAnswerBean> getShortBeanBySubId(int id);
	
/*	@Insert("INSERT INTO topic (content,typeId,subjectId) VALUES (content,typeId,subjectId)")
	@SelectKey(statement="call identity()", keyProperty="id", before=false, resultType=int.class)
	@Select("select id")
	public int add(@Param(value="content")String content,@Param(value="typeId")int typeId,
			@Param(value="currentSubjectId")int subjectId);*/
	
	
	@Update("UPDATE topic SET points=#{points},correctAnswer=#{correctAnswer} WHERE id=#{topicId}")
	public int update(@Param(value="points")String points,@Param(value="correctAnswer")String correctAnswer,
			@Param(value="topicId")int topicId);
	
	@Update("UPDATE topic SET points=#{points},correctAnswer=#{correctAnswer},pdf=#{pdf} WHERE id=#{topicId}")
	public int updateSFM(@Param(value="points")String points,@Param(value="correctAnswer")String correctAnswer,
			@Param(value="pdf")String pdf,@Param(value="topicId")int topicId);
	
	/*@Insert("insert into topic (number,content,typeId,img,audio,video,paperId,points,correctAnswer) values (#{number},#{content},#{TYPE},#{img},#{audio},#{video},#{paperId},#{fullMark},#{correctAnswer})")
	public int addShortAnswer(ShortAnswerRow shortAnswerRow);
	
	@Insert("insert into topic (number,content,typeId,img,audio,video,paperId,points,correctAnswer) values (#{number},#{content},#{TYPE},#{img},#{audio},#{video},#{paperId},#{fullMark},#{correctAnswerFile})")*/
	
	
	//数据库修改之后的操作语句
	
	@Select("SELECT id,number,content,img,audio,video FROM `topic` WHERE typeId=0 AND paperId=#{paperId}")
	public List<SingleChoiceBean> getSingleByPaperId(int paperId);
	
	@Select("SELECT id,number,content,img,audio,video FROM `topic` WHERE typeId=1 AND paperId=#{paperId}")
	public List<MultiChoicesBean> getMultiByPaperId(int paperId);
	
	@Select("SELECT id,number,content,img,audio,video FROM `topic` WHERE typeId=2 AND paperId=#{paperId}")
	public List<JudgementBean> getJudgeByPaperId(int paperId);
	
	@Select("SELECT id,number,content,img,audio,video FROM topic WHERE typeId=3 AND paperId=#{paperId}")
	public List<MatchingBean> getMatchByPaperId(int paperId);
	
	@Select("SELECT id,number,content,img,audio,video,pdf FROM topic WHERE typeId=4 AND paperId=#{paperId}")
	public List<ShortAnswerBean> getShortByPaperId(int paperId);
	
	@Select("SELECT id,number,content,img,audio,video,pdf FROM topic WHERE typeId=5 AND paperId=#{paperId}")
	public List<FillBlankBean> getFillBlankByPaperId(int paperId);
	
	@Select("SELECT id,content,img,audio,video,pdf FROM topic WHERE typeId=6 AND paperId=#{paperId}")
	public List<MachineTestBean> getMachineByPaperId(int paperId);
	
	@Select("SELECT id AS topicId,correctAnswer AS choiceId,points FROM `topic` WHERE paperId=#{paperId}")
	public List<CorrectAnswerBean> getCorrectAnswerByPaperId(int paperId);
	
	@Select("SELECT correctAnswer FROM `topic` WHERE  id=#{id}")
	public String getCorrectAnswerById(int id);
	
	@Select("SELECT points FROM `topic` WHERE  id=#{id}")
	public String getPointsById(int id);
	
	//@Select("TRUNCATE TABLE topic")
	@Delete("delete from topic")
	public void delteAll();
	
	@Select("SELECT id,points,correctAnswer FROM topic")
	public List<TopicBeanExport> getPointsAndCorrect();
	
	@Select("SELECT DISTINCT typeId FROM `topic` WHERE paperId=#{id}")
	public List<Integer> getTypeList(int id);
	
	@Select("SELECT SUM(topic.points) FROM topic WHERE typeId=#{typeId} AND paperId=#{paperId}")
	public int getSumOfType(@Param(value = "typeId")int typeId,@Param(value = "paperId")int paperId);
	
	@Select("SELECT COUNT(*)  FROM topic WHERE typeId=#{typeId} AND paperId=#{paperId}")
	public int getNumOfType(@Param(value = "typeId")int typeId,@Param(value = "paperId")int paperId);
}

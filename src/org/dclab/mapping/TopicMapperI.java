package org.dclab.mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.dclab.model.ContentBean;
import org.dclab.model.JudgementBean;
import org.dclab.model.MatchingBean;
import org.dclab.model.MultiChoicesBean;
import org.dclab.model.SingleChoiceBean;
import org.dclab.model.ShortAnswerBean;

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
	
}

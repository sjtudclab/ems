package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.model.ContentBean;
import org.dclab.model.JudgementBean;
import org.dclab.model.MatchingBean;
import org.dclab.model.MultiChoicesBean;
import org.dclab.model.SingleChoiceBean;
import org.dclab.model.shortAnswerBean;

public interface TopicMapperI {
	
	@Select("SELECT id,content,img,audio,video FROM `topic` WHERE typeId=0 AND subjectId=#{id} LIMIT 5")
	public List<SingleChoiceBean> getSingleBeanBySubId(int id);//获取单选题
	
	@Select("SELECT id,content,img,audio,video FROM `topic` WHERE typeId=1 AND subjectId=#{id} LIMIT 5")
	public List<MultiChoicesBean> getMultiBeanBySubId(int id);//获取多选题
	
	
	@Select("SELECT id FROM `topic` WHERE typeId=3 AND subjectId=#{id}")
	public List<Integer> getMatchTopicIdBySubId(int id);
	
	@Select("select id,content,img,audio,video from topic where typeId=2 and subjectID=#{id} limit 5")
	public List<JudgementBean> getJudgeBeanBySubId(int id);
	
	@Select("select id as contentId,content,img,audio,video from topic where typeId=3 && number=#{num}")
	public List<ContentBean> getMatchContent(int num);
	
	@Select("select id,content from topic where typeId=4 and subjectId=#{id}")
	public List<shortAnswerBean> getShortBeanBySubId(int id);
	
}

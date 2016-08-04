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
	@Select("select id,content,img,audio,video from topic where typeId=0 limit 5")
	public List<SingleChoiceBean> getSingleBean();
	
	@Select("select id,content,img,audio,video from topic where typeId=1 limit 5")
	public List<MultiChoicesBean> getMultiBean();
	
	
	@Select("select id from topic where typeId=3")
	public List<Integer> getMatchTopicId();
	
	@Select("select id,content,img,audio,video from topic where typeId=2 limit 5")
	public List<JudgementBean> getJudgeBean();
	
	@Select("select id as contentId,content,img,audio,video from topic where typeId=3 && number=#{num}")
	public List<ContentBean> getMatchContent(int num);
	
	@Select("select id,content from topic where typeId=4")
	public List<shortAnswerBean> getShortBean();
	
}

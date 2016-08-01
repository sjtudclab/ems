package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.model.ContentBean;
import org.dclab.model.JudgementBean;
import org.dclab.model.MatchingBean;
import org.dclab.model.MultiChoicesBean;
import org.dclab.model.SingleChoiceBean;

public interface TopicMapperI {
	@Select("select id,content,img,audio from topic where typeId=0 order by rand() limit 5")
	public List<SingleChoiceBean> getSingleBean();
	
	@Select("select id,content,img,audio from topic where typeId=1 order by rand() limit 5")
	public List<MultiChoicesBean> getMultiBean();
	
	
	@Select("select id from topic where typeId=3 order by rand()")
	public List<Integer> getMatchTopicId();
	
	@Select("select id,content,img,audio from topic where typeId=2 order by rand() limit 5")
	public List<JudgementBean> getJudgeBean();
	
	@Select("select id as contentId,content,img,audio from topic where typeId=3 && number=#{num}")
	public List<ContentBean> getMatchContent(int num);
	
}

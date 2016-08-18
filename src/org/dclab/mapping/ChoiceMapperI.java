package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.dclab.model.ChoicesBean;

public interface ChoiceMapperI {
	@Select("select choiceId,content from choice where topicId=#{topicId}")
	public List<ChoicesBean> getChoice(int topicId);
	
	@Select("select choiceId,content from choice where choiceId>60&&choiceId<63")
	public List<ChoicesBean> getJudgeChoice();
	
	@Insert("insert into choice (topicId) values (#{topicId})")
	public int addGapChoice(int topicId);
	
	@Select("SELECT COUNT(*) FROM `choice` WHERE topicId=#{topicId}")
	public int getFillNumById(int topicId);
	
}

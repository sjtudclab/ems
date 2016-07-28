package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.model.ChoicesBean;

public interface ChoiceMapperI {
	@Select("select id as choiceId,content from choice where topicId=#{topicId}")
	public List<ChoicesBean> getChoice(int topicId);
	
	@Select("select id as choiceId,content from choice where id>60&&id<63")
	public List<ChoicesBean> getJudgeChoice();
}

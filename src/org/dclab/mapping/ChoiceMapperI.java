package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dclab.model.ChoicesBean;

public interface ChoiceMapperI {
	@Select("select choiceId,content from choice where topicId=#{topicId}")
	public List<ChoicesBean> getChoice(int topicId);
	
	@Select("SELECT choiceId,content FROM `choice` WHERE choiceId=#{right} OR choiceId=#{error}")
	public List<ChoicesBean> getJudgeChoice(@Param(value= "right")int right,@Param(value="error")int error);
	
	@Insert("insert into choice (topicId) values (#{topicId})")
	public int addGapChoice(int topicId);
	
	@Select("SELECT COUNT(*) FROM `choice` WHERE topicId=#{topicId}")
	public int getFillNumById(int topicId);
	
	//@Select("TRUNCATE TABLE choice")
	@Delete("delete from choice")
	public void deleteAll();
	
}

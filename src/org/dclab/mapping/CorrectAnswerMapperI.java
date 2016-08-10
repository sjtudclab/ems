package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dclab.model.CorrectAnswerBean;

public interface CorrectAnswerMapperI {
	@Select("SELECT * FROM `correct_answer`")
	public List<CorrectAnswerBean> getCorrectAnswer();
	
	@Insert("INSERT into correct_answer(topicId,choiceId,points) VALUES (#{topicId},#{choiceId},#{points})")
	public int add(@Param(value="topicId")int topicId,@Param(value="choiceId")String choiceId,
			@Param(value="points")String points);
}

package org.dclab.mapping;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.dclab.model.CandidateAnswerBean;

public interface CanAnswerMapperI {
	@Insert("INSERT INTO candidate_answer(candidate_id,topicId,choiceId) VALUES(#{candidate_id},#{topicId},#{choiceId})")
	public int insertAnswer(CandidateAnswerBean cabean);
	
	@Select("`TRUNCATE` TABLE candidate_answer")
	public void deleteAll();
}

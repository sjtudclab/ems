package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.model.CorrectAnswerBean;

public interface CorrectAnswerMapperI {
	@Select("SELECT * FROM `correct_answer`")
	public List<CorrectAnswerBean> getCorrectAnswer();
}

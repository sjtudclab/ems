package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.model.ContentBean;

public interface MatchItemMapperI {
	@Select("select id as contentId,content from matchItem where topicId=#{topicId}")
	public List<ContentBean> getItem(int topicId);
}

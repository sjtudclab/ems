package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dclab.model.ContentBean;

public interface MatchItemMapperI {
	@Select("select id as contentId,content from matchItem where topicId=#{topicId}")
	public List<ContentBean> getItem(int topicId);
	
	
	@Insert("insert into matchitem (content,topicId) values (#{content},#{topicId})")
	public int addItem(@Param(value="content")String content,@Param(value="topicId")int topicId);
	
	//@Select("TRUNCATE TABLE matchitem")
	@Delete("delete from matchitem")
	public void deleteAll();
}

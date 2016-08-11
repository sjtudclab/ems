package org.dclab.mapping;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SubTypeMapperI {
	@Insert("INSERT INTO subject_type (typeId,subId,points) VALUES(#{typeId},#{subId},#{points})")
	public int add(@Param(value="typeId")int typeId,@Param(value="subId")int subId,@Param(value="points")String points);
	
	
	@Select("SELECT points FROM `subject_type` where typeId=#{typeId} and subId=#{currentSubjectId}")
	public String getPointsByType(@Param(value="typeId")int typeId,@Param(value="currentSubjectId")int currentSubjectId);
}

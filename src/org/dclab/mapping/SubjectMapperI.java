package org.dclab.mapping;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SubjectMapperI {
	@Insert("INSERT INTO `subject`(name,duration,earliestSubmit,latestLogin) VALUES (#{name},#{duration},#{earliestSubmit},#{latestLogin})")
	public int add(@Param(value="name")String name,@Param(value="duration")int duration,
			@Param(value="earliestSubmit")int earliestSubmit,@Param(value="latestLogin")int latestLogin);
	
	
	@Select("SELECT subId FROM `subject` WHERE `name`=#{name}")
	public int getSubIdByName(String name);
	
	@Select("SELECT duration FROM `subject` WHERE subId=#{id}")
	public int getDurationBySubId(int id);
	
	
	@Select("select earliestSubmit from subject where subId=#{id}")
	public int getEarSubmitBySubId(int id);
}

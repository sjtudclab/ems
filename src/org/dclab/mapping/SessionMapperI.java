
package org.dclab.mapping;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.Session;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

public interface SessionMapperI {
	//获取session对象
	@Select("SELECT `name`,startTime,session.subId,earliestSubmit,latestLogin FROM `session` INNER JOIN `subject` on session.subId=subject.subId WHERE id=#{id}")
	public Session getById(int id);
	//由id取得session的name
	@Select("select name from session where id=#{id}")
	public String getNameById(int id);

	
	//获得这场考试的开始时间
	@Select("select startTime from session where id=#{id}")
	public Timestamp getStartTimeById(int id);
	
	
	//由场次id获得科目id
	@Select("SELECT subId FROM `session` WHERE id=#{id}")
	public int getSubIdById(int id);
	
	
	@Select("SELECT id FROM `session` WHERE startTime=#{time}")
	public List<Integer> getSidByTime(Timestamp time);
}

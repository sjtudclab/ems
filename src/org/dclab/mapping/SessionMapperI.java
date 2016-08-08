
package org.dclab.mapping;

import java.sql.Date;
import java.sql.Timestamp;

import org.apache.ibatis.annotations.Select;
import org.dclab.Session;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

public interface SessionMapperI {
	//获取session对象
	@Select("select * from session where id=#{id}")
	public Session getById(int id);
	//由id取得session的name
	@Select("select name from session where id=#{id}")
	public String getNameById(int id);
	//获得这场考试的标准考试时长
	@Select("SELECT  duration FROM `session` WHERE id=#{id}")
	public int getDurationById(int id);
	//获得这场考试的开始时间
	@Select("select startTime from session where id=#{id}")
	public Timestamp getStartTimeById(int id);
	
	
	//由场次id获得科目id
	@Select("SELECT subId FROM `session` WHERE id=#{id}")
	public int getSubIdById(int id);
}
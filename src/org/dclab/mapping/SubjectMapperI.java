
package org.dclab.mapping;

import java.sql.Date;
import java.sql.Timestamp;

import org.apache.ibatis.annotations.Select;
import org.dclab.Subject;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

public interface SubjectMapperI {
	//获取subject对象
	@Select("select * from subject where id=#{id}")
	public Subject getById(int id);
	//由id取得subject的name
	@Select("select name from subject where id=#{id}")
	public String getNameById(int id);
	
	@Select("select duration from subject where id=#{id}")
	public int getDurationById(int id);
	
	@Select("select startTime from subject where id=#{id}")
	public Timestamp getStartTimeById(int id);
}

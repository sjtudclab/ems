package org.dclab.mapping;

import org.apache.ibatis.annotations.Select;
import org.dclab.Subject;

public interface SubjectMapperI {
	//获取subject对象
	@Select("select * from subject where id=#{id}")
	public Subject getById(int id);
	//由id取得subject的name
	@Select("select name from subject where id=#{id}")
	public String getNameById(int id);
}

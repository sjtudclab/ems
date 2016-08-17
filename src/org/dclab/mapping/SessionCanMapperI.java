package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SessionCanMapperI {
	
	@Insert("INSERT INTO session_candidate (sid,seatNum,ip,Uid) VALUES (#{sid},#{seatNum},#{ip},#{Uid})")
	public int addSessionCan(@Param(value="sid")int sid,@Param(value="seatNum")int seatNum,@Param(value="ip")String ip,@Param(value="Uid") String Uid);
	
	
	@Select("SELECT Uid FROM `session_candidate` WHERE sid=#{sid}")
	public List<String> getUidListBySid(int sid);
	
}

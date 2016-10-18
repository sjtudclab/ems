package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dclab.model.CandidateBean;

public interface SessionCanMapperI {
	
	@Insert("INSERT INTO session_candidate (sid,seatNum,ip,Uid) VALUES (#{sid},#{seatNum},#{ip},#{Uid})")
	public int addSessionCan(@Param(value="sid")int sid,@Param(value="seatNum")int seatNum,@Param(value="ip")String ip,@Param(value="Uid") String Uid);
	
	
	@Select("SELECT Uid FROM `session_candidate` WHERE sid=#{sid} AND Uid IS NOT NULL")
	public List<String> getUidListBySid(int sid);
	
	@Select("SELECT sid FROM `session_candidate` WHERE Uid=#{id}")
	public int getSidByUid(String id);
	
	
	@Select("SELECT roomName,seatNum,Uname,gender,Cid,`user`.Uid FROM session_candidate INNER JOIN `user` on `user`.Uid=session_candidate.Uid JOIN  `session` on session_candidate.sid=`session`.id WHERE `user`.Uid=#{uid}")
	public CandidateBean getCandidateBeanByUid(String uid);
	
	
	@Select("SELECT seatNum FROM `session_candidate` WHERE Uid IS NULL")
	public List<Integer> getFreeSeat();
	
	@Select("SELECT COUNT(*) FROM `session_candidate` WHERE sid=#{sid}")
	public int getSizeOfSession(int sid);
	
	//@Select("TRUNCATE TABLE session_candidate")
	@Delete("delete from `session_candidate`")
	public void deleteAll();


}

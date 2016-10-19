
package org.dclab.mapping;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dclab.Session;
import org.dclab.model.RoomInfoBean;
import org.dclab.model.SessionBean;
import org.dclab.model.StudentInfoBean;

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
	
	@Select("SELECT DISTINCT startTime FROM `session`")
	public List<Timestamp> getStartTime();
	
	//由场次id获得科目id
	@Select("SELECT subId FROM `session` WHERE id=#{id}")
	public int getSubIdById(int id);
	
	
	@Select("SELECT id FROM `session` WHERE startTime=#{time}")
	public List<Integer> getSidByTime(Timestamp time);
	
	
	@Select("select roomName from session where Uid=#{uid}")
	public String getRoomNameByUid(String uid);
	
	@Select("SELECT id FROM `session` WHERE Uid=#{uid}")
	public int getIdByUid(String uid);
	
	@Select("SELECT * FROM `session` WHERE Uid IS NOT NULL")
	public List<RoomInfoBean> getRoomInfo();
	
	@Select("SELECT id,startTime,roomName FROM `session`")
	public List<SessionBean> getSessionList();
	
	//@Select("TRUNCATE TABLE session")
	@Delete("delete from `session`")
	public void deleteAll();
	
	@Select("SELECT roomName FROM `session` where id = #{id}")
	public String getRoomNameById(int id);
	
	@Select("SELECT  roomName,startTime,`user`.Uid,seatNum,Uname,ip,gender,Cid,photo,proName,subName,paperNum FROM `session` JOIN session_candidate JOIN `user` JOIN paper WHERE `user`.Uid = session_candidate.Uid AND `session`.id = session_candidate.sid AND paper.paperId = `user`.paperId")
	public List<StudentInfoBean> getStuInfoAll();
	
	@Select("SELECT Uid FROM `session` WHERE id = #{id}")
	public List<String> getUidById(int id);
	
	@Update("UPDATE `session` SET startFlag=1 WHERE id = #{id}")
	public int updateStartFlag(int id);
	
	@Update("UPDATE `session` SET startFlag=2 WHERE id = #{id}")
	public int cancelStartFlag(int id);
	
	@Select("SELECT startFlag from `session` WHERE id =#{id}")
	public int getStartFlag(int id);
	
	@Select("SELECT startFlag from `session` WHERE Uid = #{uid}")
	public int getStartFlagByUid(String uid);
}

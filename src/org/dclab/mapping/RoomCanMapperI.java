
package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.model.CandidateBean;

/**
 * @author alvis
 *该接口负责查询room_candidate表，即考场号和uid的对应关系
 */
public interface RoomCanMapperI {
	@Select("select roomId from room_candidate where candidateId=#{Uid}")
	public int getRoomIdByUid(int Uid);
	
	@Select("select seatNum,Uname,gender,user.Uid,Cid from room_candidate inner join user on room_candidate.candidateId=user.Uid where roomId=#{roomId}")
	public List<CandidateBean> getUserListByRoomId(int roomId);
	
	@Select("SELECT seatNum FROM room_candidate WHERE roomId=#{roomId} AND candidateId IS NULL ")
	public List<Integer> getFreeSeatByRoomId(int roomId);
}


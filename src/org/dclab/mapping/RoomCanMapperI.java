package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;

/**
 * @author alvis
 *该接口负责查询room_candidate表，即考场号和uid的对应关系
 */
public interface RoomCanMapperI {
	@Select("select roomId from room_candidate where candidateId=#{Uid}")
	public int getRoomIdByUid(int Uid);
	
	//这个语句注意一下
	@Select("select candidateId from room_candidate where roomId=#(roomId) and seatNum!=null order by seatNum")
	public List<Integer> getUidByRoomId(int roomId);
}

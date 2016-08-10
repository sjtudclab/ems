package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.model.RoomInfoBean;

/**
 * @author alvis
 *查询room表的接口
 *主要根据监考id查询对应考场号
 */
public interface RoomMapperI {
	@Select("select id from room where Uid=#{Uid}")
	public int getRoomIdByUid(int Uid);
	
	@Select("SELECT id as roomId,name,size,Uid FROM `room`")
	public List<RoomInfoBean> getRoomInfo();
}

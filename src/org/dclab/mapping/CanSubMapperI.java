package org.dclab.mapping;

import org.apache.ibatis.annotations.Select;

/**
 * @author alvis
 *查询candidate_subject表的sql接口
 */
public interface CanSubMapperI {
	@Select("select sessionId from candidate_session where candidateId=#{uid}")//用考生准考证号获得sessionId
	public int getSessionIdByUid(int uid);
}

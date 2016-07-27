package org.dclab.mapping;

import org.apache.ibatis.annotations.Select;

/**
 * @author alvis
 *查询candidate_subject表的sql接口
 */
public interface CanSubMapperI {
	@Select("select subjectId from candidate_subject where candidateId=#{uid}")
	public int getSubjectIdByUid(int uid);
}

package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dclab.model.PaperInfoBean;
import org.dclab.model.SubjectRow;

public interface PaperMapperI {
	//@Update("TRUNCATE TABLE paper")
	@Delete("delete from paper")
	public void deleteAll(); 
	
	@Select("SELECT * FROM `paper`")
	public List<SubjectRow> getSubjectrowById();
	
	@Select("SELECT paperId,proName,proId,subName,subId,paperNum FROM `paper`")
	public List<PaperInfoBean> getPaperInfo();
}

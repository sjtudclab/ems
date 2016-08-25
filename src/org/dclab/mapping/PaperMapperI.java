package org.dclab.mapping;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface PaperMapperI {
	@Update("TRUNCATE TABLE paper")
	public void deleteAll();
}

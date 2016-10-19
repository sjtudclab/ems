package org.dclab.mapping;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dclab.User;
import org.dclab.model.CandidatePaperRelationRow;

/**
 * @author alvis
 * 定义sql映射的接口，使用注解指明方法要执行的SQL
 */
public interface UserMapperI {
	
	@Select("select * from user where Uid=#{uid}")
	public User getByUid(String uid);
	
	@Select("select Uid from user inner join candidate_session on user.Uid=candidate_session.candidateId")
	public List<Integer> getUid();
	
	@Select("select Uid from user where Rid=1")
	public List<String> getUidByRid();
	
	
	@Select("SELECT Uname  FROM `user` WHERE Uid=#{id}")
	public String getNmaeByUid(String id);
	
	
	@Insert("INSERT INTO `user` (Uid,Uname,Cid,photo,gender,paperId) VALUES (#{Uid},#{Uname},#{Cid},#{photo},#{gender},#{paperId})")
	public int addUser(CandidatePaperRelationRow row);
	
	
	@Select("SELECT mark FROM `user` WHERE Uid=#{uid}")
	public Integer getMarkByUid(String uid);
	
	
	@Select("SELECT paperId FROM `user` WHERE Uid=#{uid}")
	public int getPaperIdByUid(String uid);
	
/*     //使用@Insert注解指明add方法要执行的SQL
	 @Insert("insert into users(name, age) values(#{name}, #{age})")
	 public int add(User user);
	    
     //使用@Delete注解指明deleteById方法要执行的SQL
	 @Delete("delete from users where id=#{id}")
	 public int deleteById(int id);
	   
     //使用@Update注解指明update方法要执行的SQL
	 @Update("update users set name=#{name},age=#{age} where id=#{id}")
	 public int update(User user);*/
	
	@Delete("delete FROM `user` WHERE Rid=0")
	public void deleteAll();
	
	@Update("UPDATE `user` SET mark=#{mark} WHERE Uid=#{uid}")
	public int updateMark(@Param(value="mark")int mark,@Param(value="uid")String uid);
}


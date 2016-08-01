package org.dclab.mapping;


import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.User;

/**
 * @author alvis
 * 定义sql映射的接口，使用注解指明方法要执行的SQL
 */
public interface UserMapperI {
	
	@Select("select * from user where Uid=#{uid}")
	public User getByUid(int uid);
	
	@Select("select Uid from user inner join candidate_subject on user.Uid=candidate_subject.candidateId")
	public List<Integer> getUid();
	
	@Select("select Uid from user where Rid=1")
	public List<Integer> getUidByRid();
	
	
/*     //使用@Insert注解指明add方法要执行的SQL
	 @Insert("insert into users(name, age) values(#{name}, #{age})")
	 public int add(User user);
	    
     //使用@Delete注解指明deleteById方法要执行的SQL
	 @Delete("delete from users where id=#{id}")
	 public int deleteById(int id);
	   
     //使用@Update注解指明update方法要执行的SQL
	 @Update("update users set name=#{name},age=#{age} where id=#{id}")
	 public int update(User user);*/
}


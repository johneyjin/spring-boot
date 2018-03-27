package com.johney.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import com.johney.user.model.User;


/**
 * 
 * @author Johney
 *
 */
@CacheConfig(cacheNames="baseCache")
public interface UserMapper {

//	@Cacheable
	public List<User> findUserInfo();
	
	@Select(" select id, name, age,phone,address from t_user where name=#{user.name} ; ")
	@Results({ @Result(id = true, property = "id", javaType = Integer.class, column = "id"),
			@Result(property = "name", javaType = String.class, column = "name"),
			@Result(property = "phone", javaType = String.class, column = "phone"),
			@Result(property = "address", javaType = String.class, column = "address"),
			@Result(property = "age", javaType = Integer.class, column = "age") })
//	@Cacheable
	public List<User> findUserInfoByUser(@Param("user")User user);

}

package com.johney.game.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.johney.game.model.Game;

public interface GameMapper {
	@Select(" select id, name from game where name=#{name} ; ")
	@Results({ @Result(id = true, property = "id", javaType = Integer.class, column = "id"),
			@Result(property = "name", javaType = String.class, column = "name")
			 })
	Game findGameByName(@Param("name")String name);
	
	List<Game> findAllGames();
}

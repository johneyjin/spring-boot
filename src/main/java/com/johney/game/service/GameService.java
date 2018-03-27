package com.johney.game.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johney.game.mapper.GameMapper;
import com.johney.game.model.Game;

@Service
public class GameService {

	@Autowired
	private GameMapper gameMapper;
	
	public Game findGameByName(String name){
		return gameMapper.findGameByName(name);
	}
	
	public List<Game> findAllGames(){
		return gameMapper.findAllGames();
	}
}

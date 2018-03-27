package com.johney.game.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.johney.game.model.Game;
import com.johney.game.service.GameService;


@Controller
@RequestMapping("/game")
public class GameController {
	private Logger logger = LoggerFactory.getLogger(GameController.class);
	@Autowired
	private GameService gameService;
	
	@RequestMapping()
	public String gamePage(){
		return "/game/game";
	}
	
	@RequestMapping("/getGames")
	@ResponseBody
	public List<Game> findGames(){
		List<Game> games=gameService.findAllGames();
		Game game=gameService.findGameByName("LOL");
		return games;
	}

}

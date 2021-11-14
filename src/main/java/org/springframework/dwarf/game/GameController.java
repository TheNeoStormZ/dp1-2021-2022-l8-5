package org.springframework.dwarf.game;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dwarf.player2.Player2;
import org.springframework.dwarf.player2.Player2Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Diego Ruiz Gil
 * @author Francisco Javier Migueles Domínguez
 */

@Controller
@RequestMapping("/games")
public class GameController {
	
	private GameService gameService;
	
	@Autowired
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}
	
	@GetMapping()
	public String listGames(ModelMap modelMap) {
		String view = "games/listGames";
		Iterable<Game> games = gameService.findAll();
		modelMap.addAttribute("games", games);
		return view;
	}
	
	
	@DeleteMapping(path="/delete/{gameId}")
	public String deleteGame(@PathVariable("gameId") Integer gameId, ModelMap modelMap) {
		String view = "games/listGames";
		Optional<Game> game = gameService.findByGameId(gameId);
		if (game.isPresent()) {
			gameService.delete(game.get());
			modelMap.addAttribute("message", "game deleted!");
		} else {
			modelMap.addAttribute("message", "game not found!");
		}
		return view;
	}
}
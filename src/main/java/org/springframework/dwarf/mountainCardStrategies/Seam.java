package org.springframework.dwarf.mountainCardStrategies;

import org.jpatterns.gof.StrategyPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dwarf.card.CardStrategy;
import org.springframework.dwarf.game.Game;
import org.springframework.dwarf.game.GameService;
import org.springframework.dwarf.player.Player;
import org.springframework.dwarf.resources.ResourceType;
import org.springframework.dwarf.resources.Resources;
import org.springframework.dwarf.resources.ResourcesService;
import org.springframework.stereotype.Component;
import org.springframework.dwarf.card.StrategyName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@StrategyPattern.ConcreteStrategy
@Component
public class Seam implements CardStrategy {
	@Autowired
	private ResourcesService resourcesService;
	@Autowired
	private GameService gameService;

	private Integer amountToAdd;
	private ResourceType resource;
	
	public Seam(String cardName) {
		if(cardName.equals("Iron Seam")){
			this.amountToAdd = 3;
			this.resource = ResourceType.IRON;
		}else if(cardName.equals("Gold Seam")){
			this.amountToAdd = 1;
			this.resource = ResourceType.GOLD;
		}
	}

	@Override
	public void actions(Player player) {
		log.debug(player.getUsername() + ", con id" + player.getId() + ", ha realizado la accion " + this.getName().toString());
		
		Game game = gameService.findPlayerUnfinishedGames(player).get();
		Resources playerResources = resourcesService.findByPlayerIdAndGameId(player.getId(),game.getId()).get();			
		try {
			playerResources.setResource(resource, amountToAdd);
		}catch(Exception e) {
			
		}
		resourcesService.saveResources(playerResources);
	}

	@Override
	public StrategyName getName() {
		return StrategyName.SEAM;
	}
}

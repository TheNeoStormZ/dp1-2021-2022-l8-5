package org.springframework.dwarf.mountainCardStrategies;

import org.jpatterns.gof.StrategyPattern;
import org.springframework.dwarf.card.CardStrategy;
import org.springframework.dwarf.player.Player;
import org.springframework.dwarf.strategies.CardStrategy.StrategyName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@StrategyPattern.ConcreteStrategy
public class DragonsKnockers implements CardStrategy{
	@Override
	public void actions(Player player) {
		log.debug(player.getUsername() + ", con id" + player.getId() + ", ha realizado la accion " + this.getName().toString());
	}

	@Override
	public StrategyName getName() {
		return StrategyName.DRAGONS_KNOCKERS;
	}
}

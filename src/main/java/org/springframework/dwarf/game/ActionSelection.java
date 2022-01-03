package org.springframework.dwarf.game;

import java.util.List;

import org.jpatterns.gof.StatePattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dwarf.player.Player;
import org.springframework.dwarf.web.LoggedUserController;
import org.springframework.dwarf.worker.Worker;
import org.springframework.dwarf.worker.WorkerService;
import org.springframework.stereotype.Component;

@StatePattern.ConcreteState
@Component
public class ActionSelection implements GamePhase{
	
	@Autowired
    private GameService gameService;
	@Autowired
    private WorkerService workerService;
    
	@Override
	public void phaseResolution(Game game) {
		Player currentPlayer = game.getCurrentPlayer();
		Player loggedUser = LoggedUserController.loggedPlayer();
		// runs only once
		if (!currentPlayer.equals(loggedUser))
			return;
				
		Integer remainingWorkers = workerService.findNotPlacedByGameId(game.getId()).size();
		if (remainingWorkers.equals(0)) {
			game.setPhase(GamePhaseEnum.ACTION_RESOLUTION);	
		}
		
		try {
			changeCurrentPlayer(game);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Worker> notPlacedWorkers = workerService.findNotPlacedByGameId(game.getId());
		// last worker to be placed
		if(notPlacedWorkers.size()==1)
			game.setPhase(GamePhaseEnum.ACTION_RESOLUTION);
	}
	
	private void changeCurrentPlayer(Game game) throws CreateGameWhilePlayingException {
		List<Player> turn = game.getTurnList();
		Player currentPlayer = game.getCurrentPlayer();

		Integer index = turn.indexOf(currentPlayer);
		currentPlayer = turn.get((index+1)%3);
		
		game.setCurrentPlayer(currentPlayer);
		gameService.saveGame(game);
		phaseResolution(game);
	}

	@Override
	public GamePhaseEnum getPhaseName() {
		return GamePhaseEnum.ACTION_SELECTION;
	}
}
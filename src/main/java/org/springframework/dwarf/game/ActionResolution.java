package org.springframework.dwarf.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jpatterns.gof.StatePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dwarf.board.Board;
import org.springframework.dwarf.board.BoardCell;
import org.springframework.dwarf.board.BoardCellActionsComparator;
import org.springframework.dwarf.board.BoardCellService;
import org.springframework.dwarf.mountain_card.CardType;
import org.springframework.dwarf.mountain_card.MountainCard;
import org.springframework.dwarf.player.Player;
import org.springframework.dwarf.resources.ResourceType;
import org.springframework.dwarf.resources.Resources;
import org.springframework.dwarf.resources.ResourcesService;
import org.springframework.dwarf.worker.WorkerService;
import org.springframework.stereotype.Component;

@StatePattern.ConcreteState
@Component
public class ActionResolution implements GamePhase{
    
	@Autowired
    private GameService gameService;
    @Autowired
    private BoardCellService boardCellService;
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private ApplicationContext applicationContext;
    
    private static final int FINAL_GAME_ROUND = 6;

	@Override
	public void phaseResolution(Game game) throws Exception {
		// funcion service no testeada
		List<BoardCell> cellsToResolve = this.getCellstoResolveActions(game);
		
		for(BoardCell cell: cellsToResolve) {
			Player player = cell.getOccupiedBy();
			MountainCard mountainCard = cell.getMountaincards().get(0);
			if(this.canResolveAction(game, mountainCard) && !cell.getIsDisabled())
				mountainCard.cardAction(player, applicationContext, false);

				if (workerService.findNotPlacedAidByGameId(game.getId()).size() > 0) {
					cell.setIsDisabled(true);
					boardCellService.saveBoardCell(cell);
					game.setPhase(GamePhaseEnum.ACTION_SELECTION);
					gameService.saveGame(game);
					game.phaseResolution(applicationContext);
					return;
				}
		}
		
		game.setCanResolveActions(true);
		game.setMusterAnArmyEffect(false);
		game.setCurrentRound(game.getCurrentRound()+1);
		this.updatePlayersPositions(game);
		
		if(game.getCurrentRound() <= FINAL_GAME_ROUND && !this.hasFourItems(game)) {
			game.setPhase(GamePhaseEnum.MINERAL_EXTRACTION);
		} else {
			game.setFinishDate(new Date());
		}
		
	}
	
	protected Boolean canResolveAction(Game game, MountainCard mountainCard) {
		Boolean canResolve = game.getCanResolveActions();
		
		// orc raiders ONLY avoid resolve MINE cards
		if(!mountainCard.getCardType().equals(CardType.MINE) && !canResolve)
			canResolve = true;
			
		return canResolve;
	}
	
	protected Boolean hasFourItems(Game game) {
		return resourcesService.findByGameId(game.getId()).stream()
				.filter(resource -> resource.getItems() >= 4)
				.collect(Collectors.toList()).size() >= 1;
	}
	
	private List<BoardCell> getCellstoResolveActions(Game game) {
		Board board = gameService.findBoardByGameId(game.getId()).get();
		
		// defend cards always resolve action
		List<BoardCell> cellsToResolve = boardCellService.findAllByBoardId(board.getId()).stream()
				.filter(cell -> cell.getOccupiedBy() != null
					|| cell.getMountaincards().get(0).getCardType().equals(CardType.DEFEND))
				.collect(Collectors.toList());
		
		Collections.sort(cellsToResolve, new BoardCellActionsComparator());
		
		return cellsToResolve;
	}
	
	public void updatePlayersPositions(Game game) {
		List<Integer> points = new ArrayList<Integer>();
		for(int i=0; i<game.getPlayersList().size(); i++) {
			points.add(0);
		}
		
		Map<ResourceType, List<Integer>> resourcesAmount = this.getResourcesAmount(game);
		
		for(ResourceType type: List.of(ResourceType.STEEL, ResourceType.GOLD, ResourceType.ITEMS)) {
			points = this.addPoints(points, resourcesAmount.get(type));
		}
		
		if(this.getIsTie(points))
			points = this.tieBreaker(points, resourcesAmount);
		
		this.updateGameWithPositions(points, game);
	}
	
	protected Map<ResourceType, List<Integer>> getResourcesAmount(Game game) {
		Map<ResourceType, List<Integer>> resourcesAmount = new HashMap<>();
		
		for(Player p: game.getPlayersList()) {
			Resources resources = resourcesService.findByPlayerIdAndGameId(p.getId(), game.getId()).get();
			for(ResourceType type: ResourceType.values()) {
				Integer amount;
				try {
					amount = resources.getResourceAmount(type);
					if(!resourcesAmount.containsKey(type))
						resourcesAmount.put(type, new ArrayList<>(List.of(amount)));
					else {
						List<Integer> amounts = resourcesAmount.get(type);
						amounts.add(amount);
						resourcesAmount.put(type, amounts);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return resourcesAmount;
	}
	
	private List<Integer> addPoints(List<Integer> points, List<Integer> resourcesAmount) {
		Integer max = Collections.max(new ArrayList<Integer>(resourcesAmount));
		
		for(int i=0; i<resourcesAmount.size(); i++) {
			if(resourcesAmount.get(i)==max) {
				points.set(i, points.get(i)+1);
			}
		}
		
		return points;
	}
	
	private Boolean getIsTie(List<Integer> points) {
		Integer max = Collections.max(new ArrayList<Integer>(points));
		Integer count = 0;
		for(Integer point: points)
			if(point == max)
				count ++;
		return count > 1;
	}
	
	private List<Integer> tieBreaker(List<Integer> points, Map<ResourceType, List<Integer>> resourcesAmount) {
		points = this.addPoints(points, resourcesAmount.get(ResourceType.BADGES));
		if(this.getIsTie(points))
			points = this.addPoints(points, resourcesAmount.get(ResourceType.IRON));
		return points;
	}

	private void updateGameWithPositions(List<Integer> points, Game game) {
		List<Integer> sortedPoints = new ArrayList<Integer>(points);
		sortedPoints.sort(Collections.reverseOrder());
		List<Player> players = game.getPlayersList();
		
		for(int i=0; i<sortedPoints.size(); i++) {
			Integer index = points.indexOf(sortedPoints.get(i)); //como empatan 2 pilla al mismo
			points.set(index, -1);
			players.set(i,game.getPlayersList().get(index));
		}
		
		game.setPlayersPosition(players);
	}
	
	@Override
	public GamePhaseEnum getPhaseName() {
		return GamePhaseEnum.ACTION_RESOLUTION;
	}
}
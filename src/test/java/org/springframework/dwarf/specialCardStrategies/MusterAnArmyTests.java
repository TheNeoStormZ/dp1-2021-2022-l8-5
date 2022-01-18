package org.springframework.dwarf.specialCardStrategies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dwarf.board.Board;
import org.springframework.dwarf.board.BoardCell;
import org.springframework.dwarf.board.BoardCellService;
import org.springframework.dwarf.board.BoardService;
import org.springframework.dwarf.card.StrategyName;
import org.springframework.dwarf.forgesAlloy.ForgesAlloyResources;
import org.springframework.dwarf.game.Game;
import org.springframework.dwarf.game.GameService;
import org.springframework.dwarf.mountain_card.CardType;
import org.springframework.dwarf.mountain_card.MountainCard;
import org.springframework.dwarf.mountain_card.MountainDeck;
import org.springframework.dwarf.mountain_card.MountainDeckService;
import org.springframework.dwarf.player.Player;
import org.springframework.dwarf.player.PlayerService;
import org.springframework.dwarf.resources.ResourceType;
import org.springframework.dwarf.resources.Resources;
import org.springframework.dwarf.resources.ResourcesService;
import org.springframework.dwarf.user.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(value= {Service.class, Component.class}))
public class MusterAnArmyTests {
	

	   @Autowired
	   protected MusterAnArmy mana;
	   
	   @Autowired
	   private GameService gameService;
	   
	   @Autowired
	   private BoardService boardService;
	   
	   private Game game;
	   
	   private Board board;
	   
	   @BeforeEach
	   void setup() throws Exception {

		   game = gameService.findByGameId(2).get();
	       
	       boardService.createBoard(game);
	       
	       board = gameService.findBoardByGameId(game.getId()).get();
	       
	      List<BoardCell> boardCells = board.getBoardCells();
	      boardCells.get(0).getMountaincards().get(0).setCardType(CardType.AID);
	      
	      boardService.saveBoard(board);
	   }
	
	   @Test
	    void testGetName() {
	        StrategyName name = mana.getName();
	        assertThat(name).isEqualTo(StrategyName.MUSTER_AN_ARMY);
	 
	    }
	   
	   @Test
	    void testGetGetHelpCardsInBoard() {
		   game = gameService.findByGameId(2).get();
	       List<BoardCell> listHelpCards = mana.getGetHelpCardsInBoard(game);
	        assertThat(listHelpCards).isNotEmpty();
	 
	    }
	   
	   @Test
	    void testGetGetHelpCardsInBoardNegative() {
		   List<BoardCell> boardCells = board.getBoardCells();
	      boardCells.get(0).getMountaincards().get(0).setCardType(CardType.MINE);
		      
		   game = gameService.findByGameId(2).get();
	       List<BoardCell> listHelpCards = mana.getGetHelpCardsInBoard(game);
	        assertThat(listHelpCards).isEmpty();
	 
	    }
	   
	   

}

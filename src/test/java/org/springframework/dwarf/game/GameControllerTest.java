package org.springframework.dwarf.game;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dwarf.configuration.SecurityConfiguration;
import org.springframework.dwarf.player.Player;
import org.springframework.dwarf.player.PlayerService;
import org.springframework.dwarf.user.User;
import org.springframework.dwarf.user.UserService;
import org.springframework.dwarf.worker.Worker;
import org.springframework.dwarf.worker.WorkerController;
import org.springframework.dwarf.worker.WorkerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link GameController}
 *
 * @author Pablo Marín
 * 
 */

@WebMvcTest(controllers = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class GameControllerTest {

	private static final int TEST_GAME_ID = 1;

	@Autowired
	private GameController gameController;
	
	@MockBean
	private GameService gameService;
	
	@MockBean
	private PlayerService playerService;
	
	@MockBean
	private UserService userService;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	private Game g0;
	
	
	@BeforeEach
	void setup() {
		g0 = new Game();
		
		Player p1 = new Player();
		User u1 = new User();
		u1.setUsername("player1");
		p1.setUser(u1);
		
		Player p2 = new Player();
		User u2 = new User();
		u2.setUsername("player2");
		p2.setUser(u2);
		
		Player p3 = new Player();
		User u3 = new User();
		u3.setUsername("player3");
		p3.setUser(u3);
		p3.setUser(u3);
		
		g0.setFirstPlayer(p1);
		g0.setSecondPlayer(p2);
		g0.setThirdPlayer(p3);
		
		
		//given(this.workerService.findByWorkerId(TEST_WORKER_ID).get()).willReturn(w0);
	}
	
	@Test
	@WithMockUser(username = "pabmargom3")
    void listWorkers() throws Exception {
	 mockMvc.perform(get("/games/searchGames")).andExpect(status().isOk())
		.andExpect(view().name("games/searchOrCreateGames"))
		.andExpect(model().attributeExists("gamesToJoin"));
	    }
	

}

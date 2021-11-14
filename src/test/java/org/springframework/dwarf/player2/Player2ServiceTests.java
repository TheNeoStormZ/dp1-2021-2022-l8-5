/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dwarf.player2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dwarf.player2.Player2;
import org.springframework.dwarf.player2.Player2Service;
import org.springframework.dwarf.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * Player2ServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class Player2ServiceTests {                
        @Autowired
	protected Player2Service playerService;

	@Test
	void shouldFindPlayersByLastName() {
		Collection<Player2> players = this.playerService.findOwnerByLastName("Marin");
		assertThat(players.size()).isEqualTo(1);

		players = this.playerService.findOwnerByLastName("Daviss");
		assertThat(players.isEmpty()).isTrue();
	}

	@Test
	void shouldFindSinglePlayer() {
		Player2 player = this.playerService.findOwnerById(1);
		assertThat(player.getLastName()).startsWith("Marin");
	}

	@Test
	@Transactional
	public void shouldInsertPlayer() {
		Collection<Player2> players = this.playerService.findOwnerByLastName("Schultz");
		int found = players.size();

		Player2 player = new Player2();
		player.setFirstName("Sam");
		player.setLastName("Schultz");
		player.setAvatarUrl("https://www.w3schools.com/w3images/avatar2.png");
                User user=new User();
                user.setUsername("Sam");
                user.setPassword("supersecretpassword");
                user.setEnabled(true);
                player.setUser(user);                
                
		this.playerService.savePlayer(player);
		assertThat(player.getId().longValue()).isNotEqualTo(0);

		players = this.playerService.findOwnerByLastName("Schultz");
		assertThat(players.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	void shouldUpdatePlayer() {
		Player2 player = this.playerService.findOwnerById(1);
		String oldLastName = player.getLastName();
		String newLastName = oldLastName + "X";

		player.setLastName(newLastName);
		this.playerService.savePlayer(player);

		// retrieving new name from database
		player = this.playerService.findOwnerById(1);
		assertThat(player.getLastName()).isEqualTo(newLastName);
	}


}
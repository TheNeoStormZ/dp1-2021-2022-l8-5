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
package org.springframework.dwarf.player;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dwarf.user.AuthoritiesService;
import org.springframework.dwarf.user.DuplicatedUsernameException;
import org.springframework.dwarf.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 * @author Pablo Marin
 * @autor Pablo Alvarez
 */
@Service
public class PlayerService {

	private PlayerRepository playerRepository;	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public PlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}
	
	@Transactional(readOnly = true)
	public Iterable<Player> findAll() throws DataAccessException {
		return playerRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Player findPlayerById(int id) throws DataAccessException {
		return playerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Player> findPlayerByLastName(String lastName) throws DataAccessException {
		return playerRepository.findByLastName(lastName);
	}
	
	@Transactional(readOnly = true)
	public Player findPlayerByUserName(String username) throws DataAccessException {
		Player player = playerRepository.findByUsername(username);
		return player;
	}

	@Transactional(rollbackFor = DuplicatedUsernameException.class)
	public void savePlayer(Player player) throws DataAccessException, DuplicatedUsernameException {
		if (getusernameDuplicated(player)){
			throw new DuplicatedUsernameException();
		}
		//creating owner
		playerRepository.save(player);		
		//creating user
		userService.saveUser(player.getUser());
		//creating authorities
		authoritiesService.saveAuthorities(player.getUser().getUsername(), "player");
		//When changing here "admin" to "player" in role, for some reason the role becames invalid and gives forbidden in everything
	}
	
	public Boolean getusernameDuplicated(Player player){
		Boolean res=true;
		Player otherPlayer=playerRepository.findByUsername(player.getUsername());
		res= res && otherPlayer!= null;
		res= res && otherPlayer.getId()!= player.getId();
		res = res && otherPlayer.getUsername()==player.getUsername();
		return res;	
	}
	
	
	/*
	@Transactional
	public void saveAdmin(Player player) throws DataAccessException {
		//creating owner
		playerRepository.save(player);		
		//creating user
		userService.saveUser(player.getUser());
		//creating authorities
		authoritiesService.saveAuthorities(player.getUser().getUsername(), "admin");
	}*/
	
	public void delete(Player player) {
		userService.delete(player.getUser());
	}

}

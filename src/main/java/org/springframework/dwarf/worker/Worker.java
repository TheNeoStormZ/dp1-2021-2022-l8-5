package org.springframework.dwarf.worker;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.Range;
import org.springframework.dwarf.game.Game;
import org.springframework.dwarf.model.BaseEntity;
import org.springframework.dwarf.player.Player;

/**
 * Simple JavaBean domain object representing a worker.
 *
 * @author Jose Ignacio Garcia
 * @author David Zamora
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workers")
public class Worker extends BaseEntity{
	
	
	
	public Worker(Player player, Game game) {
		super();
		this.player = player;
		this.game = game;
		this.status=false;
	}
	
	

	public Worker() {
		super();
	}

	@Column(name = "xposition")
	@Range(min= 1, max= 3)
    Integer xposition;
	
	@Column(name = "yposition")
	@Range(min= 0, max= 2)
    Integer yposition;
    
	@Column(name = "status")
	Boolean status;

	@OneToOne
	@JoinColumn(name= "playerID")
	private Player player;
	
	@OneToOne
	@JoinColumn(name= "gameID")
	private Game game;

}

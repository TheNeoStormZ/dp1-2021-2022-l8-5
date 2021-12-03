package org.springframework.dwarf.board;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.dwarf.game.Game;
import org.springframework.dwarf.model.BaseEntity;
import org.springframework.dwarf.mountain_card.MountainCard;
import org.springframework.dwarf.mountain_card.MountainDeck;

/**
 * @author Diego Ruiz Gil
 * @author Francisco Javier Migueles Domínguez
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "boards")
public class Board extends BaseEntity{
	String background;
	@Positive
    int width;
    @Positive
    int height;
    
    public Board(){
    	this.background ="resources/images/oro_erebor.jpg";
        this.width=750;
        this.height=600;
    }
    
    @NotNull
    @OneToOne
    @JoinColumn(name = "MOUNTAINDECK")
    MountainDeck mountainDeck;
    
    @NotNull
    @OneToOne(optional=false)
    @JoinColumn(name = "GAME")
    Game game;

}

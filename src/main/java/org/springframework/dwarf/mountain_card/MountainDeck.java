package org.springframework.dwarf.mountain_card;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.dwarf.model.BaseEntity;

/**
 * @author Diego Ruiz Gil
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mountainDecks")
public class MountainDeck extends BaseEntity{
	
	@Range(min=0,max=4)
    int xPosition;
    @Range(min=0,max=2)
    int yPosition;
    
    public Integer getPositionXInPixels(Integer size) {
    	return (xPosition)*size;
    }
    
    public Integer getPositionYInPixels(Integer size) {
    	return (yPosition)*size;
    }
    
    @NotNull
	@OneToMany
    List<MountainCard> mountainCards;
}

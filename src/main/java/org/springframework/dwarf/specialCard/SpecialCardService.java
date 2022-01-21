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
package org.springframework.dwarf.specialCard;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Pablo Marin
 * @author Pablo Álvarez
 *
 */
@Service
public class SpecialCardService {

	
	private SpecialCardRepository specialCardRepo;
	
	@Autowired
	public SpecialCardService(SpecialCardRepository specialCardRepository) {
		this.specialCardRepo = specialCardRepository;
	}		
	
	@Transactional
	public int cardCount() {
		return (int) specialCardRepo.count();
	}

	public Iterable<SpecialCard> findAll() {
		return specialCardRepo.findAll();
	}
	@Transactional(readOnly = true)
	public Optional<SpecialCard> findBySpecialCardId(int id){
		return specialCardRepo.findById(id);
	}
	
}
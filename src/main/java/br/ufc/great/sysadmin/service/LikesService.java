package br.ufc.great.sysadmin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.ufc.great.sysadmin.domain.repository.ILikesRepository;
import br.ufc.great.sysadmin.domain.model.Likes;
import br.ufc.great.sysadmin.domain.model.Person;

@Service
public class LikesService extends AbstractService<Likes, Long>{
	@Autowired
	private ILikesRepository likesRepository;
	
	@Override
	protected JpaRepository<Likes, Long> getRepository() {
		return likesRepository;
	}

	/**
	 * Dada uma pessoa retorna os likes dela
	 * @param person Pessoa
	 * @return lista de likes da pessoa
	 */
	public List<Likes> getLikesByPerson(Person person){
		return likesRepository.findByPerson(person);
	}
	
}
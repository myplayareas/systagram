package br.ufc.great.sysadmin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.ufc.great.sysadmin.domain.repository.IPostRepository;
import br.ufc.great.sysadmin.domain.model.Person;
import br.ufc.great.sysadmin.domain.model.Post;

@Service
public class PostService extends AbstractService<Post, Long>{
	@Autowired
	private IPostRepository postRepository;
	
	@Override
	protected JpaRepository<Post, Long> getRepository() {
		return postRepository;
	}

	/**
	 * Busca o post por pessoa
	 * @param person Pessoa
	 * @return post
	 */
	public List<Post> getPostsByPerson(Person person) {
		return postRepository.findByPerson(person);
	}
	
}

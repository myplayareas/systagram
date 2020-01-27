package br.ufc.great.sysadmin.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.sysadmin.domain.model.Person;
import br.ufc.great.sysadmin.domain.model.Post;

/**
 * Interface repositório de Post baseada no JPARepository do Spring
 * @author armandosoaressousa
 *
 */
@Repository
public interface IPostRepository extends JpaRepository<Post, Long>{
	List<Post> findByPerson(Person person);
}
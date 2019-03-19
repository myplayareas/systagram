package br.ufc.great.sysadmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.sysadmin.model.Person;
import br.ufc.great.sysadmin.model.Post;

/**
 * Interface repositório de Post baseada no JPARepository do Spring
 * @author armandosoaressousa
 *
 */
@Repository
public interface IPostRepository extends JpaRepository<Post, Long>{
	List<Post> findByPerson(Person person);
}
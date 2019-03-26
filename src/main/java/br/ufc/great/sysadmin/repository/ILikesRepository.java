package br.ufc.great.sysadmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.sysadmin.model.Likes;
import br.ufc.great.sysadmin.model.Person;

/**
 * Interface repositório de Likes baseada no JPARepository do Spring
 * @author armandosoaressousa
 *
 */
@Repository
public interface ILikesRepository extends JpaRepository<Likes, Long>{
	List<Likes> findByPerson(Person person);
}
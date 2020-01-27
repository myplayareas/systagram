package br.ufc.great.sysadmin.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.sysadmin.domain.model.Comment;
import br.ufc.great.sysadmin.domain.model.Person;

/**
 * Interface repositório de Usuário baseada no JPARepository do Spring
 * @author armandosoaressousa
 *
 */
@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByPerson(Person person);
	List<Comment> findByDescription(String description);
}
package br.ufc.great.sysadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.sysadmin.model.Comment;
import br.ufc.great.sysadmin.model.Person;

/**
 * Interface repositório de Usuário baseada no JPARepository do Spring
 * @author armandosoaressousa
 *
 */
@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long>{
	Comment findByPerson(Person person);
	Comment findByDescription(String description);
}
package br.ufc.great.sysadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.sysadmin.model.Person;
import br.ufc.great.sysadmin.model.Users;

/**
 * Interface repositório de Usuário baseada no JPARepository do Spring
 * @author armandosoaressousa
 *
 */
@Repository
public interface IPersonRepository extends JpaRepository<Person, Long>{
	Person findByUser(Users user);
	Person findByName(String name);
}
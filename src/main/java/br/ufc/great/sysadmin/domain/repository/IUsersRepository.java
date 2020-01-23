package br.ufc.great.sysadmin.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.sysadmin.domain.model.Person;
import br.ufc.great.sysadmin.domain.model.Users;

/**
 * Interface repositório de Usuário baseada no JPARepository do Spring
 * @author armandosoaressousa
 *
 */
@Repository
public interface IUsersRepository extends JpaRepository<Users, Long>{
	Users findByUsername(String username);
	Users findByEmail(String email);
	Users findByPerson(Person person);
}
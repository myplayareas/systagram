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
public interface IUsersRepository extends JpaRepository<Users, Long>{
	Users findByUsername(String username);
	Users findByEmail(String email);
	Users findByPerson(Person person);
}
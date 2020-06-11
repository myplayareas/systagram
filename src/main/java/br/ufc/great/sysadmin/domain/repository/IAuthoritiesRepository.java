package br.ufc.great.sysadmin.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.sysadmin.domain.model.Role;

@Repository
public interface IAuthoritiesRepository extends JpaRepository<Role, Long>{
	Role findByNome(String nome);
}

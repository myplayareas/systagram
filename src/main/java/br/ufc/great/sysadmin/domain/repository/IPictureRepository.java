package br.ufc.great.sysadmin.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.sysadmin.domain.model.Person;
import br.ufc.great.sysadmin.domain.model.Picture;

@Repository
public interface IPictureRepository extends JpaRepository<Picture, Long>{
	List<Picture> findByPerson(Person person);
}

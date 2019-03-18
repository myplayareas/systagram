package br.ufc.great.sysadmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.ufc.great.sysadmin.model.Picture;
import br.ufc.great.sysadmin.repository.IPictureRepository;

@Service
public class PictureService extends AbstractService<Picture, Long>{
	@Autowired
	private IPictureRepository pictureRepository;
	@Override
	protected JpaRepository<Picture, Long> getRepository() {
		return pictureRepository;
	}

}

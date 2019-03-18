package br.ufc.great.sysadmin.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.sysadmin.model.Person;
import br.ufc.great.sysadmin.model.Picture;
import br.ufc.great.sysadmin.model.Users;
import br.ufc.great.sysadmin.service.PersonService;
import br.ufc.great.sysadmin.service.PictureService;
import br.ufc.great.sysadmin.service.UsersService;
import br.ufc.great.sysadmin.util.Constantes;
import br.ufc.great.sysadmin.util.ManipuladorDatas;
import br.ufc.great.sysadmin.util.MySessionInfo;

@Controller
public class FileUploadController {

	private UsersService userService;
	private Users loginUser;
	private PersonService personService;
	private PictureService pictureService;
	
	@Autowired
	private MySessionInfo mySessionInfo;

	@Autowired
	public void setUserService(UsersService userServices){
		this.userService = userServices;
	}
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	@Autowired
	public void setPictureService(PictureService pictureService) {
		this.pictureService = pictureService;
	}

	private void checkUser() {
		loginUser = mySessionInfo.getCurrentUser();
	}

	@RequestMapping("/uploads")
	public String UploadPage(Model model) {
		return "uploads/uploadview";
	}
	
	@RequestMapping("/upload/person/{id}/picture")
	public String listMyPictures(@PathVariable Long id, Model model) {
		Person person = this.personService.get(id);
		
		List<Picture> list = person.getPictures();
		
		checkUser();
		model.addAttribute("user", person.getUser());
		model.addAttribute("person", person);
		model.addAttribute("list", list);
		model.addAttribute("loginusername", loginUser.getUsername());
		model.addAttribute("loginemailuser", loginUser.getEmail());
		model.addAttribute("loginuserid", loginUser.getId());
		
		return "/uploads/listMypictures";
	}
	
	public File renameFile(File file, String newName) throws IOException {
		// File (or directory) with new name
		File file2 = new File(newName);

		if (file2.exists())
			throw new java.io.IOException("file exists");

		// Rename file (or directory)
		boolean success = file.renameTo(file2);

		if (success) {
			return file;
		}else {
			return null;
		}

	}

	@RequestMapping("/upload/selected/image/users/{idUser}")
	public String upload(@PathVariable(value = "idUser") Long idUser, Model model,@RequestParam("photouser") MultipartFile[] files) {
		StringBuilder fileNames = new StringBuilder();
		new Constantes();
		String uploadFilePath = Constantes.uploadUserDirectory; 	  
		String idAux = String.valueOf(idUser);

		for (MultipartFile file : files) {
			Path fileNameAndPath = Paths.get(uploadFilePath, idAux+".png");
			fileNames.append(idAux+".png"+" ");
			try {
				Files.write(fileNameAndPath, file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		checkUser();
		Users user = this.userService.get(idUser);

		model.addAttribute("user", user);
		model.addAttribute("loginusername", loginUser.getUsername());
		model.addAttribute("loginemailuser", loginUser.getEmail());
		model.addAttribute("loginuserid", loginUser.getId());
		model.addAttribute("successFlash", "Successfully uploaded files "+fileNames.toString());

		return "uploads/formpwd";
	}

	/**
	 * Faz o upload de fotos da pessoa para seu album de fotos
	 * @param personId id da Pessoa
	 * @param model Model
	 * @param files arquivos de imagens que serão carregados para o album de fotos
	 * @return carrega listMyPictures.html
	 */
	@RequestMapping(value="/upload/selected/picture/person/{personId}")
	public String uploadPicture(@PathVariable(value = "personId") Long personId, Picture picture, Model model,@RequestParam("photouser") MultipartFile[] files, RedirectAttributes ra) {
		StringBuilder fileNames = new StringBuilder();

		new Constantes();
		String uploadFilePath = Constantes.picturesDirectory; 	  
		String idAux = String.valueOf(personId);
		String padrao = "yyyy/MM/dd HH:mm:ss";
		
		new ManipuladorDatas();
		String currentData = ManipuladorDatas.getCurrentDataTime(padrao);
		String dataAux = currentData.replace("/", "-");
		String data1 = dataAux.replaceAll(":", "-").trim();
		String data = data1.replace(" ", "-");
		
		//Define o diretório da imagem e o nome do arquivo que será salvo no filesystem
		String path = uploadFilePath + FileSystems.getDefault().getSeparator() + idAux + "-" + data + ".png";
		String systemName = idAux + "-" + data;
		
    	picture.setPath(path);
    	picture.setSystemName(systemName);
    	
    	//Dono da imagem
    	Person person = this.personService.get(personId);
    	person.addPicture(picture, person);
    	    	
    	Person save = this.personService.save(person);
		
    	//Salva os bytes do arquivo no arquivo criado no filesystem
		for (MultipartFile file : files) {
			Path fileNameAndPath = Paths.get(uploadFilePath, idAux + "-" + data + ".png");
			fileNames.append(idAux + "-" + data + ".png"+" ");
			try {
				Files.write(fileNameAndPath, file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		checkUser();
		
		List<Picture> list = person.getPictures();

		model.addAttribute("user", person.getUser());
		model.addAttribute("person", person);
		model.addAttribute("list", list);
		model.addAttribute("loginusername", loginUser.getUsername());
		model.addAttribute("loginemailuser", loginUser.getEmail());
		model.addAttribute("loginuserid", loginUser.getId());
		model.addAttribute("successFlash", "Successfully uploaded files " + fileNames.toString());

		return "/uploads/listMypictures";
	}

	@RequestMapping(value = "/upload/picture/{pictureName}")
	@ResponseBody
	public byte[] getPicture(@PathVariable(value = "pictureName") String imageName) throws IOException {
		new Constantes();
		String uploadFilePath = Constantes.picturesDirectory;

		File serverFile = new File(uploadFilePath + FileSystems.getDefault().getSeparator() + imageName + ".png");
		
		if (serverFile.length() > 0) {
			return Files.readAllBytes(serverFile.toPath());
		}else {
			return null;
		}
		
	}

	
	@RequestMapping(value = "/upload/image/users/{imageName}")
	@ResponseBody
	public byte[] getUserImage(@PathVariable(value = "imageName") String imageName) throws IOException {
		new Constantes();
		String uploadFilePath = Constantes.uploadUserDirectory;

		File serverFile = new File(uploadFilePath + FileSystems.getDefault().getSeparator() + imageName + ".png");      
		File userPadrao = new File(uploadFilePath + FileSystems.getDefault().getSeparator() + "anonymous2.png");

		if (serverFile.length() > 0) {
			return Files.readAllBytes(serverFile.toPath());	  
		}else {		  
			return Files.readAllBytes(userPadrao.toPath());
		}

	}


	@RequestMapping(value = "/upload/image/{imageName}")
	@ResponseBody
	public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {
		new Constantes();
		String uploadFilePath = Constantes.uploadDirectory;

		File serverFile = new File(uploadFilePath + FileSystems.getDefault().getSeparator() + imageName + ".png");
		return Files.readAllBytes(serverFile.toPath());
	}

	@RequestMapping(value="/viewFile")
	public String viewFile() {
		return "viewfileuploaded";
	}

}

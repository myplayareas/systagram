package br.ufc.great.sysadmin.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import br.ufc.great.sysadmin.model.Users;
import br.ufc.great.sysadmin.service.CommentService;
import br.ufc.great.sysadmin.service.PictureService;
import br.ufc.great.sysadmin.service.PostService;
import br.ufc.great.sysadmin.service.UsersService;
import br.ufc.great.sysadmin.util.Constantes;
import br.ufc.great.sysadmin.util.MySessionInfo;
import br.ufc.great.sysadmin.util.aws.s3.S3ClientManipulator;

/**
 * Faz o controle do Dashboard
 * @author armandosoaressousa
 *
 */
@Controller
public class DashboardController {
	
	private UsersService userService;
	private String acesso;
	private CommentService commentService;
	private PictureService pictureService;
	private PostService postService;
	
	@Autowired
	private MySessionInfo mySessionInfo;

	@Autowired
	public void setPostService(PostService postService) {
		this.postService = postService;
	}
	
	@Autowired
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@Autowired
	public void setPictureService(PictureService pictureService) {
		this.pictureService = pictureService;
	}
	
	@Autowired
	public void setUserService(UsersService userService) {
		this.userService = userService;
	}
	
    @RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	/**
	 * Verifica quais são as permissões do usuário logado e direciona para o dashboard correto
	 * @param model
	 * @param principal
	 * @return
	 */
    @RequestMapping("/")
    public String index(Model model, Principal principal) {    
    	    
    	String servico="/dashboard";
    	
    	if (mySessionInfo.hasRole("ADMIN") && mySessionInfo.hasRole("USER")) {
    		servico = servico + "/admin";
    		return "redirect:"+servico;
    	}
    	if (mySessionInfo.hasRole("USER")) {
    		servico = servico + "/user";
    		return "redirect:"+servico;
    	}
		return "redirec:/logout";    	           	    	
    }
    
    /**
     * Carrega o dashboard do usuário administrador do sistema
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/dashboard/admin")
    public String indexAdmin(Model model, Principal principal) {
    	int totalUsers=0;
    	int totalComments=0;
    	int totalPictures=0;
    	int totalPosts=0;
    	
    	totalUsers = (int) this.userService.count();
    	
    	Users loginUser = userService.getUserByUserName(mySessionInfo.getCurrentUser().getUsername());
    	
    	totalComments = loginUser.getPerson().getComments().size();
    	totalPictures = loginUser.getPerson().getPictures().size();
    	totalPosts = loginUser.getPerson().getPosts().size();
    	
    	acesso = mySessionInfo.getAcesso();
    	
    	List<Users> listUsers = this.userService.getAll();
    	    	
    	model.addAttribute("totalUsers", totalUsers);
    	model.addAttribute("totalComments", totalComments);
    	model.addAttribute("totalPictures", totalPictures);
    	model.addAttribute("totalPosts", totalPosts);
    	model.addAttribute("listUsers", listUsers);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("person", loginUser.getPerson());
    	model.addAttribute("acesso", acesso);
    	model.addAttribute("loginuser", loginUser);
    	model.addAttribute("s3awsurl", new Constantes().s3awsurl);
    	
    	/*
    	 * Habilite esse trecho apenas quando inserir as credenciais de acesso ao AWS
    		testaAcessoS3Amazon();
    	*/
    	     	
        return "dashboard/index";
    }
    
    /**
     * Carrega o dashboard do usuário comum
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/dashboard/user")
    public String indexUser(Model model, Principal principal) {    	    	
    	int totalUsers = (int) this.userService.count();    	
    	Users loginUser = mySessionInfo.getCurrentUser();
    	    	
    	acesso = mySessionInfo.getAcesso();
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("totalUsers", totalUsers);
    	model.addAttribute("person", loginUser.getPerson());
    	model.addAttribute("acesso", acesso);
    	model.addAttribute("loginuser", loginUser);
    	model.addAttribute("s3awsurl", new Constantes().s3awsurl);
    	
        return "dashboard/indexUser";
    }
    
	public void testaAcessoS3Amazon() {
    	String bucketName; 
    	S3ClientManipulator s3Client; 
    	
    	try {
    		//Informa o nome do bucket
    		bucketName = "systagram-uploads2";
    		//Cria uma instancia do Manipulador de S3
    		s3Client = new S3ClientManipulator(bucketName);

    		System.out.println("Acessando o S3 da AWS");
    		//Lista os objetos do bucket selecionado OK
    		for (S3ObjectSummary item : s3Client.getObjectsFromBucket()) {
    			System.out.println("*" + item.getKey());
    		}
    		
    	}
    	catch (AmazonServiceException e) {
    	    System.err.println(e.getErrorMessage());
    	    e.printStackTrace();
    	    System.exit(1);
    	} 
	}

       
}
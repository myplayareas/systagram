package br.ufc.great.sysadmin.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.sysadmin.model.Comment;
import br.ufc.great.sysadmin.model.Person;
import br.ufc.great.sysadmin.model.Users;
import br.ufc.great.sysadmin.service.CommentService;
import br.ufc.great.sysadmin.service.PersonService;
import br.ufc.great.sysadmin.service.PictureService;
import br.ufc.great.sysadmin.service.UsersService;
import br.ufc.great.sysadmin.util.MySessionInfo;

/**
 * Faz o controle do domínio de Controle de Acesso
 * @author armandosoaressousa
 *
 */
@Controller
public class PersonController {
	
	private PersonService personService;
	private UsersService userService;
	private Users loginUser;
	private CommentService commentService;
	private PictureService pictureService;
	
	@Autowired
	private MySessionInfo mySessionInfo;

	@Autowired
	public void setPictureService(PictureService pictureService) {
		this.pictureService = pictureService;
	}
	
	@Autowired
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
    @Autowired
    public void setUserService(UsersService userService) {
    	this.userService = userService;
    }
	
    /**
     * Atualiza os dados do usuario logado
     */
	private void checkUser() {
		loginUser = mySessionInfo.getCurrentUser();
	}
	
	/**
	 * Lista todas as pessoas cadastradas no sistema
	 * @param model
	 * @return página com a lista de pessoas cadastradas
	 */
    @RequestMapping(value="/person", method = RequestMethod.GET)
    public String index(Model model) {
    	List<Person> list = this.personService.getAll();
    	checkUser();
    	
    	model.addAttribute("list", list);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());

        return "person/list";
    }
    
    /**
     * Dada uma pessoa, retorna todos os comentários que ela fez
     * @param id Id de pessoa
     * @param model Model do View
     * @return listMyComments.html
     */
    @RequestMapping(value="/person/{id}/comment")
    public String listMyComments(@PathVariable Long id, Model model) {
    	Person person = this.personService.get(id);
    	List<Comment> comments = new LinkedList<Comment>();
    	
    	comments = person.getComments();
    	checkUser();
    	
    	model.addAttribute("list", comments);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());

        return "person/listMyComments";

    }
          
    /**
     * Retorna todos os comentários de todas as pessoas
     * @param model Model
     * @return listComments.html
     */
    @RequestMapping(value="/person/comment")
    public String listAllComments(Model model) {
    	List<Comment> comments = this.commentService.getAll();
    	
    	checkUser();
    	model.addAttribute("list", comments);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
    	return "person/listComments";
    }
    
    /**
     * Dada uma pessoa e um novo comentário mostra o formulário para inserir comentário
     * @param id Id da pessoa
     * @param model Model
     * @return formComment.html
     */
    @RequestMapping(value="/person/{id}/comment/add")
    public String addMyComment(@PathVariable Long id, Model model) {
    	checkUser();
    	Person person = this.personService.get(id);
    	List<Comment> comments = person.getComments();
    	
    	model.addAttribute("person", person);
    	model.addAttribute("comments", comments);
    	model.addAttribute("comment", new Comment());
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
    	return "person/formComment";
    }
    
    /**
     * Dada uma pessoa e um comentário preenchido, salva esse comentário na lista de comentários da pessoa
     * @param id Id da Pessoa
     * @param comment Novo comentario
     * @param ra mensagem flash de retorno
     * @return listMyComments.html
     */
    @RequestMapping(value="/person/{id}/comment/save", method=RequestMethod.POST)
    public String saveMyComment(@PathVariable Long id, Comment comment, final RedirectAttributes ra) {
    	String local="";
    	
    	Person person = this.personService.get(id);
    	person.addComment(comment, person);
    	Person save = this.personService.save(person);
    	ra.addFlashAttribute("successFlash", "Comentário salvo com sucesso.");

    	if (this.mySessionInfo.getAcesso().equals("ADMIN")) {
    		local = "/person/comment";
    	}else {
    		local = "/person/"+id+"/comment";
    	}
    	
    	return "redirect:"+local;
    }

    @RequestMapping(value="/person/{id}/comment/update", method=RequestMethod.POST)
    public String saveMyCommentEdited(@PathVariable Long id, Comment comment, final RedirectAttributes ra) {
    	String local="";
    	
    	Person person = this.personService.get(id);
    	comment.setPerson(person);
    	
    	this.commentService.update(comment);
    	ra.addFlashAttribute("successFlash", "Comentário salva com sucesso.");
    	
    	if (this.mySessionInfo.getAcesso().equals("ADMIN")) {
    		local = "/person/comment";
    	}else {
    		local = "/person/"+id+"/comment";
    	}
    	
    	return "redirect:"+local;
    }

    
    /**
     * Dada uma pessoa e um comentário selecionado faz a edição do mesmo
     * @param personId id da Pessoa
     * @param commentId id do Comentário selecionado
     * @param model Model
     * @return formEditMyComment.html
     */
    @RequestMapping(value="/person/{personId}/comment/{commentId}/edit")
    public String editMyComment(@PathVariable Long personId, @PathVariable Long commentId, Model model) {
    	Person person = this.personService.get(personId);
    	Comment comment = person.getMyComment(commentId);
    	
    	checkUser();
    	
        model.addAttribute("person", person);
        model.addAttribute("comment", comment);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());

        return "person/formEditMyComment";
    }
    
    public String addPicture() {
    	//TODO
    	return null;
    }
    
    public String savePicture() {
    	//TODO
    	return null;
    }
    
    public String listMyPictures() {
    	//TODO
    	return null;
    }
    
    public String listAllPictures() {
    	//TODO
    	return null;
    }
    
    public String editMyPicture() {
    	//TODO
    	return null;
    }

}
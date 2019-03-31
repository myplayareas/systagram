package br.ufc.great.sysadmin.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.sysadmin.model.Comment;
import br.ufc.great.sysadmin.model.Person;
import br.ufc.great.sysadmin.model.Picture;
import br.ufc.great.sysadmin.model.Post;
import br.ufc.great.sysadmin.model.Role;
import br.ufc.great.sysadmin.model.Users;
import br.ufc.great.sysadmin.service.AuthoritiesService;
import br.ufc.great.sysadmin.service.PersonService;
import br.ufc.great.sysadmin.service.UsersService;
import br.ufc.great.sysadmin.util.GeradorSenha;
import br.ufc.great.sysadmin.util.MySessionInfo;

/**
 * Faz o controle do domínio de usuários
 * @author armandosoaressousa
 *
 */
@Controller
public class UserController {
	private UsersService userService;
	private Users loginUser;
	private AuthoritiesService authoritiesService;
	private PersonService personService;
	
	@Autowired
	private MySessionInfo mySessionInfo;
	
	@Autowired
	public void setUserService(UsersService userServices){
		this.userService = userServices;
	}
	
	@Autowired
	public void setAuthoritiesService(AuthoritiesService authoritiesService) {
		this.authoritiesService = authoritiesService;
	}
	
	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	/*
	 * Atualiza os dados da sessao do usuario logado
	 */
	private void checkUser() {
		loginUser = mySessionInfo.getCurrentUser();
	}
	
	/**
	 * Lista todos os usuarios cadastrados
	 * @param model
	 * @return pagina com todos os usuarios cadastrados
	 */
	@RequestMapping(value = "/users")
	public String index(Model model){
    	List<Users> list = userService.getAll();
    	checkUser();
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);
      	
    	model.addAttribute("list", list);
		
		return "users/list";
	}
	
	/**
	 * Faz a paginação da lista de usuários cadastrado
	 * @param pageNumber
	 * @param model
	 * @return pagina contendo os usuarios paginados pelo pageNumber
	 */
    @RequestMapping(value = "/users/{pageNumber}", method = RequestMethod.GET)
    public String list(@PathVariable Integer pageNumber, Model model) {
    	Page<Users> page = this.userService.getList(pageNumber);
    	checkUser();
    	
        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);
        
        return "users/list";
    }

    /**
     * Faz o cadastro de um novo usuário
     * @param model
     * @return formulario para cadastrar novo usuario
     */
    @RequestMapping("/users/add")
    public String add(Model model) {
    	checkUser();
    	
        model.addAttribute("user", new Users());
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);
    	
        return "users/form";
    }

    /**
     * Edita um usuário selecionado
     * @param id do usuario
     * @param model
     * @return formulario de edicao do usuario com checagem de password
     */
    @RequestMapping("/users/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
		Users editUser = userService.get(id);
		Person person = editUser.getPerson();
		checkUser();
		
        model.addAttribute("user", editUser);
		model.addAttribute("person", person);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);
    	
        return "users/formpwd";
    }

    /**
     * Mostra os detalhes do usuario selecionado
     * @param id Id do Usuario
     * @param model Dados do Usuario
     * @return aboutUser.html
     */
    @RequestMapping("/users/about/{id}")
    public String about(@PathVariable Long id, Model model) {
		Users aboutUser = userService.get(id);
		Person person = aboutUser.getPerson();
		checkUser();
		
        model.addAttribute("user", aboutUser);
		model.addAttribute("person", person);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);
    	
        return "users/aboutUser";
    }
    
    /**
     * Edita profile do usuário logado
     * @param id do usuario logado
     * @param model
     * @return formulario de edicao de profile do usuario
     */
    @RequestMapping("/users/edit/profile/{id}")
    public String editProfile(@PathVariable Long id, Model model) {
    	checkUser();
		Users user = this.userService.get(loginUser.getId());
		Person person = user.getPerson();

        model.addAttribute("user", loginUser);
        model.addAttribute("person", person);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());    	
    	model.addAttribute("loginuser", loginUser);
    	
        return "users/formpwdProfile";

    }
    
    /**
     * Salva os dados de um usuário novo
     * @param user usuario
     * @param password senha
     * @param confirmPassword confirma senha
     * @param nome tipo de acesso ao sistema
     * @param ra
     * @return pagina de usuarios com o novo usuario
     */
    @RequestMapping(value = "/users/save", method = RequestMethod.POST)
    public String save(Users user, @RequestParam("password") String password, 
    		@RequestParam("confirmpassword") String confirmPassword, 
    		@RequestParam("nome") String authority, 
    		final RedirectAttributes ra) {
    	String senhaCriptografada;
    	List<Role> roles = new LinkedList<>();		
    	
    	switch (authoritiesService.checkAuthority(authority)) {
		case "USER":
			roles.add(authoritiesService.getRoleByNome("USER"));
			user.setRoles(roles);
			break;
		default:
			ra.addFlashAttribute("errorFlash", "A permissão não está registrada no sistema!");
			break;
		}
    	
    	if (password.equals(confirmPassword)){
        	senhaCriptografada = new GeradorSenha().criptografa(password);
        	user.setPassword(senhaCriptografada);
        	//Criar uma pessoa vazia e associa ao novo usuário
        	Person person = new Person();
        	person.setUser(user);
        	
        	user.setPerson(person);
            Users save = userService.save(user);
            ra.addFlashAttribute("successFlash", "Usuário foi salvo com sucesso.");
    	}else{
            ra.addFlashAttribute("errorFlash", "A senha do usuário NÃO confere.");
    	}    	
    	return "redirect:/users";
    }
    
    /**
     * Salva as alterações do usuário editado
     * @param user novos dados do usuário
     * @param originalPassword senha original registrada no banco
     * @param newPassword nova senha passada pelo usuário
     * @param confirmnewpassword compara se é igual a newPassword
     * @param ra redireciona os atributos
     * @return página que lista todos os usuários
     */
    @RequestMapping(value = "/users/saveedited", method = RequestMethod.POST)
    public String saveEdited(Users user, @RequestParam("password") String originalPassword, 
    		@RequestParam("newpassword") String newPassword, 
    		@RequestParam("confirmnewpassword") String confirmNewPassword, 
    		final RedirectAttributes ra) {
    	
    	Users userOriginal = userService.get(user.getId());
    	String recuperaPasswordBanco = userOriginal.getPassword();
    	Person personOriginal = userOriginal.getPerson();
    	List<Role> roles = userOriginal.getRoles();	
    	String local="";
		     	    	
    	user.setRoles(roles);
    	user.setPerson(personOriginal);
    	
    	if (newPassword.equals(confirmNewPassword)){
        	if (new GeradorSenha().comparaSenhas(originalPassword, recuperaPasswordBanco)){
        		String novaSenhaCriptografada = new GeradorSenha().criptografa(newPassword);
        		user.setPassword(novaSenhaCriptografada);
                Users save = userService.save(user);
                ra.addFlashAttribute("successFlash", "Usuário " + user.getUsername() + " foi alterado com sucesso.");  		
        	}else{
        		ra.addFlashAttribute("errorFlash", "A senha informada é diferente da senha original.");
        	}
    	}
    	else{
            ra.addFlashAttribute("errorFlash", "A nova senha não foi confirmada.");
    	}
    	
    	if (this.mySessionInfo.getAcesso().equals("ADMIN")) {
    		local = "/users";
    	}else {
    		local = "/";
    	}
    	
    	return "redirect:"+local;
    }

    /**
     * Salva as alterações do usuário editado
     * @param user novos dados do usuário
     * @param originalPassword senha original registrada no banco
     * @param newPassword nova senha passada pelo usuário
     * @param confirmnewpassword compara se é igual a newPassword
     * @param ra redireciona os atributos
     * @return página que lista todos os usuários
     */
    /*
    @RequestMapping(value = "/users/personsaveedited", method = RequestMethod.POST)
    public String savePersonEdited(@RequestParam("id") long id, @RequestParam("name") String name, @RequestParam("address") String address,   
    		@RequestParam("city") String city, @RequestParam("state") String state, 
    		@RequestParam("cep") String cep,  
    		final RedirectAttributes ra) {
    */
    @RequestMapping(value = "/users/personsaveedited", method = RequestMethod.POST)
    public String savePersonEdited(Person person, final RedirectAttributes ra) {
    	String local="";
    	   	
    	Person original = this.personService.get(person.getId());
    	
    	List<Comment> comments = original.getComments();
    	List<Picture> pictures = original.getPictures();
    	List<Post> posts = original.getPosts();

    	person.setComments(comments);
    	person.setPictures(pictures);
    	person.setPosts(posts);
    	this.personService.update(person);
		ra.addFlashAttribute("successFlash", "Os dados pessoais do " + person.getUser().getUsername() + " foram alterados com sucesso.");
    	
		if (this.mySessionInfo.getAcesso().equals("ADMIN")) {
    		local = "/users";
    	}else {
    		local = "/";
    	}
    	
    	return "redirect:"+local;
    }

    
    /**
     * TODO: Checar as dependencias de usuario. Usuario tem lista de permissoes e usuario tem lista de amigos.
     * Remove um usuário selecionado
     * @param id
     * @return
     */
    @RequestMapping("/users/delete/{id}")
    public String delete(@PathVariable Long id, Model model, final RedirectAttributes ra) {    
    	String mensagem = "";    	
    	String nome="";
    	Users userToDelete = this.userService.get(id);
    	    	
    	this.userService.delete(id);
    	nome = userToDelete.getUsername();
    	mensagem =  "Usuário " + nome + " removido com sucesso!";

    	
    	ra.addFlashAttribute("successFlash", mensagem);
        return "redirect:/users";
    }
    
    /**
     * Lista todos os usuários disponíveis
     * @param model
     * @return pagina com todos os usuarios cadastrados
     */
    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    public String listAllUsers(Model model) {
    	List<Users> users =  this.userService.getAll();
    	checkUser();
    	
        model.addAttribute("list", users);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);
        
        return "users/listAllUsers";
    }
         
    /**
     * Seleciona uma imagem de um usuario
     * @param idUser id do usuario
     * @param model
     * @return um formulario para fazer o upload de uma imagem do perfil do usuario
     */
	@RequestMapping(value = "/users/{idUser}/select/image")
	public String selectImage(@PathVariable(value = "idUser") Long idUser, Model model){
		Users editUser = userService.get(idUser);
		checkUser();
		
        model.addAttribute("user", editUser);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("idUser", editUser.getId());
    	model.addAttribute("username", editUser.getUsername());
    	model.addAttribute("loginuser", loginUser);
    	
        return "users/formImage";
	}

	/**
	 * TODO é preciso zerar a sessão do usuário
	 * Return registration form template
	 * @param model
	 * @param user novo usuario que sera registrado
	 * @return formulario para registro de um novo usuario. Um novo usuario recebe a permissao padrao USER.
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegistrationPage(Model model){
		model.addAttribute("user", new Users());		
		return "/register";
	}

	//TODO Revisar a forma de registra das permissões do usuário
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processRegistrationForm(Model model, Users user, @RequestParam("password") String password, 
    		@RequestParam("confirmpassword") String confirmPassword, @RequestParam("authority") String authority, 
    		final RedirectAttributes ra) {
		
		String username = user.getUsername();
		Users userExists = this.userService.getUserByUserName(username);
		
		if (userExists != null) {			
			ra.addFlashAttribute("msgerro", "Usuário já existe!");
			return "redirect:/register";
		}else {	
			//checa a senha do usuário 			
			if (password.equals(confirmPassword)) {
			  	String senhaCriptografada = new GeradorSenha().criptografa(password);

			  	user.setPassword(senhaCriptografada);
				user.setEnabled(true);				
				Role authorities = new Role();	
				Person person = new Person();
				person.setUser(user);
				
				//checa o tipo do usuário
				if (authority.equals("USER")) {				
					authorities = authoritiesService.getRoleByNome("USER");
					List<Role> roles = new LinkedList<>();
					roles.add(authorities);
					user.setRoles(roles);
					user.setPerson(person);
					this.userService.save(user);
					model.addAttribute("msg", "Usuário comum registrado com sucesso!");
					return "/login";				
				}
				
				//checa demais tipos de permissões
				
			}else {
				ra.addFlashAttribute("msgerro", "Senha não confere!");
				return "redirect:/register";
			}			
			
		}
		return "/login";
	}
	
	@RequestMapping(value="/users/search", method=RequestMethod.POST)
	public String search(Model model, @RequestParam("search") String search, final RedirectAttributes ra) {
		Users userExists = this.userService.getUserByUserName(search);
		
		if (userExists == null) {
			ra.addFlashAttribute("errorFlash", "Usuário " + search +  " pesquisado não existe!");
			return "redirect:/users/list";
		}
		
    	checkUser();
    	
        model.addAttribute("list", userExists);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);
        
        return "users/listAllUsers";
	
	}
	
}
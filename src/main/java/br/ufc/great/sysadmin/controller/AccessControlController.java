package br.ufc.great.sysadmin.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.sysadmin.domain.model.Role;
import br.ufc.great.sysadmin.domain.model.Users;
import br.ufc.great.sysadmin.service.AuthoritiesService;
import br.ufc.great.sysadmin.service.UsersService;
import br.ufc.great.sysadmin.util.MySessionInfo;

/**
 * Faz o controle do domínio de Controle de Acesso
 * @author armandosoaressousa
 *
 */
@Controller
public class AccessControlController {
	
	private AuthoritiesService authoritiesService;
	private UsersService userService;
	private Users loginUser;
	
	@Autowired
	private MySessionInfo mySessionInfo;

	@Autowired
	public void setAuthoritiesService(AuthoritiesService authoritiesService) {
		this.authoritiesService = authoritiesService;
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
	 * Lista as permissões cadastradas
	 * @param model
	 * @return página com a lista de permissões cadastradas
	 */
    @RequestMapping(value="/accesscontrol", method = RequestMethod.GET)
    public String index(Model model) {
    	List<Role> authoritiesList = this.authoritiesService.getListAll();
    	checkUser();
    	
    	model.addAttribute("list", authoritiesList);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);

        return "accesscontrol/list";
    }
    
	/**
	 * Lista os usuários cadastrados no sistema
	 * @param model
	 * @return página contendo os usuários do sistema
	 */
	@RequestMapping(value = "/accesscontrol/users")
	public String listUsers(Model model){
    	List<Users> list = userService.getAll();
    	checkUser();
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("list", list);
    	model.addAttribute("loginuser", loginUser);
		
		return "accesscontrol/users";
	}

    /**
     * Edita as permissões do usuário selecionado
     * @param id
     * @param model
     * @return formulário dos dados do usuário com a edição de sua permissão no sistema
     */
    @RequestMapping("/accesscontrol/users/edit/{id}")
    public String editUserAuthority(@PathVariable Long id, Model model) {
		Users editUser = userService.get(id);
		List<Role> authoritiesList = this.authoritiesService.getListAll();
		
		checkUser();
		
        model.addAttribute("user", editUser);
        model.addAttribute("authoritieslist", authoritiesList);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);
    	
        return "accesscontrol/formAuthority";
    }
    
    /**
     * Salva as alterações do usuário editado
     * @param user novos dados do usuário
     * @param nome permissao selecionada
     * @param ra redireciona os atributos
     * @return página que lista todos os usuários
     */
    @RequestMapping(value = "/accesscontrol/users/saveedited", method = RequestMethod.POST)
    public String saveEdited(Users user, @RequestParam("checkMyPermissoes") String authority, final RedirectAttributes ra, ModelMap model) { 
    	List<Role> roles = new LinkedList<Role>();
    	//Recupera o usuário editado
    	Users userEdited = this.userService.get(user.getId());     	    	   	
    	
    	if (authority != null) {
        	//faz o split da string authority e guarda em um array de String
        	String[] myPermissoes = authority.split(",");

    		//para cada elemento busca a role correspondente
    		for (String elemento : myPermissoes) {
    			Role myRole;
    			myRole = this.authoritiesService.getRoleByNome(elemento);  
    			roles.add(myRole);
    		}

    		//atualiza os dados do usuário editado
    		userEdited.setEmail(user.getEmail());
    		userEdited.setEnabled(user.isEnabled());    	
    		userEdited.setRoles(roles);

    		this.userService.save(userEdited);					
    		ra.addFlashAttribute("successFlash", "As permissões " + authority + " do Usuário " + userEdited.getUsername() + " foram alteradas com sucesso.");    		
    	}
    	else {
    		ra.addFlashAttribute("errorFlash", "A permissão não está registrada no sistema!");
    	}
       	          				
        return "redirect:/accesscontrol/users";
    }
    
	/**
     * Adiciona uma nova permissão a um usário existente
     * @param model
     * @return formulário para preencher os dados da nova permissão
     */
    @RequestMapping("/accesscontrol/add")
    public String add(Model model) {
    	checkUser();
    	
        model.addAttribute("access", new Role());        
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);

        return "accesscontrol/form";
    }

    /**
     * Salva a permissão selecionada
     * @param authorities
     * @param ra
     * @return volta para a página de lista de permissões
     */
    @RequestMapping(value = "/accesscontrol/save", method = RequestMethod.POST)
    public String save(Role authorities, final RedirectAttributes ra) {   	  
    	
    	Role save = authoritiesService.save(authorities);
    	ra.addFlashAttribute("successFlash", "Permissão salva com sucesso.");

    	return "redirect:/accesscontrol";	
    }
    
    /**
     * TODO: Revisar a operação de edicão de permissão
     * Edita a permissão selecionada
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/accesscontrol/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
    	checkUser();
    	
        model.addAttribute("access", authoritiesService.get(id));        
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("loginuser", loginUser);

        return "accesscontrol/formEdit";
    }

}
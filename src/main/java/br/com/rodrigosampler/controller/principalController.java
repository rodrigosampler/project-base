package br.com.rodrigosampler.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.rodrigosampler.model.Usuario;
import br.com.rodrigosampler.service.LoginFacebook;

@Controller
@RequestMapping(path="/")
public class principalController {
	
	@Autowired
	private LoginFacebook loginFacebook;

	/**
	 * Método que chama URL do facebook onde o usuário poderá autorizar a aplicação
	 * a acessar seus recursos privados.
	 * @return
	 */
	@RequestMapping("loginfb")
	public String logarComFacebook(){
		return "redirect:"+loginFacebook.getLoginRedirectURL();
	}

	/**
	 * Executado quando o Servidor de Autorização fizer o redirect.
	 * @param 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@RequestMapping("loginfbresponse")
	public String logarComFacebook(String code) throws MalformedURLException, IOException{
		
		loginFacebook.obterUsuarioFacebook(code);
		
		return "redirect:/";
	}
	
	@RequestMapping(path="")
	public ModelAndView loginUsuario() {
		ModelAndView model = new ModelAndView("formLoginUsuario");
		model.addObject("usuario", new Usuario());
		return model;
	}
	
	@RequestMapping(path="erroLogin")
	public String loginErrorUsuario(RedirectAttributes attributes) {
		attributes.addFlashAttribute("mensagem", "Email ou senha incorretos!");
		return "redirect:/";
	}

}

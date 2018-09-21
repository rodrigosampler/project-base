package br.com.rodrigosampler.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.rodrigosampler.model.Papel;
import br.com.rodrigosampler.model.Usuario;
import br.com.rodrigosampler.service.LoginFacebook;
import br.com.rodrigosampler.service.UsuarioService;
import br.com.rodrigosampler.util.Constants;



@Controller
@RequestMapping(path="/usuario")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	private LoginFacebook loginFacebook;

	/**
	 * Método que chama URL do facebook onde o usuário poderá autorizar a aplicação
	 * a acessar seus recursos privados.
	 * @return
	 */
	@RequestMapping(path="/loginfb")
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
	@RequestMapping(path="/loginfbresponse")
	public String logarComFacebook(String code) throws MalformedURLException, IOException{
		
		loginFacebook.obterUsuarioFacebook(code);
		
		return "redirect:/";
	}
	
	@GetMapping(path="/meus_eventos")
	public ModelAndView meusEventos(){
		ModelAndView model = new ModelAndView("meusEventos");
		Usuario logado = usuarioService.getUsuarioLogado();	
		return model;
	}
	
	@GetMapping(path = "/cadastrar")
	public ModelAndView cadastroUsuario(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("formCadastroUsuario");
		model.addObject(new Usuario());
		return model;
	}
	
	@PostMapping(path = "/cadastrar")
	public String salvarUsuario(@Valid Usuario usuario, BindingResult result, @RequestParam(value="imagem", required=false) MultipartFile imagem, RedirectAttributes attributes) throws IOException {
		if (result.hasErrors()) return "formCadastroUsuario";
		usuario.setPapel(Papel.USUARIO);
		
		Usuario userBanco = null;
		try{
			userBanco = usuarioService.salvarUsuario(usuario);
		}catch (Exception e) {
			attributes.addFlashAttribute("mensagem", "Erro ao cadastrar novo usuário (" + e.getMessage() + ")");
			return "redirect:/usuario/cadastrar";
		}
				

		
		if(imagem != null && !imagem.isEmpty())
			userBanco.setFoto64(imagemBase64(imagem));
		else
			userBanco.setFoto64(Constants.IMAGE_DEFAULT);
		
		usuarioService.atualizaUsuario(userBanco);
		
		return "redirect:/evento/meus_eventos";

	}
	
	private String imagemBase64(MultipartFile imagem) throws IOException{
		StringBuilder imagem64 = new StringBuilder();
		imagem64.append("data:image/png;base64,");
		imagem64.append(StringUtils.newStringUtf8(Base64.encodeBase64(imagem.getBytes(), false)));
		return imagem64.toString();
	}
	
	
	@GetMapping(path = "/editar")
	public ModelAndView editarUsuarioForm() {
		ModelAndView model = new ModelAndView("editarUsuario");
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		model.addObject("usuario", usuarioLogado);
		return model;
	}
	
	@PostMapping(path = "/editar")
	public String editarUsuario(Usuario usuario, @RequestParam String senhaAtual, @RequestParam(value="imagem", required=false) MultipartFile imagem, RedirectAttributes attributes) throws IOException {
		Usuario usuarioLogado = usuarioService.getUsuarioLogado();
		Usuario usuarioBanco = usuarioService.getUsuario(usuarioLogado.getEmail());
		
		if(imagem != null && !imagem.isEmpty())
			usuarioBanco.setFoto64(imagemBase64(imagem));
		
		if(!usuario.getNome().isEmpty())
			usuarioBanco.setNome(usuario.getNome());
		if(!usuario.getEmail().isEmpty())
			usuarioBanco.setEmail(usuario.getEmail());
		usuarioBanco = usuarioService.atualizaUsuario(usuarioBanco);
		
		if(senhaAtual != null && !senhaAtual.isEmpty()){
			if(usuarioService.compararSenha(usuarioBanco.getSenha(), senhaAtual)){
				usuarioBanco.setSenha(usuario.getSenha());
				usuarioService.salvarUsuario(usuarioBanco);
			}
		}
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(usuarioBanco, usuarioBanco.getSenha(), usuarioBanco.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		attributes.addFlashAttribute("mensagem", "Alteração realizada com sucesso!");
		
		return "redirect:/usuario/editar";
	}	
	
	
	@GetMapping(path="/starter")
	public String starter(){
		return "starter";
	}
	
	@GetMapping(path="/recuperarsenha")
	public String recuperarsenha(){
		return "formRecuperarSenha";
	}
	

	
	@GetMapping(path="/alterarsenha/{token}")
	public ModelAndView alterarSenha(@PathVariable String token){
		ModelAndView model = new ModelAndView("formAlterarSenha");
		model.addObject("token", token);
		return model;
	}
	

}

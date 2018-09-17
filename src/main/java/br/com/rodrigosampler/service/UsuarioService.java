package br.com.rodrigosampler.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.rodrigosampler.model.Token;
import br.com.rodrigosampler.model.Usuario;
import br.com.rodrigosampler.repository.UsuarioRepository;


@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UsuarioService() {
		bCryptPasswordEncoder = new BCryptPasswordEncoder();
	}
	
	public Usuario salvarUsuario(Usuario usuario){
		usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));
		return usuarioRepository.save(usuario);
	}
	
	public Usuario atualizaUsuario(Usuario usuario){
		return usuarioRepository.save(usuario);
	}
	
	public boolean logar(String email, String senha){
		Usuario userBanco = usuarioRepository.findByEmail(email);
		if(userBanco != null && new BCryptPasswordEncoder().matches(senha, userBanco.getSenha())) return true;
		else return false;
	}
	
	public boolean logar(Usuario usuario){
		Usuario userBanco = usuarioRepository.findByEmail(usuario.getEmail());
		if(userBanco != null && new BCryptPasswordEncoder().matches(usuario.getSenha(), userBanco.getSenha())) return true;
		else return false;
	}
	
	public boolean compararSenha(String senhaCrip, String senhaLimpa){
		if(new BCryptPasswordEncoder().matches(senhaLimpa, senhaCrip)) return true;
		else return false;
	}
	
	public List<Usuario> getTodosUsuarios(){
		return usuarioRepository.findAll();
	}
	
	public Usuario getUsuario(String email){
		return usuarioRepository.findByEmail(email);
	}
	
	
	public Usuario getUsuarioLogado(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		Usuario usuarioLogado = this.getUsuario(email);
		return usuarioLogado;
	}
	

}

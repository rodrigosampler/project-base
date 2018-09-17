package br.com.rodrigosampler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import br.com.rodrigosampler.model.Token;
import br.com.rodrigosampler.model.Usuario;
import br.com.rodrigosampler.repository.TokenRepository;

@Service
@ComponentScan("br.ufc.quixada.npi.service")
public class TokenService {
	
	@Autowired
	private TokenRepository tokenRepository;
	

	public Token buscarPorUsuario(Usuario usuario) {
		return tokenRepository.findByUsuario(usuario);
	}


	public void salvar(Token token) {
		tokenRepository.save(token);
	}

	public void deletar(Token token) {
		tokenRepository.delete(token);
	}
	
}

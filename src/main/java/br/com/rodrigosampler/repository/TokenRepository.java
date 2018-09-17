package br.com.rodrigosampler.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rodrigosampler.model.Token;
import br.com.rodrigosampler.model.Usuario;


@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, String>{

	Token findByUsuario(Usuario usuario);
}

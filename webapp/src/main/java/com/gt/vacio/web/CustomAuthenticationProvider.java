package com.gt.vacio.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.gt.vacio.web.model.usuarios.UserRol;
import com.gt.vacio.web.model.usuarios.Usuario;
import com.gt.vacio.web.repo.sistema.UsuarioRepo;

/**
 * Created by aLeXcBa1990 on 24/11/2018.
 * 
 */

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	UsuarioRepo usuarioRepo;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		List<GrantedAuthority> grantedAuths = new ArrayList<>();

		Usuario usuario = null;

		if (usuarioRepo.count() == 0) {
			usuario = new Usuario();
			usuario.setCodigo(1);
			usuario.setActivo(true);
			usuario.setFechaAlta(new Date());
			usuario.setUsername(username);
			usuario.setNombre("Administrador del sistema");
			usuario.setAndEncryptPassword(password);
			usuario.setObservaciones("Creado como SYSADMIN al no encontrar usuarios");
			usuario.getRoles().add(UserRol.SYSADMIN);

			usuarioRepo.save(usuario);

		} else {

			usuario = usuarioRepo.findByUsernameAndPassword(username,
					org.apache.commons.codec.digest.DigestUtils.sha256Hex(password)).orElse(null);

		}

		if (usuario != null) {
			for (UserRol rol : usuario.getRoles()) {
				grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + rol.name()));
			}
		}

		if (grantedAuths.size() > 0) {
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password,
					grantedAuths);
			auth.setDetails(usuario);
			return auth;
		}

		return null;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {

		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));

	}

}

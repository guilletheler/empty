package com.gt.vacio.web.bean;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gt.vacio.web.model.usuarios.Usuario;

import lombok.Getter;

@Named
@ViewScoped
public class SessionMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	private Usuario currentUser;

	@PostConstruct
	public void init() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null && context.getAuthentication() != null) {
			currentUser = (Usuario) context.getAuthentication().getDetails();
		}
	}

	public boolean inRole(String roles) {
		if (currentUser == null) {
			return false;
		}

		List<String> strRolesList = currentUser.getRoles().stream().map(userRol -> userRol.name())
				.collect(Collectors.toList());

		for (String rol : roles.split(",")) {
			if (strRolesList.contains(rol.trim())) {
				return true;
			}
		}

		return false;
	}
}

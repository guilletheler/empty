package com.gt.vacio.web.controller.sistema;

import static com.github.adminfaces.template.util.Assert.has;
import static com.gt.toolbox.spb.webapps.commons.infra.utils.Utils.addDetailMessage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ValidationException;

import org.omnifaces.util.Faces;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.gt.vacio.web.dto.ClaveValorDto;
import com.gt.vacio.web.model.usuarios.UserRol;
import com.gt.vacio.web.model.usuarios.Usuario;
import com.gt.vacio.web.service.sistema.UsuarioService;
import com.gt.toolbox.spb.webapps.commons.infra.utils.Utils;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class UsuarioEditController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	UsuarioService usuarioService;

	@Getter
	@Setter
	Integer id;

	@Getter
	@Setter
	Usuario usuario;

	@Getter
	String password;

	@Getter
	@Setter
	List<ClaveValorDto<String, String>> parametros;

	@Getter
	@Setter
	List<ClaveValorDto<String, String>> telegramChatIds;

	public void init() {
		if (Faces.isAjaxRequest()) {
			return;
		}
		if (has(id)) {
			usuario = usuarioService.getRepo().findById(id).orElse(new Usuario());
		} else {
			usuario = new Usuario();
		}
		if (usuario.getId() == null) {
			usuario.setCodigo(usuarioService.getRepo().nextCodigo());
			usuario.getRoles().add(UserRol.USUARIO);
			usuario.setNombre("Usuario " + usuario.getCodigo());
			usuario.setUsername("usuario" + usuario.getCodigo());
			setPassword(usuario.getUsername());
		}
		cargarParametros();
	}

	public void remove() throws IOException {
		if (!Utils.isUserInRole("ROLE_SYSADMIN")) {
			throw new AccessDeniedException(
					"Usuario no autorizado! Solo el rol <b>SYSADMIN</b> puede borrar usuarios.");
		}
		if (has(usuario) && has(usuario.getId())) {
			usuarioService.getRepo().delete(usuario);
			addDetailMessage("Usuario " + usuario.getNombre() + " borrado exitosamente");
			Faces.getFlash().setKeepMessages(true);
			Faces.redirect("user/car-list.jsf");
		}
	}

	public String save() {
		String msg = "Usuario " + usuario.getNombre();
		String ret = null;
		Severity severity = FacesMessage.SEVERITY_INFO;

		acomodarParametros();

		try {
			usuario = usuarioService.save(usuario);
			if (usuario.getId() == null) {
				msg += " creado exitosamente";
			} else {
				msg += " editado exitosamente";
			}

			addDetailMessage(msg);

			ret = "ListUsuarios?faces-redirect=true";
		} catch (ValidationException vex) {
			msg = "Error:\n" + vex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
		}

		addDetailMessage(msg, severity);

		return ret;
	}

	public void clear() {
		usuario = new Usuario();
		id = null;
	}

	public boolean isNew() {
		return usuario == null || usuario.getId() == null;
	}

	public void setPassword(String password) {
		this.password = password;
		if (password != null && !password.isEmpty()) {
			usuario.setAndEncryptPassword(password);
		}
	}

	private void cargarParametros() {
		parametros = new ArrayList<>();
		for (Map.Entry<String, String> param : usuario.getParametros().entrySet()) {
			parametros.add(new ClaveValorDto<>(param.getKey(), Optional.ofNullable(param.getValue()).orElse("")));
		}
		for (int i = 0; i < 10; i++) {
			parametros.add(new ClaveValorDto<>("PARAMETRO_" + i, ""));
		}

	}

	private void acomodarParametros() {
		usuario.getParametros().clear();
		List<ClaveValorDto<String, String>> params = new ArrayList<>();
		boolean esta = false;
		for (ClaveValorDto<String, String> param : parametros) {
			if (param.getClave() != null && !param.getClave().isEmpty()) {
				esta = false;
				for (int i = 0; i < 10; i++) {
					if (Objects.equals(param.getClave(), "PARAMETRO_" + i)) {
						esta = true;
						break;
					}
				}
				if (!esta) {
					params.add(param);
				}
			}
		}
		for (ClaveValorDto<String, String> param : params) {
			usuario.getParametros().put(param.getClave(), param.getValor());
		}
	}

}

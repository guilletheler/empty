package com.gt.vacio.web.controller.sistema;

import static com.gt.toolbox.spb.webapps.commons.infra.utils.Utils.addDetailMessage;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import com.gt.toolbox.spb.webapps.commons.infra.datamodel.EntityLazyDataModel;
import com.gt.vacio.web.model.usuarios.Usuario;
import com.gt.vacio.web.service.sistema.UsuarioService;

import lombok.Getter;

@Named
@ViewScoped
public class UsuarioListController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	UsuarioService usuarioService;

	@Getter
	LazyDataModel<Usuario> lazyDataModel;

	@PostConstruct
	private void init() {
		lazyDataModel = new EntityLazyDataModel<>(usuarioService);
	}

	public void borrarUsuario(Usuario usuario) {
		usuarioService.getRepo().delete(usuario);
		addDetailMessage("Usuario " + usuario.getNombre() + " borrado exitosamente");
	}
}

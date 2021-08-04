package com.gt.vacio.web.controller.sistema;

import static com.github.adminfaces.template.util.Assert.has;
import static com.gt.vacio.web.util.Utils.addDetailMessage;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.gt.vacio.web.model.sistema.AppParam;
import com.gt.vacio.web.service.sistema.AppParamService;
import com.gt.vacio.web.util.Utils;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class AppParamEditController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
 	AppParamService appParamService;

	@Getter
	@Setter
	Integer id;

	@Getter
	@Setter
	AppParam appParam;
	
	public void init() {
		if (Faces.isAjaxRequest()) {
			return;
		}
		if (has(id)) {
			appParam = appParamService.getRepo().findById(id).orElse(new AppParam());
		} else {
			appParam = new AppParam();
		}
		if(appParam.getId() == null) {
			appParam.setCodigo(appParamService.getRepo().nextCodigo());
			appParam.setNombre("AppParam " + appParam.getCodigo());
		}
	}

	public void remove() throws IOException {
		if (!Utils.isUserInRole("ROLE_SYSADMIN")) {
			throw new AccessDeniedException("AppParam no autorizado! Solo el rol <b>SYSADMIN</b> puede borrar appParams.");
		}
		if (has(appParam) && has(appParam.getId())) {
			appParamService.getRepo().delete(appParam);
			addDetailMessage("AppParam " + appParam.getNombre() + " borrado exitosamente");
			Faces.getFlash().setKeepMessages(true);
			Faces.redirect("ListParametros");
		}
	}

	public String save() {
		String msg = "AppParam " + appParam.getNombre();
		
		appParamService.getRepo().save(appParam);
		
		if (appParam.getId() == null) {
			msg += " creado exitosamente";
		} else {
			msg += " editado exitosamente";
		}
		addDetailMessage(msg);
		
		return "ListParametros?faces-redirect=true";
	}

	public void clear() {
		appParam = new AppParam();
		id = null;
	}

	public boolean isNew() {
		return appParam == null || appParam.getId() == null;
	}
	
}

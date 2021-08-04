package com.gt.vacio.web.controller.sistema;

import static com.gt.vacio.web.util.Utils.addDetailMessage;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import com.gt.vacio.web.infra.model.EntityLazyDataModel;
import com.gt.vacio.web.model.sistema.AppParam;
import com.gt.vacio.web.service.sistema.AppParamService;

import lombok.Getter;

@Named
@ViewScoped
public class AppParamListController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	AppParamService appParamService;

	@Getter
	LazyDataModel<AppParam> lazyDataModel;

	@PostConstruct
	private void init() {
		lazyDataModel = new EntityLazyDataModel<>(appParamService);
	}

	public void borrarAppParam(AppParam appParam) {
		appParamService.getRepo().delete(appParam);
		addDetailMessage("AppParam " + appParam.getNombre() + " borrado exitosamente");
	}
}

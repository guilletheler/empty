/**
 * Generado con CRUDO
 * C:\Users\guill\prog\java_mvn\vacio\webvacio\src\main\java\com\gt\vacio\web\controller\sistema\LogEntryListController.java
 */
package com.gt.vacio.web.controller.sistema;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import com.gt.toolbox.spb.webapps.commons.infra.datamodel.EntityLazyDataModel;
import com.gt.vacio.web.model.usuarios.LogEntry;
import com.gt.vacio.web.service.sistema.LogEntryService;

import lombok.Getter;

@Named
@ViewScoped
public class LogEntryListController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	LogEntryService logEntryService;

	@Getter
	LazyDataModel<LogEntry> lazyDataModel;

	@PostConstruct
	private void init() {
		lazyDataModel = new EntityLazyDataModel<>(logEntryService, LogEntry.class, "id");
	}
}

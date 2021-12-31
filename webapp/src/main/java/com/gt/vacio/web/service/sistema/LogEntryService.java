package com.gt.vacio.web.service.sistema;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gt.toolbox.spb.webapps.commons.infra.datamodel.SelectableLazyDMFiller;
import com.gt.vacio.web.model.sistema.AppParam;
import com.gt.vacio.web.model.usuarios.LogEntry;
import com.gt.vacio.web.model.usuarios.Usuario;
import com.gt.vacio.web.repo.sistema.LogEntryRepo;
import com.gt.vacio.web.service.QueryHelper;

import lombok.Getter;
import lombok.extern.java.Log;

@Log
@Service
public class LogEntryService implements SelectableLazyDMFiller<LogEntry> {

	@Getter
	@Autowired
	LogEntryRepo logEntryRepo;

	@Autowired
	AppParamService appParamService;
	
	@Autowired
	UsuarioService usuarioService;

	public Page<LogEntry> findByFilter(Map<String, String> filters, Pageable pageable) {
		return logEntryRepo.findAll(QueryHelper.getFilterSpecification(filters), pageable);
	}

	@Override
	public LogEntry findById(Object id) {
		if (id == null) {
			return null;
		}
		return logEntryRepo.findById((Integer) id).orElse(null);
	}

	public void registrar(String texto) {
		registrar(texto, usuarioService.getCurrentUser());
	}

	public void registrar(String texto, Usuario usuario) {
		LogEntry logEntry = new LogEntry();
		logEntry.setFechaHora(new Date());
		logEntry.setUsuario(usuario);
		logEntry.setDetalle(texto);

		logEntryRepo.save(logEntry);
	}

	public void rotarBitacora() {

		AppParam paramRegistrosBitacora = appParamService.findOrCreateParam("REGISTROS_BITACORA", "1000");

		try {
			registrar("Eliminando los registros de la bitacora que superan los " + paramRegistrosBitacora.getValor());
			getLogEntryRepo().deleteOverflow(Integer.parseInt(paramRegistrosBitacora.getValor()));
		} catch (Exception ex) {
			log.severe("Error al eliminar el excedente de la bitácora");
		}
	}
}

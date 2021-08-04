package com.gt.vacio.web.repo.sistema;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gt.vacio.web.model.usuarios.LogEntry;
import com.gt.vacio.web.model.usuarios.Usuario;

@Repository
@Transactional
public interface LogEntryRepo extends PagingAndSortingRepository<LogEntry, Integer>,
		JpaSpecificationExecutor<LogEntry> {

	Optional<LogEntry> findByUsuario(Usuario usuario);

	void deleteByFechaHoraBefore(Date fecha);

	@Modifying
	@Query(value = "DELETE FROM logfile "
			+ "WHERE id NOT IN (SELECT id FROM logfile ORDER BY id DESC LIMIT :keepRows);", nativeQuery = true)
	void deleteOverflow(int keepRows);
}

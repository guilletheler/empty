package com.gt.vacio.web.repo.sistema;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gt.vacio.web.model.sistema.AppParam;

@Repository
public interface AppParamRepo extends PagingAndSortingRepository<AppParam, Integer>,
		JpaSpecificationExecutor<AppParam> {

	Optional<AppParam> findByNombre(String name);
	
	@Query("SELECT COALESCE(MAX(ap.codigo), 0) + 1 FROM AppParam ap")
	Integer nextCodigo();
	
	Boolean existsByNombre(String nombre);
}

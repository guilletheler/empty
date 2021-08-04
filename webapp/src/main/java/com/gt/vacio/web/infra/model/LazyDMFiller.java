package com.gt.vacio.web.infra.model;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LazyDMFiller<E> {

	Page<E> findByFilter(Map<String, String> filters, Pageable pageable);
}

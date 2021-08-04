package com.gt.vacio.web.infra.model;

public interface SelectableLazyDMFiller<E> extends LazyDMFiller<E> {

	E findById(Object id);
}

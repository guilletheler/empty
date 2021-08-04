package com.gt.vacio.web.bean;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class EntityManagerHelperMB {

	@PersistenceContext
	EntityManager em;

	public EntityManager getEm() {
		return em;
	}
}

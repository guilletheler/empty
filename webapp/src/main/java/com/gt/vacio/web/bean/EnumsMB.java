package com.gt.vacio.web.bean;

import javax.ejb.Singleton;
import javax.inject.Named;

import com.gt.vacio.web.model.usuarios.UserRol;

/**
 * Clase que devuelve los valores de los enums
 * 
 * @author PortalTheler
 *
 */
@Named
@Singleton
public class EnumsMB {

	public UserRol[] getRoles() {
		return UserRol.values();
	}

}

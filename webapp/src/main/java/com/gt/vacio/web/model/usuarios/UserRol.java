/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.vacio.web.model.usuarios;

import java.util.Objects;

import lombok.Getter;

/**
 *
 * @author guille
 */
public enum UserRol {
    SYSADMIN("ADMINISTRADOR DE SISTEMAS", "ABM usuarios, ABM parámetros de sistema"),
    TECNICO("TECNICO", "ABM bases, ABM fórmulas, ABM empresas"),
    CLIENTE("CLIENTE", "ABM productos"),
    USUARIO("CAMBIO DE CONTRASEÑA", ""),
    WEBSERVICES("WEBSERVICES", ""),
    AYUDA("AYUDA", "Muestra la ayuda del sistema");

	@Getter
    String nombre;
	
    @Getter
    String descripcion;

    private UserRol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public static UserRol parse(String nombre) {
    	for(UserRol userRol : UserRol.values()) {
    		if(Objects.equals(nombre, userRol.getNombre())) {
    			return userRol;
    		}
    	}
    	return null;
    }
}

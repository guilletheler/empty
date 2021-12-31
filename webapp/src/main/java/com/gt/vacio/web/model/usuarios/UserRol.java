/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.vacio.web.model.usuarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;

/**
 *
 * @author guille
 */
public enum UserRol {
    SYSADMIN("ADMINISTRADOR DE SISTEMAS", "ABM usuarios, ABM parámetros de sistema", "", "/pages/sistema/**"),
    USUARIO("CAMBIO DE CONTRASEÑA", "", "SYSADMIN", "/pages/usuario/**"),
    WEBSERVICES("WEBSERVICES", "", "SYSADMIN", "/ws/**"),
    AYUDA("AYUDA", "Muestra la ayuda del sistema", "SYSADMIN", "/pages/ayuda/**");

    @Getter
    String nombre;

    @Getter
    String descripcion;

    @Getter
    String padres;

    @Getter
    String[] folders;

    private UserRol(String nombre, String descripcion,String padres, String... folders) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.folders = folders;
    }

    public static UserRol parse(String nombre) {
        for (UserRol userRol : UserRol.values()) {
            if (Objects.equals(nombre, userRol.getNombre())) {
                return userRol;
            }
        }
        return null;
    }

    public static List<UserRol> parseList(String roles) {

        List<UserRol> ret = new ArrayList<>();

        for(String str : roles.split(",")) {
            UserRol userRol = parse(str.trim().toUpperCase());
            if(userRol != null) {
                ret.add(userRol);
            }
        }
        return ret;
    }

    /**
     * Lista de usuarios con persmisos para las diferentes páginas
     * @return
     */
    public static Map<String, Set<String>> getPermissionsMap() {
        Map<String, Set<String>> ret = new HashMap<>();

        for(UserRol userRol : UserRol.values()) {
            for(String folder : userRol.getFolders()) {

                if(folder == null || folder.isEmpty()) {
                    continue;
                }

                if(!ret.containsKey(folder)) {
                    ret.put(folder, new HashSet<>());
                }

                ret.get(folder).add(userRol.name());

                for(UserRol padres : UserRol.parseList(userRol.getPadres())) {
                    ret.get(folder).add(padres.name());
                }
            }
        }

        return ret;
    }

}

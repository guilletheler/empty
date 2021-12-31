/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.vacio.web.model.sistema;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gt.toolbox.spb.webapps.commons.infra.model.IWithIntegerId;
import com.gt.toolbox.spb.webapps.commons.infra.model.IWithObservaciones;
import com.gt.vacio.web.model.CodigoNombre;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author guillermo
 */
@Entity
@Table(name = "parametros")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppParam extends CodigoNombre implements IWithIntegerId, IWithObservaciones, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 10000)
	private String valor;
	
    @Column(length = 10000)
    private String observaciones;

}

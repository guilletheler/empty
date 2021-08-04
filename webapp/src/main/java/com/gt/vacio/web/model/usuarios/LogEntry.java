/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.vacio.web.model.usuarios;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

import com.gt.vacio.web.model.IWithIntegerId;

import lombok.Data;

/**
 *
 * @author guille
 */
@Entity(name = "logfile")
@Data
public class LogEntry implements IWithIntegerId, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private Usuario usuario;

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date fechaHora;

	@Column(length = 10000)
	private String detalle;

}

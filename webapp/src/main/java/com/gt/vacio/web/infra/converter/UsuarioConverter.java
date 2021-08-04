package com.gt.vacio.web.infra.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import com.gt.vacio.web.bean.EntityManagerHelperMB;
import com.gt.vacio.web.model.usuarios.Usuario;

@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter<Usuario> {

	@Override
	public Usuario getAsObject(FacesContext context, UIComponent component, String value) {
		EntityManagerHelperMB rhb = context.getApplication().evaluateExpressionGet(context, "#{entityManagerHelperMB}",
				EntityManagerHelperMB.class);
		EntityManager em = rhb.getEm();
		Usuario tmp = em.find(Usuario.class, Integer.valueOf(value));
		return tmp;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Usuario value) {
		return value.getId().toString();
	}

}

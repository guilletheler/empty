package com.gt.vacio.web.infra.model;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;

public class LazySorter<T> implements Comparator<T> {


	private Method method;
	private SortOrder sortOrder;;

	public LazySorter(Class<T> clazz, String sortField, SortOrder sortOrder) {
		this.sortOrder = sortOrder;
		String methodName = "get" + StringUtils.capitalize(sortField);
		try {
			method = clazz.getMethod(methodName);
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No se puede obtener el método " + methodName);
		}
	}

	public int compare(T comprobante1, T comprobante2) {
		try {
			Object value1 = method.invoke(comprobante1);
			Object value2 = method.invoke(comprobante2);

			@SuppressWarnings({ "unchecked", "rawtypes" })
			int value = ((Comparable) value1).compareTo(value2);

			return Objects.equals(sortOrder, SortOrder.ASCENDING) ? value : -1 * value;
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al aplicar filtro", e);

			return 0;
		}
	}
}

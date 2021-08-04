package com.gt.vacio.web.infra.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.gt.vacio.web.util.Utils;

public class CollectionLazyDataModel<T> extends LazyDataModel<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<T> datasource;

	Method rowKeyMethod;

	private Class<T> clazz;

	public CollectionLazyDataModel(Class<T> clazz, List<T> data) {
		this(clazz, data, "id");
	}

	public CollectionLazyDataModel(Class<T> clazz, List<T> data, String rowKey) {
		super();

		this.datasource = data;
		this.setRowCount(data.size());
		this.clazz = clazz;
		try {
			rowKeyMethod = clazz.getMethod("get" + StringUtils.capitalize(rowKey));
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error obteniendo metodo para rowKey");
		}
	}

	public void setDatasource(List<T> data) {
		this.datasource = data;
		this.setRowCount(data.size());
	}

	@Override
	public T getRowData(String rowKey) {
		for (T object : datasource) {
			if (Objects.equals(getRowKey(object).toString(), rowKey)) {
				return object;
			}
		}

		return null;
	}

	@Override
	public Object getRowKey(T object) {
		try {
			return rowKeyMethod.invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error obteniendo columna del objeto");
		}
		return null;
	}

	@Override
	public List<T> load(int first, int pageSize, Map<String, SortMeta> sortMeta,
			Map<String, FilterMeta> filterMeta) {
		List<T> data = new ArrayList<>();

		// filter
		for (T comprobante : datasource) {
			boolean match = true;

//			String filtrosLog = filterMeta.isEmpty() ? "Sin filtros" : ("filtrando " + filterMeta.toString());
			
			if (filterMeta != null) {
				
				for (FilterMeta meta : filterMeta.values()) {
					try {
						String filterField = meta.getFilterField();
						Object filterValue = meta.getFilterValue();
						
						if (filterValue == null) {
//							filtrosLog += " filterValue es nulo";
							continue;
						}

						String methodName = StringUtils.capitalize(filterField);

						Object fieldValue = comprobante.getClass().getMethod("get" + methodName).invoke(comprobante);

						if (fieldValue == null) {
//							filtrosLog += " field value es nulo";
							match = false;
						} else if (Objects.equals(fieldValue.getClass(), Boolean.class)
								|| Objects.equals(fieldValue.getClass(), boolean.class)) {
							Boolean filterVal = String.valueOf(filterValue).equalsIgnoreCase("1")
									|| String.valueOf(filterValue).equalsIgnoreCase("True")
									|| String.valueOf(filterValue).equalsIgnoreCase("V")
									|| String.valueOf(filterValue).equalsIgnoreCase("Verdadero")
									|| String.valueOf(filterValue).equalsIgnoreCase("Si");
							
//							filtrosLog += " buscando un valor boolean " + filterVal + " para el campo " + methodName;

							match = Objects.equals((Boolean) fieldValue, filterVal);
						} else if (Objects.equals(fieldValue.getClass(), Date.class)) {
							if (filterValue.toString().contains("-")) {
								// 2 fechas
								String[] fechas = String.valueOf(filterValue).split("-");

								Date desde;
								Date hasta;

								try {
									desde = Utils.SDF_SLASH_DMYY.parse(fechas[0]);
								} catch (ParseException e) {
									Calendar cal = Calendar.getInstance();
									cal.set(1900, 0, 1);
									desde = cal.getTime();
								}

								try {
									hasta = Utils.SDF_SLASH_DMYY.parse(fechas[1]);
								} catch (ParseException | ArrayIndexOutOfBoundsException e) {
									hasta = DateUtils.ceiling(new Date(), Calendar.DAY_OF_MONTH);
								}

								Date tmp = (Date) fieldValue;
								
//								filtrosLog += " buscando un valor fecha entre 2 fechas para el campo " + methodName;

								match = (DateUtils.isSameDay(tmp, desde) || tmp.after(desde))
										&& (DateUtils.isSameDay(tmp, hasta) || tmp.before(hasta));
							} else {
//								filtrosLog += " buscando un valor de fecha para el campo " + methodName;
								
								match = Utils.SDF_SLASH_DMYY.format((Date) fieldValue).contains(String.valueOf(filterValue));
							}
						} else if (Number.class.isAssignableFrom(fieldValue.getClass())) {
							if (String.valueOf(filterValue).startsWith("0")) {
//								filtrosLog += " buscando un valor de numero exacto de " + fieldValue + " para el campo " + methodName;
								match = Objects.equals(Double.valueOf(String.valueOf(filterValue)),
										Double.valueOf(String.valueOf(fieldValue)));
							} else {
//								filtrosLog += " buscando un valor de numero " + fieldValue + " para el campo " + methodName;
								match = String.valueOf(fieldValue).contains(String.valueOf(filterValue));
							}
						} else {
//							filtrosLog += " buscando un valor de string " + fieldValue + " para el campo " + methodName;
							match = String.valueOf(fieldValue).toLowerCase()
									.contains(String.valueOf(filterValue).toLowerCase());
						}

						if (!match) {
//							filtrosLog += " no matchea, descarto";
							break;
						}

					} catch (NullPointerException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error al filtrar collection",
								e);
						match = false;
					}
				}
			}
			
//			Logger.getLogger(getClass().getName()).log(Level.INFO, filtrosLog);

			if (match) {
				data.add(comprobante);
			}
		}

		// sort
		if (sortMeta != null && !sortMeta.isEmpty()) {
			for (SortMeta meta : sortMeta.values()) {
				Collections.sort(data, new LazySorter<>(clazz, meta.getSortField(), meta.getSortOrder()));
			}
		}

		// rowCount
		int dataSize = data.size();
		this.setRowCount(dataSize);

		List<T> ret;

		// paginate
		if (dataSize > pageSize) {
			try {
				ret = data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				ret = data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			ret = data;
		}

		Logger.getLogger(getClass().getName()).log(Level.INFO,
				"devolviendo " + ret.size() + " objetos de un total de " + datasource.size());

		return ret;
	}
}

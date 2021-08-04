package com.gt.vacio.web.infra.model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.gt.vacio.web.model.IWithId;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * Custom Lazy Presentacion DataModel which extends PrimeFaces LazyDataModel.
 * For more information please visit
 * http://www.primefaces.org/showcase-labs/ui/datatableLazy.jsf
 *
 * @param <E> Entidad
 */
public class EntityLazyDataModel<E> extends LazyDataModel<E> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final SortOrder DEFAULT_SORT_ORDER = SortOrder.ASCENDING;
//	private static final String DEFAULT_SORT_FIELD = "id";

	@Getter
	@Setter
	Map<String, FilterMeta> staticFilters;

	protected final LazyDMFiller<E> filler;

	Class<E> entityClass;

	protected String columnKey = null;

	@Getter
	@Setter
	protected String defaultSortField = "id";

	/**
	 *
	 * @param lazyDataProvider
	 * @param BeforeCloseEmEventListener
	 *
	 */
	public EntityLazyDataModel(LazyDMFiller<E> filler) {
		super();
		this.filler = filler;
	}

	/**
	 *
	 * @param lazyDataProvider
	 * @param BeforeCloseEmEventListener
	 *
	 */
	public EntityLazyDataModel(LazyDMFiller<E> filler, String columnKey) {
		this(filler);
		this.columnKey = columnKey;
	}

	public EntityLazyDataModel(LazyDMFiller<E> filler, Class<E> entityClass) {
		this(filler);
		this.entityClass = entityClass;
	}

	public EntityLazyDataModel(LazyDMFiller<E> filler, Class<E> entityClass, String columnKey) {
		this(filler, entityClass);
		this.columnKey = columnKey;
	}

	public EntityLazyDataModel(LazyDMFiller<E> filler, Class<E> entityClass, String columnKey,
			Map<String, FilterMeta> staticFilters) {
		this(filler, entityClass, columnKey);
		this.staticFilters = staticFilters;
	}

	@Override
	public List<E> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, FilterMeta> filters) {
		Sort sort = null;
		if (sortField != null) {
			sort = Sort.by(getDirection(sortOrder != null ? sortOrder : DEFAULT_SORT_ORDER), sortField);
		} else if (getDefaultSortField() != null) {
			sort = Sort.by(getDirection(sortOrder != null ? sortOrder : DEFAULT_SORT_ORDER), getDefaultSortField());
		}

		return filterAndSort(first, pageSize, filters, sort);
	}

	@Override
	public List<E> load(int first, int pageSize, Map<String, SortMeta> multiSortMeta, Map<String, FilterMeta> filters) {

		Sort sort = Sort.by(getDirection(DEFAULT_SORT_ORDER), getDefaultSortField());

		if (multiSortMeta != null) {
			List<Order> orders = multiSortMeta.values().stream()
					.map(m -> new Order(getDirection(m.getSortOrder() != null ? m.getSortOrder() : DEFAULT_SORT_ORDER),
							m.getSortField()))
					.collect(Collectors.toList());
			sort = Sort.by(orders);
		}
		return filterAndSort(first, pageSize, filters, sort);
	}

	private List<E> filterAndSort(int first, int pageSize, Map<String, FilterMeta> filters, Sort sort) {

		Map<String, String> filtersMap = new HashMap<>();

		if (staticFilters != null) {
			for (Map.Entry<String, FilterMeta> entry : staticFilters.entrySet()) {
				if (entry.getValue().getFilterValue() != null) {
					filtersMap.put(entry.getKey(), entry.getValue().getFilterValue().toString());
				}
			}
		}

		for (Map.Entry<String, FilterMeta> entry : filters.entrySet()) {
			if (entry.getValue().getFilterValue() != null) {
				filtersMap.put(entry.getKey(), entry.getValue().getFilterValue().toString());
			}
		}

		Page<E> page = filler.findByFilter(filtersMap, PageRequest.of(first / pageSize, pageSize, sort));

		if (page == null) {
			throw new RuntimeException("La página resultó nula, fist: " + first + " pageSize = " + pageSize);
		}

		this.setRowCount(((Number) page.getTotalElements()).intValue());

		this.setWrappedData(page.getContent());

		return page.getContent();
	}

	private static Direction getDirection(SortOrder order) {
		switch (order) {
		case ASCENDING:
			return Direction.ASC;
		case DESCENDING:
			return Direction.DESC;
		case UNSORTED:
		default:
			return null;
		}
	}

	@Override
	public E getRowData(String rowKey) {

		if (!SelectableLazyDMFiller.class.isAssignableFrom(filler.getClass())) {
			return super.getRowData(rowKey);
		}

		if (entityClass != null) {
			String getIdMethodName = null;

			if (columnKey != null) {
				getIdMethodName = "get" + columnKey.substring(0, 1).toUpperCase() + columnKey.substring(1);
			} else if (IWithId.class.isAssignableFrom(entityClass)) {
				getIdMethodName = "getId";
			}

			try {
				Method getIdMethod = entityClass.getMethod(getIdMethodName);
				Object typedId;
				if (Objects.equals(getIdMethod.getReturnType(), Integer.class)
						|| Objects.equals(getIdMethod.getReturnType(), int.class)) {
					typedId = Integer.valueOf(rowKey);
				} else if (Objects.equals(getIdMethod.getReturnType(), Long.class)
						|| Objects.equals(getIdMethod.getReturnType(), long.class)) {
					typedId = Long.valueOf(rowKey);
				} else {
					typedId = rowKey;
				}
				return ((SelectableLazyDMFiller<E>) filler).findById(typedId);
			} catch (SecurityException | NoSuchMethodException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al obtener RowData desde la clave rowKey", e);
			} 
		}
		return null;
	}

}

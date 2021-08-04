package com.gt.vacio.web.infra.model;

import java.util.Objects;

/**
 * Created by rmpestano on 10/31/14.
 */
public enum SortOrder {

    ASCENDING, DESCENDING, UNSORTED;

    public boolean isAscending() {
        return Objects.equals(this, ASCENDING);
    }
}

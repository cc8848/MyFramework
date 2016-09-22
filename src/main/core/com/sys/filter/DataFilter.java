package com.sys.filter;

import org.hibernate.Filter;
import org.hibernate.Session;

import java.util.*;

/**
 * Created by lenovo on 2014/12/21.
 */
public class DataFilter {
    private String filterName;
    private Map<String,Object> filterParams=new HashMap<>(0);

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public Map<String, Object> getFilterParams() {
        return filterParams;
    }

    public void setFilterParams(Map<String, Object> filterParams) {
        this.filterParams = filterParams;
    }

    public DataFilter() {
    }

    public DataFilter(String filterName, Map<String, Object> filterParams) {
        this.filterName = filterName;
        this.filterParams = filterParams;
    }

    public DataFilter(String filterName) {
        this.filterName = filterName;
    }
    public void addParam(String paramName,Object values){
        filterParams.put(paramName,values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataFilter filter = (DataFilter) o;

        if (filterName != null ? !filterName.equals(filter.filterName) : filter.filterName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return filterName != null ? filterName.hashCode() : 0;
    }
}

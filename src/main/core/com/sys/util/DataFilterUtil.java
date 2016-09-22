package com.sys.util;

import com.sys.entity.Client;
import com.sys.entity.SessionUser;
import com.sys.filter.DataFilter;
import com.sys.manager.ClientManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.hibernate.Filter;
import org.hibernate.Session;

import java.util.*;

/**
 * 数据过滤器
 */
public class DataFilterUtil {
    public static void enableFilters(Session session, Set<DataFilter> filters){
        if(CollectionUtils.isNotEmpty(filters)){
            for(DataFilter filter:filters){
                Filter filter1=session.enableFilter(filter.getFilterName());
                Map<String,Object> paramMap=filter.getFilterParams();
                if(MapUtils.isNotEmpty(paramMap)){
                    for(String param:paramMap.keySet()){
                        Object o=paramMap.get(param);
                        if(o!=null){
                            if(o.getClass().isArray()|| Collection.class.isAssignableFrom(o.getClass())){
                                Collection collection=null;
                                if(o.getClass().isArray()){
                                    collection=new ArrayList(Arrays.asList(o));
                                }else{
                                    collection= (Collection) o;
                                }

                                filter1.setParameterList(param,collection);
                            }else{
                                filter1.setParameter(param, o);
                            }
                        }
                    }
                }
            }
        }
    }
    public static void enableFilters(Session session){
        Client client= ClientManager.getInstance().getCurrentClient();
        if(client!=null){
           DataFilterUtil.enableFilters(session,client.getDataFilters());
        }
    }
}

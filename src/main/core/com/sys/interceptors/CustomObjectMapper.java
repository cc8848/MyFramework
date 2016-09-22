package com.sys.interceptors;


import com.sys.util.DateUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.map.ser.StdSerializers;
import org.codehaus.jackson.map.ser.std.DateSerializer;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 解决日期返回json时显示long
 * @author zzl
 * Date:2014-08-05
 */
public class CustomObjectMapper  extends ObjectMapper{
    public CustomObjectMapper(){
        this.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        CustomSerializerFactory factory = new CustomSerializerFactory();
        factory.addGenericMapping(Date.class, new DateSerializer());
      //  factory.addSpecificMapping(Timestamp.class,new DateSerializer(DateUtil.YYYYMMDDHHMINSS));
        this.setSerializerFactory(factory);
    }

}

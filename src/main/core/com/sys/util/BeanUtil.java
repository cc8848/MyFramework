package com.sys.util;

import com.code.entity.BaseEntity;
import com.code.entity.TableFieldBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ext.JodaSerializers;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.security.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类工具
 * Created by lenovo on 2014/7/28.
 */
public class BeanUtil extends BeanUtils {
    /**
     *生成查询条件
     * @param clazz
     * @param request
     * @return
     */
    public static List<Criterion> generateCriterions(Class clazz,HttpServletRequest request,boolean isfuzzy){

        return generateSubCriterions("",clazz,request,isfuzzy);
    }

    public static List<Criterion> generateSubCriterionsByPower(Class clazz,HttpServletRequest request,boolean isfuzzy,String qyDaIdList){

        return generateSubCriterionsByPower("",clazz,request,isfuzzy,qyDaIdList);
    }

    /**
     * 生成子查询条件方法
     * @param pre
     * @param clazz
     * @param request
     * @param isfuzzy
     * @return
     */
    public static List<Criterion> generateSubCriterionsByPower(String pre,Class clazz,HttpServletRequest request,boolean isfuzzy,String qyDaIdList){
        List<Criterion> criterions=new ArrayList<>();
        ConvertUtils.register(new DateConverter(),Date.class);
        Map<String,String[]> requestMap=request.getParameterMap();
        Map<String,Field> fieldMap=getFields(pre,clazz);
        Map<String,String> paramFieldMap=new HashMap<>();
        for (Field field:fieldMap.values()) {
            String name=field.getName();
            if(Objects.equals("class",name))
                continue;
            name=pre+name;
            if(isEntity(field.getType())){
                criterions.addAll(generateSubCriterionsByPower(name + ".", field.getType(), request, isfuzzy,qyDaIdList));
            }else{
                paramFieldMap.put(name,name);
                paramFieldMap.put(name+"_",name);
                paramFieldMap.put(name+"_begin",name);
                paramFieldMap.put(name+"_end",name);
            }

        }
        Set<String> paramSet=new HashSet<>(requestMap.keySet());
        paramSet.retainAll(paramFieldMap.keySet());
        for(String str :paramSet){
            String value=request.getParameter(str);
            if(StringUtils.isNotBlank(value)){
                if(value.endsWith("*")){
                    criterions.add(getCriterion(paramFieldMap.get(str),fieldMap.get(paramFieldMap.get(str)).getType(),value.substring(0,value.length()-1),true));
                }else{
                    if(str.endsWith("_begin")){
                        criterions.add(getGroupCriterion(paramFieldMap.get(str),fieldMap.get(paramFieldMap.get(str)).getType(),value,true));
                    }else if(str.endsWith("_end")){
                        criterions.add(getGroupCriterion(paramFieldMap.get(str),fieldMap.get(paramFieldMap.get(str)).getType(),value,false));
                    }else if(str.endsWith("_")){
                        criterions.add(getCriterion(paramFieldMap.get(str), fieldMap.get(paramFieldMap.get(str)).getType(), value, true));
                    }else {
                        String values[]=request.getParameterValues(str);
                        Set<String> valSet=new HashSet<>(Arrays.asList(values));
                        if(valSet.size()>1){
                            Set newValSet=new HashSet();
                            for(String str1:valSet){
                                if(StringUtils.isNotBlank(str1))
                                    newValSet.add(ConvertUtils.convert(str1,fieldMap.get(paramFieldMap.get(str)).getType()));
                            }
                            criterions.add(Restrictions.in(paramFieldMap.get(str),newValSet));
                        }else{
                            criterions.add(getCriterion(paramFieldMap.get(str),fieldMap.get(paramFieldMap.get(str)).getType(),value,isfuzzy));
                        }
                    }
                }
            }

        }
        return criterions;
    }



    /**
     * 生成子查询条件方法
     * @param pre
     * @param clazz
     * @param request
     * @param isfuzzy
     * @return
     */
    public static List<Criterion> generateSubCriterions(String pre,Class clazz,HttpServletRequest request,boolean isfuzzy){
        List<Criterion> criterions=new ArrayList<>();
        ConvertUtils.register(new DateConverter(),Date.class);
        Map<String,String[]> requestMap=request.getParameterMap();

        Map<String,Field> fieldMap=getFields(pre,clazz);
        Map<String,String> paramFieldMap=new HashMap<>();
        for (Field field:fieldMap.values()) {
                String name=field.getName();
                if(Objects.equals("class",name))
                    continue;
                name=pre+name;
                if(isEntity(field.getType())){
                    criterions.addAll(generateSubCriterions(name + ".", field.getType(), request, isfuzzy));
                }else{
                    paramFieldMap.put(name,name);
                    paramFieldMap.put(name+"_",name);
                    paramFieldMap.put(name+"_begin",name);
                    paramFieldMap.put(name+"_end",name);
                }

        }
        Set<String> paramSet=new HashSet<>(requestMap.keySet());
        paramSet.retainAll(paramFieldMap.keySet());
        for(String str :paramSet){
            String value=request.getParameter(str);
            if(StringUtils.isNotBlank(value)){
                if(value.endsWith("*")){
                    criterions.add(getCriterion(paramFieldMap.get(str),fieldMap.get(paramFieldMap.get(str)).getType(),value.substring(0,value.length()-1),true));
                }else{
                    if(str.endsWith("_begin")){
                        criterions.add(getGroupCriterion(paramFieldMap.get(str),fieldMap.get(paramFieldMap.get(str)).getType(),value,true));
                    }else if(str.endsWith("_end")){
                        criterions.add(getGroupCriterion(paramFieldMap.get(str),fieldMap.get(paramFieldMap.get(str)).getType(),value,false));
                    }else if(str.endsWith("_")){
                        criterions.add(getCriterion(paramFieldMap.get(str), fieldMap.get(paramFieldMap.get(str)).getType(), value, true));
                    }else {
                        String values[]=request.getParameterValues(str);
                        Set<String> valSet=new HashSet<>(Arrays.asList(values));
                        if(valSet.size()>1){
                            Set newValSet=new HashSet();
                            for(String str1:valSet){
                                if(StringUtils.isNotBlank(str1))
                                    newValSet.add(ConvertUtils.convert(str1,fieldMap.get(paramFieldMap.get(str)).getType()));
                            }
                            criterions.add(Restrictions.in(paramFieldMap.get(str),newValSet));
                        }else{
                            criterions.add(getCriterion(paramFieldMap.get(str),fieldMap.get(paramFieldMap.get(str)).getType(),value,isfuzzy));
                        }
                    }
                }
            }

        }
        return criterions;
    }

    /**
     * 获取类及父类的所有属性
     * @param clazz
     * @return
     */
    private static Map<String,Field> getFields(String pre,Class clazz){
        Map<String,Field> fieldMap=new HashMap<>();
        Field[] fields=clazz.getDeclaredFields();
        for(Field field:fields){
            fieldMap.put(pre+field.getName(),field);
        }
        if(clazz.getSuperclass()!=null){
            Map<String,Field> sFieldMap=getFields(pre,clazz.getSuperclass());
            fieldMap.putAll(sFieldMap);
        }
        return fieldMap;
    }


    /**
     * 获取精确或模糊查询条件
     * @param name
     * @param type
     * @param value
     * @param fuzzy
     * @return
     */
    private static Criterion getCriterion(String name,Class type,String value,boolean fuzzy){
        if(fuzzy){
            if(type.equals(String.class)){
                return Restrictions.like(name, "%" + value + "%");
            }
            return Restrictions.like(name, ConvertUtils.convert(value,type));
        } else
            return Restrictions.eq(name,ConvertUtils.convert(value,type));
    }

    /**
     * 获取范围查询条件
     * @param name
     * @param type
     * @param value
     * @param isGe 是否大于等于
     * @return
     */
    private static Criterion getGroupCriterion(String name,Class type,String value,boolean isGe){
        if(isGe)
          return  Restrictions.ge(name,ConvertUtils.convert(value,type));
        else
          return  Restrictions.le(name,ConvertUtils.convert(value,type));
    }

    /**
     * 判断是否是hibernate实体类
     * @param clazz
     * @return
     */
    private static boolean isEntity(Class clazz){
        Annotation [] a=clazz.getAnnotations();
        if(a!=null&&a.length>0){
            for(Annotation annotation:a){
                System.out.println(annotation.annotationType());
                if(annotation.annotationType().equals(javax.persistence.Entity.class)){
                    return true;
                }

            }
        }
        return false;
    }
    /**
     * 将databean中非空属性值复制到tobean,只能复制简单对象和date
     * @param databean
     * @param tobean
     */
    public static void copyNotNull2Bean(Object databean,Object tobean){
        ConvertUtils.register(new DateConverter(),Date.class);
        Map<String,Field> dataBeanFields=getFields("",databean.getClass());
        Map<String,Field> toBeanFields=getFields("",tobean.getClass());
        Set<String> fieldNameSet=toBeanFields.keySet();
        fieldNameSet.retainAll(dataBeanFields.keySet());
        for(String name:fieldNameSet){
            if (PropertyUtils.isReadable(databean, name) &&
                    PropertyUtils.isWriteable(tobean, name)) {
                try {
                    Object value = PropertyUtils.getSimpleProperty(databean, name);
                    if(value instanceof Number){
                        if(((Number)value).byteValue()==0){
                            continue;
                        }
                    }
                    if (value != null ) {
                        copyProperty(tobean, name, value);
                    }
                } catch (IllegalArgumentException ie) {
                    ie.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
      /* PropertyDescriptor[] propertyDescriptors= PropertyUtils.getPropertyDescriptors(databean);

        if(propertyDescriptors!=null&&propertyDescriptors.length>0){
            for(int i=0;i<propertyDescriptors.length;i++){
                PropertyDescriptor propertyDescriptor=propertyDescriptors[i];
                String name=propertyDescriptor.getName();
                if(Objects.equals("class",name))
                    continue;
                if (PropertyUtils.isReadable(databean, name) &&
                        PropertyUtils.isWriteable(tobean, name)) {
                    try {
                        Object value = PropertyUtils.getSimpleProperty(databean, name);
                        if(value!=null&&value.toString().trim().length()>0){
                            copyProperty(tobean, name, value);
                        }
                    }
                    catch (java.lang.IllegalArgumentException ie) {
                        ie.printStackTrace();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }*/
    }

    /**
     * 将entity中属性名称为propertyName的值设为value
     * @param entity
     * @param propertyName
     * @param value
     */
    public static boolean  setObjectProperty(Object entity,String propertyName,Object value){
        try {
            Field field=entity.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(entity,value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 返回entity中属性为propertyName的值
     * @param entity
     * @param propertyName
     * @return
     */
    public static Object getObjectProperty(Object entity,String propertyName){
        Object o=null;
        try {
            Field field=entity.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            o=field.get(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
       public static  void main(String[] args){
          /* List<TableFieldBean> list=new ArrayList<>();
           TableFieldBean t1=new TableFieldBean();
           t1.setFieldContent("123");
           list.add(t1);
           TableFieldBean t2=new TableFieldBean();
           t2.setFieldContent("456");
           list.add(t2);
           TableHeadBean head=new TableHeadBean();
         //  head.setFields(list);
         //  List<TableFieldBean> list2= (List<TableFieldBean>) getObjectProperty(head,"fields");
           try {
               setProperty(head,"fields",list);
               System.out.println(head.getFields());
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (InvocationTargetException e) {
               e.printStackTrace();
           }
          // System.out.println(list2.size());*/
         // List<String> list=  getAlignField(TableFieldBean.class);
          // System.out.println("data_end".substring(0,"data_end".indexOf("_")));

       }

    /**
     * 获取关联的字段
     * @param clazz
     * @return
     */
    public static List<String> getAlignField(Class clazz){
        Set<String> fieldNameList=new HashSet<>();
        Set<Class> anSet=new HashSet<>();
        anSet.add(javax.persistence.OneToMany.class);
        anSet.add(javax.persistence.ManyToOne.class);
        anSet.add(ManyToMany.class);
        anSet.add(OneToOne.class);
        Field[] fields=clazz.getDeclaredFields();
        for(Field f:fields){
            f.setAccessible(true);
            Annotation[] a=f.getAnnotations();
            for(Annotation ab:a){
                if(anSet.contains(ab.annotationType()))
                    fieldNameList.add(f.getName());
            }
        }
        return new ArrayList<>(fieldNameList);
    }

    /**
     * 根据文件路径规则，跟字段赋值文件路径
     * @param o
     * @param fileFieldList
     * @return
     */
    public static Object setFilePath(Object o,List<TableFieldBean> fileFieldList){
        String regStr="\\{([^\\}]+)}";//\{(\w+?)}
        Pattern pattern = Pattern.compile(regStr);
        for(TableFieldBean fieldBean :fileFieldList){
            String defaultValue=fieldBean.getFieldDefault();
            Matcher matcher = pattern.matcher(defaultValue);
            Object val=null;
            while(matcher.find()){
                val=null;
                try{
                    val=getProperty(o,matcher.group(1));
                }catch (Exception e){
                    e.printStackTrace();
                }
               if(val!=null&&StringUtils.isNotBlank(val.toString())){
                   defaultValue=defaultValue.replace("{"+matcher.group(1)+"}",val.toString());
               }
            }
            try {
                setProperty(o,fieldBean.getFieldName(),defaultValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    /**
     * 将bean转换成map
     * 如成员类也想转换，则须集成BaseEntity。支持成员变量为数组或集合
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        Map<String,Object> dataMap=new HashMap<>();
        Map<String,Field> fieldMap=getFields("",obj.getClass());//获取所有字段
        for(Field field:fieldMap.values()){
            field.setAccessible(true);
            String name=field.getName();
            Object value=null;
            try {
                 value=field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(value==null){
                dataMap.put(name,"");
                continue;
            }

            if(BaseEntity.class.isAssignableFrom(field.getType())){
                    dataMap.put(field.getName(),transBean2Map(value));
            }else if(field.getType().isArray()||Collection.class.isAssignableFrom(field.getType())){
                Collection collection=null;
                if(field.getType().isArray()){
                    collection=Arrays.asList(value);
                }else{
                    collection=(Collection)value;
                }
                List<Object> list=new ArrayList<>();
                Iterator it= collection.iterator();
                while (it.hasNext()){
                    list.add(transBean2Map(it.next()));
                }
                dataMap.put(name,list);
            }else{
                if(field.getType().equals(Date.class)){
                    dataMap.put(name,getDateFormatStr(field,obj));
                }else{
                    dataMap.put(name,value);
                }

            }
        }
        return dataMap;

    }

    /**
     * 将map转成bean
     * @param map
     * @param clazz
     * @return
     */
    public static Object transMap2Bean(Map map,Class clazz){
        Object o=null;
        try{
            o=clazz.newInstance();
            if(map==null) return null;
            Field[] fields=clazz.getDeclaredFields();
            for(Field field: fields){
                field.setAccessible(true);
                String key=field.getName();
                if(map.get(key)==null) continue;
                if(BaseEntity.class.isAssignableFrom(field.getType())){
                    try{
                        field.set(o,transMap2Bean((Map) map.get(key), field.getType()));
                    }catch (Exception e){
                        continue;
                    }

                }else if(field.getType().equals(Date.class)){
                    Date d=null;
                    try{
                        String value = (String) map.get(key);
                        if(value==null)continue;
                        if(value.contains("月")){
                            value = value.replace(" ","");
                            d = DateUtil.parse(value,DateUtil.YYMMDD);
                        }else {
                            d=DateUtil.parse(map.get(key)+"",DateUtil.YYYYMMDD);
                        }
                    }catch (Exception e){
                        d=new Date(new Long(map.get(key)+""));
                        if(d==null)
                            continue;
                    }
                    field.set(o,d);
                }else if(field.getType().equals(Timestamp.class)){
                    Date d=null;
                    try{
                        d=DateUtil.parse(map.get(key)+"",DateUtil.YYYYMMDDHHMINSS);
                    }catch (Exception e){
                        d=new Date(new Long(map.get(key)+""));
                    }
                    field.set(o,d);
                }else if(Collection.class.isAssignableFrom(field.getType())){
                        if(Collection.class.isAssignableFrom(map.get(key).getClass())){
                            Type[] types=((ParameterizedType)field.getGenericType()).getActualTypeArguments();
                            if(types==null||types.length==0) continue;
                            Class o2= (Class) types[0];
                            List list=new ArrayList();
                            Collection c= (Collection) map.get(key);
                            Iterator it=c.iterator();
                            while (it.hasNext()){
                                list.add(transMap2Bean((Map) it.next(),o2));
                            }
                            field.set(o,list);
                        }

                }else{
                    try{
                        field.set(o, ConstructorUtils.invokeConstructor(field.getType(),map.get(key)));
                    }catch (Exception e){
                        continue;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return o;
    }

    /**
     * 获取DATE的字符串形式
     * @param field 类型为DATE
     * @param o  对象
     * @return
     */
    private static String getDateFormatStr(Field field,Object o){
        Date date=null;
        try {
             date= (Date) field.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(date==null)
            return "";
        JsonSerialize annotation=  field.getAnnotation(JsonSerialize.class);
       if(annotation==null){
           try {
               Method method=o.getClass().getMethod(StringUtil.toGetMethodNameWithColName(field.getName()));
               if(method!=null)
                    annotation=method.getAnnotation(JsonSerialize.class);
           } catch (NoSuchMethodException e) {
               e.printStackTrace();
           }
       }
       if(annotation!=null){
          if(JodaSerializers.DateTimeSerializer.class.equals(annotation.using())){
              return DateUtil.format(date,DateUtil.YYYYMMDDHHMINSS);
          }
       }
       return DateUtil.format(date,DateUtil.YYYYMMDD);
    }

    /**
     * 获取类中的BaseEntity及子类
     * @param o
     * @return
     */
    public List<BaseEntity> getBaseEntities(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        List<BaseEntity> list=new ArrayList<>();
        for(Field field :fields){
            field.setAccessible(true);
            if(BaseEntity.class.isAssignableFrom(field.getType())){
                try {
                    list.add((BaseEntity)field.get(o));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return  list;
    }

    /**
     * 将targetObj 类型为valueObj对应类型的属性值设为valueObj
     * @param targetObj
     * @param valueObj
     * @param includeNotNull 是否包含非空的属性
     * @return
     */
    public static void setSamePropertyValue(Object targetObj,Object valueObj,boolean includeNotNull){
        Field[] fields=targetObj.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            if(field.getType().equals(valueObj.getClass())){
                try {
                        if(includeNotNull){
                        field.set(targetObj,valueObj);
                        }else{
                            Object val=field.get(targetObj);
                            if(val==null)
                                field.set(targetObj,valueObj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                 }
            }
        }
    }
}

package com.code.util;

import com.code.core.impl.SimpleTemplateUtil;
import com.code.entity.TableHeadBean;
import com.sys.dynamic.CharSequenceJavaFileObject;
import com.sys.dynamic.DynamicEngine;
import com.sys.util.ResourceUtil;
import com.sys.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;

import javax.tools.JavaFileObject;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author zzl
 *         Date:2014-08-02
 */
public class ClassUtil {
    private static Map<String,String> simpleSourceMap;
    private static Map<String,String> one2ManySourceMap;
    static{
        simpleSourceMap= ResourceUtil.getResource("code_templates/SimpleTempResource.properties");
        one2ManySourceMap= ResourceUtil.getResource("code_templates/OneToManyResource.properties");
    }
    public static String getClassName(String type){
        switch (type){
            case "int":
                return "Integer";
            case "float":
                return "Float";
            case "long":
                return "Long";
            case "string":
            case "file":
                return"String";
            case "date":
                return "java.util.Date";
            case "timestamp":
                return "java.sql.Timestamp";
            default:
                return "String";
        }

    }

    /**
     * 生成实体类
     * @param tableHeadBeans
     * @param basePath
     * @param basePackageName
     * @return
     */
    public static List<String> generateEntityClass(List<TableHeadBean> tableHeadBeans,String basePath,String basePackageName){
        Set<String> classNameSet=new HashSet<>();
        DynamicEngine engine=DynamicEngine.getInstance();
        for(TableHeadBean tableHeadBean:tableHeadBeans){
            tableHeadBean.setBasePackageName(basePackageName);
            if(CollectionUtils.isNotEmpty(tableHeadBean.getSubList())){
                classNameSet.addAll(generateOne2ManyEntityClass(tableHeadBean,engine,basePath+File.separator+basePackageName.replace(".",File.separator)));
            }else{
                classNameSet.addAll(generateSimpleEntityClass(tableHeadBean,engine,basePath+File.separator+basePackageName.replace(".",File.separator)));
            }
        }
        return new ArrayList<>(classNameSet);
    }
    private static List<String> generateSimpleEntityClass(TableHeadBean tableHeadBean,DynamicEngine engine,String dirPath){
        List<String> classNameList=new ArrayList<>();
        SimpleTemplateUtil util=new SimpleTemplateUtil();
        File dir=new File(dirPath+File.separator+"entity");
        if(!dir.exists())
            dir.mkdirs();
        for(String key:simpleSourceMap.keySet()){
            if(key.toLowerCase().startsWith("entity")){
                String codeStr=util.generateFileFromTemplate((String)simpleSourceMap.get(key),tableHeadBean);
                String className=tableHeadBean.getBasePackageName()+".entity."+ StringUtil.toEntityName(tableHeadBean.getTableName())+"Entity";
                engine.javaCodetoClassFile(className,codeStr,new File(dir,StringUtil.toEntityName(tableHeadBean.getTableName())+"Entity.class"));
                classNameList.add(className);
            }
        }
        return classNameList;
    }
    private static List<String> generateOne2ManyEntityClass(TableHeadBean tableHeadBean,DynamicEngine engine,String dirPath){
        List<String> classNameList=new ArrayList<>();
        File dir=new File(dirPath+File.separator+"entity");
        if(!dir.exists())
            dir.mkdirs();
        Map<String,File> classNameMap=new HashMap<>();
        List<JavaFileObject> jfo=new ArrayList<>();
        for(String key:one2ManySourceMap.keySet()){
            if(key.toLowerCase().startsWith("entity")){
                SimpleTemplateUtil util=new SimpleTemplateUtil();
                if(key.toLowerCase().startsWith("entity_main")){
                    String codeStr=util.generateFileFromTemplate((String) one2ManySourceMap.get(key), tableHeadBean);
                    String className=tableHeadBean.getBasePackageName()+".entity."+ StringUtil.toEntityName(tableHeadBean.getTableName())+"Entity";
                    classNameMap.put(className,new File(dir,StringUtil.toEntityName(tableHeadBean.getTableName())+"Entity.class"));
                    jfo.add(new CharSequenceJavaFileObject(className,codeStr));
                    classNameList.add(className);
                }else if(key.toLowerCase().startsWith("entity_sub")){
                    List<TableHeadBean> headBeanList=tableHeadBean.getSubList();
                    if(headBeanList!=null&&headBeanList.size()>0){
                        for(TableHeadBean headBean:headBeanList){
                            headBean.setBasePackageName(tableHeadBean.getBasePackageName());
                            String codeStr=util.generateFileFromTemplate((String)one2ManySourceMap.get(key),headBean);
                            String className=headBean.getBasePackageName()+".entity."+ StringUtil.toEntityName(headBean.getTableName())+"Entity";
                            classNameMap.put(className,new File(dir,StringUtil.toEntityName(headBean.getTableName())+"Entity.class"));
                            jfo.add(new CharSequenceJavaFileObject(className,codeStr));
                            classNameList.add(className);
                        }

                    }
                }

            }
        }

            engine.javaCodetoClassFile(null,jfo,classNameMap);

        return classNameList;
    }

    /**
     * 加载类
     * @param className
     * @return
     */
    public static Class loadClass(String className){
        Class clazz=null;
        try{
            URL url=ClassUtil.class.getClassLoader().getResource("").toURI().toURL();
            clazz= URLClassLoader.newInstance(new URL[]{url}, ClassUtil.class.getClassLoader()).loadClass(className);
        }catch (Exception e){

        }
       return clazz;
    }
    public static void loadClass(List<String> classNameList){
        try{
            URL url=ClassUtil.class.getClassLoader().getResource("").toURI().toURL();
            ClassLoader loader=URLClassLoader.newInstance(new URL[]{url}, ClassUtil.class.getClassLoader());
            for(String str:classNameList){
                loader.loadClass(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

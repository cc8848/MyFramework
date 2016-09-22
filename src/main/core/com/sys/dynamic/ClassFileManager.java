package com.sys.dynamic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.tools.*;

/**
 * @author     : jialin
 * @group      : THS_JAVA_PLATFORM
 * @Date       : 2014-10-25 上午10:59:34
 * @Comments   : 类文件管理器
 * @Version    : 1.0.0
 */
public class ClassFileManager extends
        ForwardingJavaFileManager {
    public JavaClassObject getJavaClassObject() {
        return jclassObject;
    }
 
    private JavaClassObject jclassObject;
    private List<JavaClassObject>  javaClassObjects=new ArrayList<>(0);
 
    public ClassFileManager(StandardJavaFileManager
        standardManager) {
        super(standardManager);
    }
 
 
    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
        String className, JavaFileObject.Kind kind, FileObject sibling)
            throws IOException {
            jclassObject = new JavaClassObject(className, kind);
         javaClassObjects.add(jclassObject);
        return jclassObject;
    }



    public List<JavaClassObject> getJavaClassObjects() {
        return javaClassObjects;
    }
}
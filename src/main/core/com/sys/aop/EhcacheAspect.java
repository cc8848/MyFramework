package com.sys.aop;

import com.sys.annotation.Ehcache;
import com.sys.constant.Globals;
import com.sys.util.CacheUtil;
import com.sys.util.JsonUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author zzl
 * Date:2014-10-09
 */
@Aspect
@Service
public class EhcacheAspect {
    private static net.sf.ehcache.Ehcache dictCache;
    static {
        if (dictCache == null) {
            dictCache = CacheUtil.getCache(Globals.Cache.dictCache);
        }
    }

    @Pointcut("@annotation(com.sys.annotation.Ehcache)")
    public void simplePointcut() {
        System.out.println("=====================>pointcut");
    }

    @AfterReturning(pointcut = "simplePointcut()")
    public void simpleAdvice() {
    }
    @Before("simplePointcut()")
    public void beforeMethod(){
        System.out.println("=====================>before");
    }
    @Around( "simplePointcut()")
    public Object aroundLogCalls(ProceedingJoinPoint joinPoint)
            throws Throwable {
        String targetName = joinPoint.getTarget().getClass().toString();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        //试图得到标注的Ehcache类
        @SuppressWarnings("unused")
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Ehcache flag = null;
        for(Method m:methods){
            if(m.getName().equals(methodName)){
                Class[] tmpCs = m.getParameterTypes();
                if(tmpCs.length==arguments.length){
                    flag = m.getAnnotation(Ehcache.class);
                    break;
                }
            }
        }
       /* if(flag==null){
            return null;
        }*/
        Object result;
        String cacheKey = getCacheKey(targetName, methodName, arguments);
        Element element = null;
            element = dictCache.get(cacheKey);
        if(flag!=null&&!flag.addOrdel()){
            dictCache.removeAll();
            element=null;
        }
        if (element == null) {
            if ((arguments != null) && (arguments.length != 0)) {
                result = joinPoint.proceed(arguments);
            } else {
                result = joinPoint.proceed();
            }
            element = new Element(cacheKey, (Serializable) result);
            if(flag!=null&&flag.addOrdel())
              dictCache.put(element);

        }
            return element.getObjectValue();

    }

    /**
     * 获得cache key的方法，cache key是Cache中一个Element的唯一标识 cache key包括
     * 包名+类名+方法名，如com.co.cache.service.UserServiceImpl.getAllUser
     */
    private String getCacheKey(String targetName, String methodName,
                               Object[] arguments) {
        StringBuffer sb = new StringBuffer();
        sb.append(targetName).append(".").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
//                sb.append(".").append(JacksonDIctUtil.object2Json(arguments[i]));
            }
        }
        return sb.toString();
    }
}

package simple.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Song on 2015/11/22.
 */
public class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);


    /**
     * 根据Class对象构建相应的对象
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<T> cls ){
        T t  = null;
        try {
            t = cls.newInstance();
        } catch (InstantiationException e) {
            logger.error("New Instance Failure :" +cls.getName(),e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error("Access Error : "+cls.getName(),e);
            e.printStackTrace();
        }
        return t;
    }


    /**
     * 调用相关的方法
     * @param obj
     * @param method
     * @return
     */
    public  static Object invokeMethod(Object obj ,Method method){
        Object result = null;
        try {
            result = method.invoke(obj);
        } catch (IllegalAccessException e) {
            logger.error("Illegal Access :" +method.getName(),e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            logger.error("Invoke Method Failure : ",method.getName(),e);
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 调用相关的方法
     * @param obj
     * @param method
     * @param params
     * @return
     */
    public  static Object invokeMethod(Object obj ,Method method, Object... params){
        Object result = null;
        try {
            result = method.invoke(obj,params);
        } catch (IllegalAccessException e) {
            logger.error("Illegal Access :" +method.getName(),e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            logger.error("Invoke Method Failure : ",method.getName(),e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置相关域的值
     * @param target
     * @param field
     * @param value
     */
    public static void setField(Object target,Field field,Object value){
        try {
            field.set(target,value);
        } catch (IllegalAccessException e) {
            logger.error("Illegal Access :"+field.getName(),e);
            e.printStackTrace();
        }
    }

}

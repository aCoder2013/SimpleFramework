package simple.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simple.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * Created by Song on 2015/11/22.
 */
public class BeanHelper {

    /**
     * 存放Bean类和Bean实例之间的关系
     */
    private static Map<Class<?>,Object> BEAN_MAP = new HashMap();
    private static Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    static {
        Set<Class<?>> classes  = ClassHelper.getBeanClassSet();
        for(Class cls : classes){
            Object instance  = ReflectionUtil.newInstance(cls);
            BEAN_MAP.put(cls,instance);
        }
    }

    /**
     * 获取BeanMap
     * @return
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     * 根据Bean的Class对象获取对应的实例
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> cls){
        if(!BEAN_MAP.containsKey(cls)){
            logger.error("Can not Find Bean " +cls.getName());
        }
        return (T) BEAN_MAP.get(cls);
    }

}

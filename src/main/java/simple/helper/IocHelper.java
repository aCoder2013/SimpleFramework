package simple.helper;


import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simple.annotation.Inject;
import simple.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * 依赖注入帮助类
 * Created by Song on 2015/11/22.
 */
public class IocHelper {

    private static Logger logger = LoggerFactory.getLogger(IocHelper.class);

    /**
     * 实施依赖注入
     */
    static {
        logger.info("Start To Inject");
        injectService();
        injectController();
        logger.info("Finish To Inject");
    }

    /**
     * 注入Service
     */
    private static void injectService(){
        Set<Class<?>> classes = ClassHelper.getServiceClassSet();
        doInject(classes);
    }

    /**
     * 注入Controller
     */
    private static void injectController(){
        Set<Class<?>> classes = ClassHelper.getControllerClassSet();
        doInject(classes);
    }

    /**
     * 真正实施注入的方法
     * @param classes
     */
    private static void doInject(Set<Class<?>> classes) {
        for(Class<?> cls : classes){
            Object beanInstance  = BeanHelper.getBean(cls);
            Field[] fields = cls.getDeclaredFields();
            if(ArrayUtils.isNotEmpty(fields)){
                for(Field field : fields){
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(Inject.class)){
                        Class fieldType = field.getType();
                        Object fieldValue = BeanHelper.getBean(fieldType);
                        ReflectionUtil.setField(beanInstance, field, fieldValue);
                    }
                }
            }
        }
    }
}

package simple.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simple.helper.BeanHelper;
import simple.helper.ClassHelper;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Song on 2015/11/25.
 */
public class InterceptorHelper {

    private final static Logger logger = LoggerFactory.getLogger(InterceptorHelper.class);

    private static Set<Class<?>> aspestsList = new HashSet<>(); //所有需要代理的类



    static {
        aspestsList = ClassHelper.getClassSetByAnnotation(AOP.class);
        for(Class<?> cls : aspestsList){
            AOP aop = cls.getAnnotation(AOP.class);
            if (aop!=null) {
                Class<Interceptor>[] interceptors = (Class<Interceptor>[]) aop.value();//得到AOP注解上标注的拦截器实现类数组
                Interceptor[] interceptorArray = new Interceptor[interceptors.length];
                int i =0;
                for(Class<Interceptor> interceptorClass : interceptors){
                    try {
                        Interceptor interceptor = interceptorClass.newInstance();
                        interceptorArray[i++] = interceptor;
                    } catch (InstantiationException e) {
                        logger.error("Instantiate Failure",e);
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    EnhancedProxy enhancedProxy = new EnhancedProxy();
                    Object proxyInstance = enhancedProxy.getProxy(cls);
                    Object targetInstance = BeanHelper.getBean(cls);
                    //将拦截器注入到代理类中
                    enhancedProxy.addInterceptors(interceptorArray);
                    //将目标类中的属性设置到代理类中
                    Field[] fields = cls.getDeclaredFields();
                    for(Field field : fields){
                        field.setAccessible(true);
                        field.set(proxyInstance,field.get(targetInstance));
                    }
                    BeanHelper.getBeanMap().put(cls,proxyInstance);//用代理替换目标类
                }   catch (IllegalAccessException e) {
                    logger.error("Access Failure :" + interceptors[0].getName());
                    e.printStackTrace();
                }
            }
        }
    }
}

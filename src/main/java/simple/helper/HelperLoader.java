package simple.helper;


import simple.aop.InterceptorHelper;
import simple.util.ClassUtil;

/**
 * Created by Song on 2015/11/22.
 */
public class HelperLoader {

    public static void init(){
        Class[] classes  = {ConfigHelper.class,
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class,
                InterceptorHelper.class
        };
        for(Class cls : classes){
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}

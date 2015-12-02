package simple.helper;

import simple.annotation.Controller;
import simple.annotation.Service;
import simple.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Song on 2015/11/22.
 */
public class ClassHelper {

    private static Set<Class<?>> CLASS_SET  = new HashSet<>();

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 得到类的集合
     * @return
     */
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /**
     * 得到所有的标有@Controller注解的类
     * @return
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classes = new HashSet<>();
        for(Class<?> cls : CLASS_SET ){
            Controller controller = cls.getAnnotation(Controller.class);
            if(controller!=null){
                classes.add(cls);
            }
        }
        return classes;
    }

    /**
     * 得到所有标有@Service注解的类
     * @return
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classes = new HashSet<>();
        for(Class<?> cls : CLASS_SET ){
            Service service = cls.getAnnotation(Service.class);
            if(service!=null){
                classes.add(cls);
            }
        }
        return classes;
    }

    /**
     * 获取所有的Bean
     * @return
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> classes = new HashSet();
        for(Class<?> cls : CLASS_SET){
            Service service = cls.getAnnotation(Service.class);
            Controller controller = cls.getAnnotation(Controller.class);
            if(service!=null){
                classes.add(cls);
            }else if(controller!=null){
                classes.add(cls);
            }
        }
        return classes;
    }


    /**
     * 获取标有特定注解的类集合
     * @param annotation
     * @return
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotation){
        if(annotation==null){
            return null;
        }
        Set<Class<?>> classes = new HashSet<>();
        for(Class<?> cls : CLASS_SET){
            if(cls.isAnnotationPresent(annotation)){
                classes.add(cls);
            }
        }
        return classes;
    }
}

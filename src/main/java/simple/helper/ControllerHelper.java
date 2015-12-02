package simple.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import simple.annotation.Controller;
import simple.annotation.RequestMethod;
import simple.annotation.RequestMapping;
import simple.bean.Handler;
import simple.bean.RequestWrapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Song on 2015/11/22.
 */
public class ControllerHelper {

    /**
     * 封装RequestWrapper和对应的Handler到Map中
     */
    private static Map<RequestWrapper, Handler> actionMap = new HashMap<>();

    static {
        //获取所有的控制器
        Set<Class<?>> classes = ClassHelper.getControllerClassSet();
        for(Class<?> cls : classes){
            /**
             * 如果Controller注解中存在value值，
             * 则将其作为前缀路径
             */
            String ctrlPath = null;
            if (cls.isAnnotationPresent(Controller.class)) {
                Controller controllerAnnotation = cls.getAnnotation(Controller.class);
                ctrlPath = controllerAnnotation.value();
            }
            //获取所有的方法
            Method[] methods = cls.getDeclaredMethods();
            if (ArrayUtils.isNotEmpty(methods)) {
                for(Method method : methods){
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    if (requestMapping!=null) {
                        String requestPath = requestMapping.value();
                        if(StringUtils.isNotEmpty(ctrlPath)){
                            requestPath = ctrlPath + requestPath;
                        }
                        RequestMethod requestMethod = requestMapping.method();
                        RequestWrapper requestWrapper  = new RequestWrapper(requestMethod,requestPath);
                        Handler controllerWrapper = new Handler(cls, method);
                        actionMap.put(requestWrapper,controllerWrapper);
                    }
                }
            }
        }
    }


    /**
     * 根据HTTP 请求的方法和路径获取对应的请求处理器
     *
     * @param method
     * @param path
     * @return
     */
    public static Optional getHandler(RequestMethod method, String path) {
        RequestWrapper wrapper = new RequestWrapper(method,path);
        return Optional.ofNullable(actionMap.get(wrapper));
    }
}

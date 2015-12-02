package simple.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Song on 2015/11/25.
 */
public class EnhancedProxy implements MethodInterceptor {

    private List<Interceptor> interceptorList = new ArrayList<>();//提供before()和after()方法的实现

    private List<Interceptor> methodInterceptorList = new ArrayList<>();

    /**
     * 增加拦截器
     * @param interceptors
     */
    public void addInterceptors(Interceptor... interceptors) {
        for (Interceptor interceptor : interceptors) {
            if (interceptor != null) {
                interceptorList.add(interceptor);
            }
        }
    }

    /***
     * 得到代理类
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer
                .create(cls, this);
    }

    /**
     * 实施拦截的方法
     * @param enhancedObj
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object enhancedObj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        if (method.isAnnotationPresent(IgnoreAop.class)) {
            result = methodProxy.invokeSuper(enhancedObj, objects);
        } else {
            //执行类级拦截器
            for (Interceptor interceptor : interceptorList) {
                interceptor.before(method, objects);
            }
            //检查是否存在方法级拦截器
            if (methodInterceptorList.isEmpty()) {
                if(method.isAnnotationPresent(AOP.class)){
                    Class<Interceptor>[] classes = (Class<Interceptor>[]) method.getAnnotation(AOP.class).value();
                    for(Class<Interceptor> interceptorClass : classes){
                       Interceptor interceptor = interceptorClass.newInstance();
                        methodInterceptorList.add(interceptor);
                    }
                }
            }
            //执行方法级拦截器
            for (Interceptor interceptor : methodInterceptorList) {
                interceptor.before(method, objects);
            }
            result = methodProxy.invokeSuper(enhancedObj, objects);
            for (Interceptor interceptor : interceptorList) {
                interceptor.after(method, objects);
            }
            for (Interceptor interceptor : methodInterceptorList) {
                interceptor.after(method, objects);
            }
        }
        return result;
    }
}

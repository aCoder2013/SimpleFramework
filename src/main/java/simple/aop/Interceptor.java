package simple.aop;

import java.lang.reflect.Method;

/**
 * 标记：拦截器
 * Created by Song on 2015/11/25.
 */
public interface Interceptor {

    void before(Method method, Object... args);

    void after(Method method, Object... args);
}

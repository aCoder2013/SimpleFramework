package simple.bean;

import java.lang.reflect.Method;

/**
 * 包装控制器
 */
public  class Handler{
    private Class cls ;
    private Method method;

    public Handler(Class cls, Method method) {
        this.cls = cls;
        this.method = method;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
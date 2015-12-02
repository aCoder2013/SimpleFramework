package simple.bean;


import simple.annotation.RequestMethod;

/**
 * 包装HTTP请求
 */
public class RequestWrapper{
    private RequestMethod method; //HTTP 请求方法
    private String path ; //路径

    public RequestWrapper(RequestMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestWrapper that = (RequestWrapper) o;

        if (method != that.method) return false;
        return !(path != null ? !path.equals(that.path) : that.path != null);

    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}
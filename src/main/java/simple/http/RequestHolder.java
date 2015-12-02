package simple.http;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Song on 2015/11/29.
 */
public class RequestHolder {
    private static ThreadLocal<HttpServletRequest> REQUEST = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request){
        REQUEST.set(request);
    }

    public static HttpServletRequest getRequest(){
        return REQUEST.get();
    }

    public static void removeRequest(){
        REQUEST.remove();
    }
}

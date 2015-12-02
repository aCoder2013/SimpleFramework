package simple.http;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Song on 2015/11/29.
 */
public class ResponseHolder {

    private static ThreadLocal<HttpServletResponse> RESPONSE  = new ThreadLocal<>();

    public static void setResponse(HttpServletResponse response){
        RESPONSE.set(response);
    }

    public static HttpServletResponse getResponse(){
        return RESPONSE.get();
    }

    public static void removeResponse(){
        RESPONSE.remove();
    }
}

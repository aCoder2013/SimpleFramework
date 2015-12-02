package simple.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simple.helper.HelperLoader;
import simple.http.RequestHolder;
import simple.http.ResponseHolder;
import simple.multipart.MultipartHelper;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Song on 2015/11/23.
 */
@WebFilter(urlPatterns = "/*")
public class PrepareAndExecuteFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(PrepareAndExecuteFilter.class);

    private static final String DEFAULT_CHARSET="UTF-8";//默认编码
    /**
     * 核心分发类
     */
    private Disapther disapther;

    private ServletContext servletContext ;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Init PrepareAndExecuteFilter");
        //初始化助手类
        HelperLoader.init();
        disapther = new Disapther();
        servletContext = filterConfig.getServletContext();
    }



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //设置编码
        req.setCharacterEncoding(DEFAULT_CHARSET);
        res.setCharacterEncoding(DEFAULT_CHARSET);
        RequestHolder.setRequest(req);
        ResponseHolder.setResponse(res);
        if(req.getRequestURI().equals("/favicon.ico")){
            return;
        }
        //若表单类型为multipart/form-data，则初始化文件上传组件
        if(MultipartHelper.isMultipart(req)){
            MultipartHelper.init(servletContext);
        }
        try {
            disapther.disapther();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }finally {
            RequestHolder.removeRequest();
            ResponseHolder.removeResponse();
        }
    }

    @Override
    public void destroy() {

    }
}

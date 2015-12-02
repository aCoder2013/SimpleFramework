package simple.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simple.annotation.RequestMethod;
import simple.annotation.RequestParam;
import simple.bean.Handler;
import simple.helper.BeanHelper;
import simple.helper.ConfigHelper;
import simple.helper.ControllerHelper;
import simple.multipart.MultipartHelper;
import simple.util.ReflectionUtil;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static simple.http.RequestHolder.getRequest;
import static simple.http.ResponseHolder.getResponse;

/**
 * Created by Song on 2015/11/22.
 */
public class Disapther {

    private Logger logger = LoggerFactory.getLogger(Disapther.class);

    private String path = null;

    private String method ;

    /**
     * 核心分发请求的方法
     * @throws ServletException
     * @throws IOException
     */
    public void disapther() throws ServletException, IOException, InvocationTargetException, IllegalAccessException {
        setUpMethodAndPath();
        doDispatch();
    }

    /**
     * 真正实施分发请求的方法
     */
    private void doDispatch(){
        if(path.startsWith("/static")) {
            try {
                int prefixLength = "/static/".length();
                String assetPath = ConfigHelper.getAssetPath() +
                        path.substring(prefixLength);
                getRequest().getRequestDispatcher(assetPath).forward(getRequest(), getResponse());
            } catch (ServletException e) {
                logger.error("Servlet Exception", e);
                e.printStackTrace();
            } catch (IOException e) {
                logger.error("IO Exception", e);
                e.printStackTrace();
            }
        }
        //得到请求处理器
        Optional<Handler> optional = ControllerHelper.getHandler(RequestMethod.valueOf(method), path);
        optional.ifPresent(handler -> handleDynamicRequest(handler));

    }

    /**
     * 处理动态请求
     * @param handler
     */
    private void handleDynamicRequest(Handler handler) {
        Class controllerClass = handler.getCls();
        Object controller = BeanHelper.getBean(controllerClass);
        Method targetMethod = handler.getMethod();
        Map<String, Object> paramsMap = getRequestParams();
        Parameter[] parameters = targetMethod.getParameters();
        Object[] paramInstances = getParamInstance(paramsMap, parameters);
        Object result = null;
        if (!paramsMap.isEmpty()) {
            result = ReflectionUtil.invokeMethod(controller, targetMethod, paramInstances);
        }else{
            result = ReflectionUtil.invokeMethod(controller,targetMethod);
        }
        //如果为View类型，则跳转到相关的JSP文件
        try {
            if(result instanceof ModelAndView){
                handleView((ModelAndView) result);
            }else{
                handleJsonData(result);
            }
        } catch (IOException e) {
            logger.error("IO Error",e);
            throw new RuntimeException(e);
        } catch (ServletException e) {
            logger.error("Servlet Exception ",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 构造目标方法参数列表
     * @param paramsMap
     * @param parameters
     * @return
     */
    private Object[] getParamInstance(Map<String, Object> paramsMap, Parameter[] parameters) {
        List paramInstances = new LinkedList<>();
        for(Parameter parameter : parameters){
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            if(requestParam!=null){
                String paramName = requestParam.value();
                if(paramsMap.containsKey(paramName)){
                    Object o = paramsMap.get(paramName);
                    paramInstances.add(o);
                }
            }
        }
        return paramInstances.toArray();
    }

    /**
     * 处理JSON数据
     * @param result
     * @throws IOException
     */
    private void handleJsonData(Object result) throws IOException {
        //否则直接返回Json数据
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(result);
        getResponse().setContentType("application/json");
        getResponse().setCharacterEncoding("utf-8");
        PrintWriter writer = getResponse().getWriter();
        writer.write(jsonString);
        writer.flush();
        writer.close();
    }

    /**
     * 处理视图
     * @param result
     * @throws IOException
     * @throws ServletException
     */
    private void handleView(ModelAndView result) throws IOException, ServletException {
        ModelAndView modelAndView = result;
        String fullPath = modelAndView.getPath();
        //如果以redirect:开头，则直接跳转
        if(fullPath.startsWith("redirect:")){
            String realPath = fullPath.substring(fullPath.indexOf("redirect:"));
            getResponse().sendRedirect(getRequest().getContextPath() + realPath);
        }else {
            //读取并将model中的属性设置到Request中
            Map<String,Object> models = modelAndView.getModels();
            if(models!=null){
                Set<Map.Entry<String, Object>> modelSet =models.entrySet();
                for(Map.Entry<String,Object> model : modelSet){
                    getRequest().setAttribute(model.getKey(), model.getValue());
                }
            }
            getRequest().getRequestDispatcher(ConfigHelper.getJspPath() + modelAndView.getPath())
                    .forward(getRequest(), getResponse());
        }
    }

    /**
     * 获取请求参数
     * 例如/hello?name=Mars中的name:Mars名值对
     */
    private Map<String, Object> getRequestParams() {
        Map<String,Object> paramsMap = new HashMap<>();
        if (MultipartHelper.isMultipart(getRequest())) {
            paramsMap= MultipartHelper.createMultipartForm(getRequest());
        }else {
            Enumeration<?> parameterNames = getRequest().getParameterNames();
            while (parameterNames.hasMoreElements()){
                String name = (String) parameterNames.nextElement();
                paramsMap.put(name, getRequest().getParameter(name));
            }
        }
        return paramsMap;
    }


    /**
     * 设置方法和路径
     */
    private void setUpMethodAndPath() {
        method = getRequest().getMethod().toUpperCase();
        /**
         * 如果ContextPath为类似/Project开头的形式，则将其截取掉
         */
        String contextPath = getRequest().getContextPath();
        if (contextPath!="/") {
            path = getRequest().getRequestURI().substring(contextPath.length());
        }
        if(path.endsWith("/")){
            path = path.substring(0,path.length()-1);
        }
    }
}

package simple.core;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Song on 2015/11/23.
 */
public class ModelAndView {

    private String path ;

    private Map<String,Object> models = new HashMap<>();



    public ModelAndView(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getModels() {
        return models;
    }


    public void addAttribute(String name ,Object value){
        if (StringUtils.isNotEmpty(name)) {
            if(value!=null){
                models.put(name,value);
            }
        }
    }
    
    public void clear(){
        models.clear();
    }

}

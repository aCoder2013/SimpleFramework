package simple.helper;


import simple.constant.ConfigConstant;
import simple.util.PropsUtils;

import java.util.Properties;

/**
 * 读取配置文件的帮助类
 * Created by Song on 2015/11/21.
 */
public class ConfigHelper {

    private static final Properties PROPS = PropsUtils.loadProps(ConfigConstant.CONFIG_FILE_NAME);


    /**
     * 获取JDBC Driver
     * @return
     */
    public static String getJdbcDriver(){
        return PropsUtils.getString(PROPS,ConfigConstant.JDBC_DRIVER);
    }

    /**
     * 获取JDBC URL
     * @return
     */
    public static String getJdbcUrl(){
        return PropsUtils.getString(PROPS,ConfigConstant.JDBC_URL);
    }

    /**
     * 获取JDBC USERNAME
     * @return
     */
    public static String getJdbcUserName(){
        return PropsUtils.getString(PROPS,ConfigConstant.JDBC_USERNAME);
    }


    /**
     * 获取JDBC PASSWORD
     * @return
     */
    public static String getJdbcPassword(){
        return PropsUtils.getString(PROPS,ConfigConstant.JDBC_PASSWORD);
    }

    /**
     * 获取基础包名
     * @return
     */
    public static String getAppBasePackage(){
        return PropsUtils.getString(PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }

    /**
     * 获取JSP文件路径
     * @return
     */
    public static String getJspPath(){
        return PropsUtils.getString(PROPS,ConfigConstant.APP_JSP_PATH);
    }


    /**
     * 获取静态资源文件路径
     * @return
     */
    public static String getAssetPath(){
        return PropsUtils.getString(PROPS,ConfigConstant.APP_ASSET_PATH);
    }

    /**
     * 获取最大上传文件尺寸
     * 默认10M
     * @return
     */
    public static Long getFileMaxSize(){
        return PropsUtils.getLong(PROPS, ConfigConstant.MULTIPART_MAX_FILE_SIZE);
    }

    /**
     * 文件上传路径
     * @return
     */
    public static String getFileLocation(){
        return PropsUtils.getString(PROPS,ConfigConstant.MULTIPART_FILE_LOCATION,"/temp");
    }
}






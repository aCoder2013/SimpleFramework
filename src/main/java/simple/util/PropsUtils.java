package simple.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性工具类
 * Created by Song on 2015/11/21.
 */
public class PropsUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropsUtils.class);
    /**
     * 默认上传文件大小
     */
    private static long DEFAULT_FILE_SIZE = 10L * 1024 * 1024;


    /**
     * 根据提供的文件名称去加载相关的属性文件
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName){
        Properties properties = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(is == null){
                throw new FileNotFoundException(fileName+ "Not Found !");
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("Load properties file failure",e);
        } finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("Close InputStream failure",e);
                }
            }
        }
        return properties;
    }


    /**
     * 获取字符串类型的属性
     * 默认值为空
     * @param properties
     * @param key
     * @return
     */
    public static String getString(Properties properties,String key){
        return getString(properties,key,"");
    }

    /**
     * 获取字符串类型的属性，并提供默认值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties properties,String key ,String defaultValue){
        return properties.getProperty(key,defaultValue);
    }

    /**
     * 获取数值类型的属性，并提供默认值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties properties,String key,int defaultValue){
        int value  = defaultValue;
        if(properties.contains(key)){
            value = Integer.parseInt(properties.getProperty(key));
        }
        return value;
    }

    /**
     * 获取数值类型的属性
     * 默认值为0
     * @param properties
     * @param key
     * @return
     */
    public static int getInt(Properties properties , String key){
        return getInt(properties,key,0);
    }
    /**
     * 获取Long类型的属性
     * @param properties
     * @param key
     * @return
     */
    public static long getLong(Properties properties,String key,long defaultValue){
        long value = defaultValue;
        String v = properties.getProperty(key);
        value = (v==null?value: Long.parseLong(v));
        return value;
    }

    /**
     * 获取Long类型的属性
     * 默认值为10MB
     * @param properties
     * @param key
     * @return
     */
    public static long getLong(Properties properties,String key){
        return getLong(properties,key,DEFAULT_FILE_SIZE);
    }

    /**
     * 提供布尔类型的属性
     * 默认值为false
     * @param properties
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties properties ,String key){
        return getBoolean(properties, key,false);
    }

    /**
     * 获取布尔类型的属性，并提供默认值
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Properties properties,String key ,boolean defaultValue){
        boolean value = defaultValue;
        if(properties.contains(key)){
            Boolean.parseBoolean(properties.getProperty(key));
        }
        return value;
    }

}

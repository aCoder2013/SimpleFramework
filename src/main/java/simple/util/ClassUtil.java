package simple.util;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 加载类的工具类
 * Created by Song on 2015/11/22.
 */
public class ClassUtil {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);


    /**
     * 获取当前线程的类加载器
     * @return
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 根据类名称加载类
     * 根据isInitiallized的值决定是否初始化
     * @param className
     * @param isInitiallized
     * @return
     */
    public static Class<?> loadClass(String className , boolean isInitiallized){
        Class cls = null ;
        try {
            cls = Class.forName(className, isInitiallized, getClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error("Load Class Failure :" +className,e);
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * 获取指定包名下的所有类
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName){
        HashSet<Class<?>> classHashSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls =  getClassLoader().getResources(packageName);
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if(protocol.equals("file")){
                    handleRegularFile(classHashSet, packageName, url.getPath());
                }else if(protocol.equals("jar")){
                    handleJarFile(classHashSet, url);
                }
            }
        } catch (IOException e) {
            logger.error("Get Resources Failure :" +packageName,e);
            e.printStackTrace();
        }
        return classHashSet;
    }

    /**
     * 处理JAR文件
     * 将所有的.class文件添加到集合中
     * @param classHashSet
     * @param url
     * @throws IOException
     */
    private static void handleJarFile(HashSet<Class<?>> classHashSet, URL url) throws IOException {
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        while(jarURLConnection!=null){
            JarFile jarFile = jarURLConnection.getJarFile();
            while (jarFile!=null){
                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                while(jarEntryEnumeration.hasMoreElements()){
                    JarEntry entry = jarEntryEnumeration.nextElement();
                    String jarEntityName = entry.getName();
                    if(jarEntityName.endsWith(".class")){
                        String className = jarEntityName.substring(0,jarEntityName.lastIndexOf(".class")).replace("/",".");
                        classHashSet.add(loadClass(className, false));
                    }
                }
            }
        }
    }

    /**
     * 遍历packageName，将所有的.class文件添加到集合中
     * @param classHashSet
     * @param packageName
     * @param path
     */
    private static void handleRegularFile(HashSet<Class<?>> classHashSet ,String packageName ,String path){
        final File[] files = new File(path).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory() || ( pathname.isFile() && pathname.getName().endsWith(".class"));
            }
        });
        if (ArrayUtils.isNotEmpty(files)) {
            for(File temp :files){
                if(temp.isDirectory()){
                    handleRegularFile(classHashSet, packageName + "/" + temp.getName(), path + "/" + temp.getName());
                }else if(temp.isFile()){
                    String className = null ;
                    className = temp.getName().substring(0, temp.getName().indexOf(".class"));
                    classHashSet.add(loadClass((packageName + "/" + className).replaceAll("/", "."),false));
                }
            }
        }
    }
}

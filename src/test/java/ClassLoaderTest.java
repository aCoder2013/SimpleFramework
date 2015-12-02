import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Song on 2015/11/21.
 */
public class ClassLoaderTest {

    @Test
    public void test(){
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("org/song");
            while (urls.hasMoreElements()){
                URL url =  urls.nextElement();
                String protocol = url.getProtocol();
                System.out.println(protocol);
                System.out.println(url.getPath().replaceAll("^/"," "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() throws IOException {
        HashSet<Class<?>> classHashSet = new HashSet<Class<?>>();
        Enumeration<URL> urls =  Thread.currentThread().getContextClassLoader().getResources("org/song");
        while (urls.hasMoreElements()){
            URL url = urls.nextElement();
            System.out.println(url.getPath());
            System.out.println(url.getPath().replaceAll("%20"," "));
            System.out.println(url.getProtocol());
            findClass(classHashSet, "org/song",url.getPath());
            for(Class cls : classHashSet){
                System.out.println(cls.getName());
            }
        }
    }

    public void test2() throws IOException, ClassNotFoundException {
        HashSet<Class<?>> classHashSet = new HashSet<Class<?>>();
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("");
        while (urls.hasMoreElements()){
            URL url = urls.nextElement();
            handleJarFile(classHashSet,url);
        }
        for(Class cls : classHashSet){
            System.out.println(cls.getName());
        }
    }


    private  void findClass(HashSet<Class<?>> classHashSet ,String packageName ,String path){
        final File[] files = new File(path).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory() || ( pathname.isFile() && pathname.getName().endsWith(".class"));
            }
        });
        if (ArrayUtils.isNotEmpty(files)) {
            for(File temp :files){
                if(temp.isDirectory()){
                    findClass(classHashSet, packageName+"/"+temp.getName(), path + "/" + temp.getName());
                }else if(temp.isFile()){
                    String className = "";
                    try {
                        className = temp.getName().substring(0, temp.getName().indexOf(".class"));
                        classHashSet.add(Thread.currentThread().getContextClassLoader().loadClass((packageName +"/"+className).replaceAll("/",".")));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private static void handleJarFile(HashSet<Class<?>> classHashSet, URL url) throws IOException, ClassNotFoundException {
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
                        System.out.println("className : " +className);
                        classHashSet.add(Thread.currentThread().getContextClassLoader().loadClass(className));
                    }
                }
            }
        }
    }
}

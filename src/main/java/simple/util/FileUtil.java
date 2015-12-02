package simple.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Song on 2015/11/28.
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取真实的文件名称，去掉路径
     * @param fileName
     * @return
     */
    public static String getRealName(String fileName){
        return FilenameUtils.getName(fileName);
    }

    /**
     * 创建父路径
     * @param filePath
     * @return
     */
    public static File createParentPath(String filePath){
        File file  = new File(filePath);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            try {
                FileUtils.forceMkdir(parentFile);
            } catch (IOException e) {
                logger.error("Create File Failure ",e);
                throw new RuntimeException(e);
            }
        }
        return file;
    }
}

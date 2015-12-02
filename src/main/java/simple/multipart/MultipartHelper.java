package simple.multipart;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simple.helper.ConfigHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Song on 2015/11/27.
 */
public class MultipartHelper {


    private static Logger logger = LoggerFactory.getLogger(MultipartHelper.class);

    /**
     * File Upload Handler
     */
    private static ServletFileUpload fileUpload ;
    /**
     * 默认表单属性编码
     */
    private static final String DEFAULT_FIELD_ENCODING="utf-8";

    /**
     * 封装表单属性
     */
    private static Map<String,Object> fileldMap  = new HashMap<>();


    /**
     * Init Method
     * @param servletContext
     */
    public static void init(ServletContext servletContext){
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // Configure a repository (to ensure a secure temp location is used)
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        fileUpload = new ServletFileUpload(factory);
        long fileMaxSize = ConfigHelper.getFileMaxSize();
        fileUpload.setFileSizeMax(fileMaxSize);
        fileUpload.setHeaderEncoding(DEFAULT_FIELD_ENCODING);
    }

    /**
     * Check that we have a file upload request
     * @param request
     * @return
     */
    public static boolean isMultipart(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 读取表单中的参数
     * eg:普通文本或文件
     * @param request
     * @return
     */
    public static Map<String,Object> createMultipartForm(HttpServletRequest request){
        //Parse the request
        try {
            List<FileItem> items = fileUpload.parseRequest(request);
            // Process the uploaded items
            Iterator<FileItem> iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = iter.next();
                if (item.isFormField()) {
                    processFormField(item);
                } else {
                    processUploadedFile(item);
                }
            }
        } catch (FileUploadException e) {
            logger.error("File Upload Failure :" +e );
            e.printStackTrace();
        }
        return fileldMap;
    }

    /**
     * Process a regular form field
     * @param item
     */
    private static void processFormField(FileItem item) {
        String name = item.getFieldName();
        String value = null;
        try {
            value = item.getString(DEFAULT_FIELD_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotEmpty(name)) {
            fileldMap.put(name,value);
        }
    }

    /**
     * Process a file upload
     * @param item
     */
    private static void processUploadedFile(FileItem item) {
        try {
            MultipartFile file = new MultipartFileImpl().setName(item.getFieldName())
                    .setOriginalFilename(item.getName())
                    .setSize(item.getSize())
                    .setContentType(item.getContentType())
                    .setBytes(item.get())
                    .setInputStream(item.getInputStream());
            fileldMap.put(item.getFieldName(),file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

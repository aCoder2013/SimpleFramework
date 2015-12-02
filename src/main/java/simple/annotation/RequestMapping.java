package simple.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Song on 2015/11/22.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {


    /**
     * 路径名称
     * @return
     */
    String value() default "";

    /**
     * HTTP请求方法
     * 默认为GET方法
     * @return
     */
    RequestMethod method() default RequestMethod.GET;
}

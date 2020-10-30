package com.mepeng.cn.SevenPen.order.excel;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumnDefine {
    int value();

    String message() default "";

    String format() default "";

    ExcelDataType dataType() default ExcelDataType.NotDefine;

    int dataCode() default -1;
}

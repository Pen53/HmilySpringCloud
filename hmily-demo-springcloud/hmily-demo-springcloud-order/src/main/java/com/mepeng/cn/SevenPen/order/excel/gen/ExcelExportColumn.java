package com.mepeng.cn.SevenPen.order.excel.gen;

import com.mepeng.cn.SevenPen.order.excel.ExcelDataType;
import lombok.Data;

@Data
public class ExcelExportColumn {
    private String fieldName;
    private String title;
    private String format;
    private ExcelDataType dataType;

    public ExcelExportColumn(String fieldName, String title) {
        this.fieldName = fieldName;
        this.title = title;
    }

    public ExcelExportColumn(String fieldName, String title, String format) {
        this.fieldName = fieldName;
        this.title = title;
        this.format = format;
    }

    public ExcelExportColumn(String fieldName, String title, ExcelDataType dataType) {
        this.fieldName = fieldName;
        this.title = title;
        this.dataType = dataType;
    }
}

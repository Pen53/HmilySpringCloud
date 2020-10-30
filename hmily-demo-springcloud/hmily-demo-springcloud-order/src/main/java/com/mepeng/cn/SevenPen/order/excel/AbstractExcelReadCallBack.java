package com.mepeng.cn.SevenPen.order.excel;

import lombok.Data;

@Data
public class AbstractExcelReadCallBack<T extends DataImportDto> {
    private Class<T> dtoClass;
    private ExcelReadCallBack<T> excelReadCallBack;
    public AbstractExcelReadCallBack(Class<T> dtoClass) {
        this.dtoClass = dtoClass;
    }

    public AbstractExcelReadCallBack(Class<T> dtoClass, ExcelReadCallBack<T> callBack) {
        this.dtoClass = dtoClass;
        this.excelReadCallBack = callBack;
    }
}

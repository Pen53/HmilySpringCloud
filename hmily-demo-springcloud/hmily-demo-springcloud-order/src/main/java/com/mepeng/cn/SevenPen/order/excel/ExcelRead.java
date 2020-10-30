package com.mepeng.cn.SevenPen.order.excel;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExcelRead<T extends DataImportDto> {
    Map<String, ImportResultDto<T>> analyzeExcel(MultipartFile excelFile, List<AbstractExcelReadCallBack<T>> callBackList) throws IOException;

    ImportResultDto<T> analyzeExcelFirstSheet(MultipartFile excelFile, AbstractExcelReadCallBack<T> callBackList) throws IOException;
}

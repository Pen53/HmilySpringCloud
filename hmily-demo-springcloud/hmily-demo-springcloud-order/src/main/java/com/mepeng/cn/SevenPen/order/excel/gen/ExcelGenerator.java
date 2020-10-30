package com.mepeng.cn.SevenPen.order.excel.gen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ExcelGenerator {
    void generateExcel(Map<String, List<Map>> var1, List<ExcelExportColumn> var2, String var3, HttpServletRequest var4, HttpServletResponse var5);

    void generateExcelSheet(Map<String, List<Map>> var1, Map<String, List<ExcelExportColumn>> var2, String var3, HttpServletRequest var4, HttpServletResponse var5);

    void generateExcelIntoPath(Map<String, List<Map>> var1, Map<String, List<ExcelExportColumn>> var2, String var3, HttpServletRequest var4, HttpServletResponse var5, String var6);
}

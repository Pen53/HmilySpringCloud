package com.mepeng.cn.SevenPen.order.excel.gen;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mepeng.cn.SevenPen.order.excel.ExcelDataType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExcelGeneratorDefaultImpl implements ExcelGenerator {
//    @Resource(
//            name = "DictCache"
//    )
//    DictCacheServiceImpl<List<DictDto>> dictCacheSerivce;
//    @Resource(
//            name = "RegionCache"
//    )
//    RegionCacheSerivceImpl<RegionDto> regionCacheService;
    private static final Logger logger = LoggerFactory.getLogger(ExcelGeneratorDefaultImpl.class);

    public ExcelGeneratorDefaultImpl() {
    }

    public void generateExcel(Map<String, List<Map>> excelData, List<ExcelExportColumn> columnDefineList, String fileName, HttpServletRequest request, HttpServletResponse response) {
        if (excelData==null||excelData.isEmpty()) {
            throw new RuntimeException("No excel data !");
        } else {
            Workbook workbook = null;
            OutputStream outputStream = null;

            try {
                outputStream = this.initOutputStream(request, response, fileName);
                workbook = this.createWorkbook();
                Set<String> sheetSet = excelData.keySet();
                Iterator var9 = sheetSet.iterator();

                while(var9.hasNext()) {
                    String sheetName = (String)var9.next();
                    List<Map> rowList = (List)excelData.get(sheetName);
                    Sheet sheet = workbook.createSheet(sheetName);
                    this.generateTitleRow(sheet, columnDefineList);
                    this.generateDataRows(sheet, rowList, columnDefineList);
                    this.setSheetFinishStyle(sheet, columnDefineList.size());
                }

                workbook.write(outputStream);
            } catch (Exception var16) {
                logger.warn(var16.getMessage(), var16);
                throw new RuntimeException(var16.getMessage(), var16);
            } finally {
                closeStream(workbook);
                closeStream(outputStream);
            }
        }
    }
    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                logger.warn("close stream " + stream.getClass() + " error", e);
            }
        }
    }
    public void generateExcelSheet(Map<String, List<Map>> excelData, Map<String, List<ExcelExportColumn>> maps, String fileName, HttpServletRequest request, HttpServletResponse response) {
        if (excelData==null||excelData.isEmpty()) {
            throw new RuntimeException("No excel data !");
        } else {
            Workbook workbook = null;
            OutputStream outputStream = null;

            try {
                outputStream = this.initOutputStream(request, response, fileName);
                workbook = this.createWorkbook();
                Set<String> sheetSet = excelData.keySet();
                Iterator var9 = sheetSet.iterator();

                while(var9.hasNext()) {
                    String sheetName = (String)var9.next();
                    List<Map> rowList = (List)excelData.get(sheetName);
                    Sheet sheet = workbook.createSheet(sheetName);
                    List<ExcelExportColumn> columnDefineList = (List)maps.get(sheetName);
                    this.generateTitleRow(sheet, columnDefineList);
                    this.generateDataRows(sheet, rowList, columnDefineList);
                    this.setSheetFinishStyle(sheet, columnDefineList.size());
                }

                workbook.write(outputStream);
            } catch (Exception var17) {
                logger.warn(var17.getMessage(), var17);
                throw new RuntimeException(var17.getMessage(), var17);
            } finally {
                closeStream(workbook);
                closeStream(outputStream);
            }
        }
    }

    public void generateExcelIntoPath(Map<String, List<Map>> excelData, Map<String, List<ExcelExportColumn>> maps, String fileName, HttpServletRequest request, HttpServletResponse response, String filePath) {
        if (excelData==null||excelData.isEmpty()) {
            throw new RuntimeException("No excel data !");
        } else {
            Workbook workbook = null;
            OutputStream outputStream = null;
            FileOutputStream os = null;

            try {
                outputStream = this.initOutputStream(request, response, fileName);
                workbook = this.createWorkbook();
                Set<String> sheetSet = excelData.keySet();
                Iterator var11 = sheetSet.iterator();

                while(var11.hasNext()) {
                    String sheetName = (String)var11.next();
                    List<Map> rowList = (List)excelData.get(sheetName);
                    Sheet sheet = workbook.createSheet(sheetName);
                    List<ExcelExportColumn> columnDefineList = (List)maps.get(sheetName);
                    this.generateTitleRow(sheet, columnDefineList);
                    this.generateDataRows(sheet, rowList, columnDefineList);
                    this.setSheetFinishStyle(sheet, columnDefineList.size());
                }

                os = new FileOutputStream(filePath + File.separator + fileName);
                workbook.write(os);
            } catch (Exception var19) {
                logger.warn(var19.getMessage(), var19);
                throw new RuntimeException(var19.getMessage(), var19);
            } finally {
                closeStream(workbook);
                closeStream(outputStream);
                closeStream(os);
            }
        }
    }

    protected void generateDataRows(Sheet sheet, List<Map> rowList, List<ExcelExportColumn> columnDefineList) {
        if (!(rowList==null||rowList.isEmpty())) {
            Map<Integer, CellStyle> columnCellStyle = new HashMap();

            for(int i = 0; i < rowList.size(); ++i) {
                Map cellList = (Map)rowList.get(i);
                Row row = sheet.createRow(i + 1);

                for(int j = 0; j < columnDefineList.size(); ++j) {
                    this.createCell(cellList.get(((ExcelExportColumn)columnDefineList.get(j)).getFieldName()), row, j, columnCellStyle, (ExcelExportColumn)columnDefineList.get(j));
                }
            }

        }
    }

    private void createCell(Object cellValue, Row row, int cellIndex, Map<Integer, CellStyle> columnCellStyle, ExcelExportColumn excelExportColumn) {
        Cell cell = row.createCell(cellIndex);
        if (cellValue == null) {
            if (columnCellStyle.get(-1) == null) {
                columnCellStyle.put(-1, this.getSheetStringStyle(row.getSheet().getWorkbook()));
            }

            cell.setCellStyle((CellStyle)columnCellStyle.get(-1));
            cell.setCellType(1);
            cell.setCellValue((String)cellValue);
        } else if (cellValue instanceof String) {
            if (excelExportColumn.getDataType() != null) {
                if (columnCellStyle.get(cellIndex) == null) {
                    columnCellStyle.put(cellIndex, this.getSheetStringStyle(row.getSheet().getWorkbook()));
                }

                cell.setCellStyle((CellStyle)columnCellStyle.get(cellIndex));
                cell.setCellType(1);
                cell.setCellValue(this.getNamesByCodes((String)cellValue, excelExportColumn.getDataType()));
            } else {
                if (columnCellStyle.get(cellIndex) == null) {
                    columnCellStyle.put(cellIndex, this.getSheetStringStyle(row.getSheet().getWorkbook()));
                }

                cell.setCellStyle((CellStyle)columnCellStyle.get(cellIndex));
                cell.setCellType(1);
                cell.setCellValue((String)cellValue);
            }

        } else if (!(cellValue instanceof Double) && !(cellValue instanceof BigDecimal)) {
            if (!(cellValue instanceof Integer) && !(cellValue instanceof Long)) {
                if (cellValue instanceof Date) {
                    if (columnCellStyle.get(cellIndex) == null) {
                        columnCellStyle.put(cellIndex, this.getSheetDateStyle(row.getSheet().getWorkbook(), excelExportColumn.getFormat()));
                    }

                    cell.setCellStyle((CellStyle)columnCellStyle.get(cellIndex));
                    cell.setCellType(0);
                    cell.setCellValue((Date)cellValue);
                }
            } else {
                if (excelExportColumn.getDataType() != null) {
                    if (columnCellStyle.get(cellIndex) == null) {
                        columnCellStyle.put(cellIndex, this.getSheetCodeDescStyle(row.getSheet().getWorkbook()));
                    }

                    cell.setCellStyle((CellStyle)columnCellStyle.get(cellIndex));
                    cell.setCellType(1);
                    cell.setCellValue(this.getNameByCode((Number)cellValue, excelExportColumn.getDataType()));
                } else {
                    if (columnCellStyle.get(cellIndex) == null) {
                        columnCellStyle.put(cellIndex, this.getSheetIntegerStyle(row.getSheet().getWorkbook(), excelExportColumn.getFormat()));
                    }

                    cell.setCellStyle((CellStyle)columnCellStyle.get(cellIndex));
                    cell.setCellType(0);
                    if (cellValue instanceof Integer) {
                        cell.setCellValue((double)(Integer)cellValue);
                    } else {
                        cell.setCellValue((double)(Long)cellValue);
                    }
                }

            }
        } else {
            if (columnCellStyle.get(cellIndex) == null) {
                columnCellStyle.put(cellIndex, this.getSheetDoubleStyle(row.getSheet().getWorkbook(), excelExportColumn.getFormat()));
            }

            cell.setCellStyle((CellStyle)columnCellStyle.get(cellIndex));
            cell.setCellType(0);
            if (cellValue instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal)cellValue).doubleValue());
            } else {
                cell.setCellValue((Double)cellValue);
            }

        }
    }

    private String getNamesByCodes(String values, ExcelDataType excelDataType) {
        String[] valuesArray = values.split(",");
        StringBuilder sb = new StringBuilder();
        String[] var5 = valuesArray;
        int var6 = valuesArray.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String value = var5[var7];
            //if (!StringUtils.isNullOrEmpty(value)) {
            if(!(value==null||"".equals(value))){
                sb.append(this.getNameByCode(Long.parseLong(value), excelDataType)).append(",");
            }
        }

        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        } else {
            return "";
        }
    }

    private String getNameByCode(Number value, ExcelDataType excelDataType) {
        if (excelDataType == ExcelDataType.Dict) {
            return "";//this.dictCacheSerivce.getDescByCodeId(Integer.parseInt(value.toString()));
        } else if (excelDataType == ExcelDataType.Region_Provice) {
            return "";//this.regionCacheService.getProvinceNameById((Long)value);
        } else if (excelDataType == ExcelDataType.Region_City) {
            return "";//this.regionCacheService.getCityNameById((Long)value);
        } else {
            return "";//excelDataType == ExcelDataType.Region_Country ? this.regionCacheService.getCountryNameById((Long)value) : null;
        }
    }

    private void generateTitleRow(Sheet sheet, List<ExcelExportColumn> columnDefineList) {
        Row row = sheet.createRow(0);
        CellStyle cellStyle = this.getSheetTitleStyle(sheet.getWorkbook());

        for(int i = 0; i < columnDefineList.size(); ++i) {
            sheet.setColumnWidth(i, ((ExcelExportColumn)columnDefineList.get(i)).getTitle().getBytes().length * 2 * 256);
            Cell cell = row.createCell(i);
            this.createStringCell(((ExcelExportColumn)columnDefineList.get(i)).getTitle(), cell, cellStyle);
        }

    }

    private void createStringCell(Object cellValue, Cell cell, CellStyle cellstyle) {
        cell.setCellType(1);
        cell.setCellStyle(cellstyle);
        cell.setCellValue((String)cellValue);
    }

    private void setSheetFinishStyle(Sheet sheet, int colSize) {
        for(int i = 0; i < colSize; ++i) {
            sheet.autoSizeColumn((short)i);
        }

        sheet.createFreezePane(0, 1, 0, 1);
    }

    private CellStyle getDefaultCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment((short)1);
        cellStyle.setVerticalAlignment((short)1);
        cellStyle.setBorderTop((short)1);
        cellStyle.setBorderBottom((short)1);
        cellStyle.setBorderLeft((short)1);
        cellStyle.setBorderRight((short)1);
        cellStyle.setWrapText(false);
        return cellStyle;
    }

    private Font getDefaultFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setColor((short)32767);
        font.setFontName("微软雅黑");
        return font;
    }

    private CellStyle getSheetTitleStyle(Workbook workbook) {
        Font font = this.getDefaultFont(workbook);
        font.setFontHeightInPoints((short)14);
        font.setBoldweight((short)700);
        CellStyle cellStyle = this.getDefaultCellStyle(workbook);
        cellStyle.setAlignment((short)2);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private CellStyle getSheetDateStyle(Workbook workbook, String format) {
        format = format == null ? "yyyy-MM-dd" : format;
        Font font = this.getDefaultFont(workbook);
        CellStyle cellStyle = this.getDefaultCellStyle(workbook);
        cellStyle.setAlignment((short)2);
        cellStyle.setFont(font);
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat(format));
        return cellStyle;
    }

    private CellStyle getSheetStringStyle(Workbook workbook) {
        Font font = this.getDefaultFont(workbook);
        CellStyle cellStyle = this.getDefaultCellStyle(workbook);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private CellStyle getSheetCodeDescStyle(Workbook workbook) {
        Font font = this.getDefaultFont(workbook);
        CellStyle cellStyle = this.getDefaultCellStyle(workbook);
        cellStyle.setAlignment((short)2);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private CellStyle getSheetDoubleStyle(Workbook workbook, String format) {
        format = format == null ? "#,##0.00##" : format;
        Font font = this.getDefaultFont(workbook);
        CellStyle cellStyle = this.getDefaultCellStyle(workbook);
        cellStyle.setAlignment((short)3);
        cellStyle.setFont(font);
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat(format));
        return cellStyle;
    }

    private CellStyle getSheetIntegerStyle(Workbook workbook, String format) {
        format = format == null ? "#,##0" : format;
        Font font = this.getDefaultFont(workbook);
        CellStyle cellStyle = this.getDefaultCellStyle(workbook);
        cellStyle.setAlignment((short)3);
        cellStyle.setFont(font);
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat(format));
        return cellStyle;
    }

    private Workbook createWorkbook() {
        Workbook workbook = new HSSFWorkbook();
        return workbook;
    }

    private OutputStream initOutputStream(HttpServletRequest request, HttpServletResponse response, String fileName)  {
        try {
            ExcelUtil.setExportFileName(request, response, fileName);
            return response.getOutputStream();
        } catch (Exception var5) {
            throw new RuntimeException("excel 流初始化失败", var5);
        }
    }

    /** @deprecated */
    @Deprecated
    protected void setWorkbookAttribute(Workbook wb) {
    }

    /** @deprecated */
    @Deprecated
    protected void setSheetAttribute(String sheetName, Sheet sheet, List<Map> rowList, String[] keys, String[] columnNames) {
    }

    /** @deprecated */
    @Deprecated
    protected void setRowAttribute(Row row, Map<String, Object> cellList) {
    }

    /** @deprecated */
    @Deprecated
    protected void setCellAttribute(Row row, Cell cell, String cellVal) {
    }
}

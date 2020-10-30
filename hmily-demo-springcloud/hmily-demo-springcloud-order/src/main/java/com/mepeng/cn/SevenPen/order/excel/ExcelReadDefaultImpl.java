package com.mepeng.cn.SevenPen.order.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ExcelReadDefaultImpl<T extends DataImportDto> implements ExcelRead<T> {
    @Autowired
    ValidateService validateService;

    @Override
    public Map<String, ImportResultDto<T>> analyzeExcel(MultipartFile excelFile, List<AbstractExcelReadCallBack<T>> callBackList) throws IOException {
        Object wb = null;

        try {
            String fileName = excelFile.getOriginalFilename();
            if (fileName.indexOf(".xlsx") > -1) {
                wb = new XSSFWorkbook(excelFile.getInputStream());
            } else {
                wb = new HSSFWorkbook(excelFile.getInputStream());
            }

            if (callBackList==null || callBackList.size() < 1) {
                throw new RuntimeException("excel 配置不正确");
            } else {
                Map<String, ImportResultDto<T>> sheetMap = new HashMap();

                for(int i = 0; i < callBackList.size(); ++i) {
                    Sheet sheet = ((Workbook)wb).getSheetAt(i);
                    sheetMap.put(sheet.getSheetName(), this.analyzeExcelXls(sheet, (AbstractExcelReadCallBack)callBackList.get(i)));
                }

                HashMap var11 = (HashMap) sheetMap;
                return var11;
            }
        } finally {
            if (wb != null) {
                Closeable wb1 = (Closeable) wb;
                try {
                    wb1.close();
                } catch (Exception e) {
                    //logger.warn("close stream " + stream.getClass() + " error", e);
                }
            }
            //IOUtils.closeStream((Closeable)wb);
        }
    }

    private ImportResultDto<T> analyzeExcelXls(Sheet sheet, AbstractExcelReadCallBack<T> callBack) {
        try {
//            LoginInfoDto loginInfo = (LoginInfoDto)ApplicationContextHelper.getBeanByType(LoginInfoDto.class);
//            Locale locale = loginInfo.getLocale();
            int rowCount = sheet.getLastRowNum();//这个Excel页签最后一行的数
            //logger.debug("rownum:" + rowCount);
            if (rowCount == 0) {
                //throw new ServiceBizException("导入的excel文件为空");
                throw new RuntimeException("导入的excel文件为空");
            } else {
                ArrayList<T> dataList = new ArrayList();
                ArrayList<T> errorList = new ArrayList();
                List<String> fieldList = new ArrayList();
                ImportResultDto<T> resultDto = new ImportResultDto();
                resultDto.setDataList(dataList);
                resultDto.setErrorList(errorList);
                Class<T> dtoClass = callBack.getDtoClass();
                Map<Integer, ExcelReadDefaultImpl<T>.ExcelMapping> columnMapping = this.getExcelColumnMapping(dtoClass);
                int cellNum = sheet.getRow(0).getLastCellNum();//获取第一行 最后一列数
                int startRow = this.getStartRow(dtoClass);//获取开始解析的行
                boolean isValidateSucess = true;

                int j;
                for(j = 0; j < startRow; ++j) {
                    Row row = sheet.getRow(j);//获取第一行

                    for(int k = 0; k < cellNum; ++k) {
                        Cell cell = row.getCell(k);
                        Object cellValue = getCellStringValue(cell);
                        fieldList.add(cellValue.toString());//列信息
                    }
                }
                //从开始解析行解析到本页签最大的行
                for(j = startRow; j <= rowCount; ++j) {
                    boolean rowSucess = true;
                    Row row = sheet.getRow(j);//获取此行
                    if (row != null) {
                        T tinstance = (T)dtoClass.newInstance();//实例化类
                        this.setExcelRowNO(tinstance, j);
                        int blankCnt = cellNum;

                        for(int k = 0; k < cellNum; ++k) {
                            Cell cell = row.getCell(k);
                            Object cellValue = getCellStringValue(cell);//获取此行此列单元格的数据
                            ExcelReadDefaultImpl<T>.ExcelMapping excelMapping = (ExcelReadDefaultImpl.ExcelMapping)columnMapping.get(k + 1);
                            if (excelMapping == null) {
                                throw new Exception("excel 配置不正确，当前列：" + (k + 1));
                            }

                            try {
                                this.setObjectValue(cellValue, tinstance, excelMapping, columnMapping);
                            } catch (Exception var32) {
                                //logger.error(var32.getMessage(), var32);
                                var32.printStackTrace();
                                System.out.println("msg:"+var32.getMessage());
                                this.setExcelErrorMsg(tinstance, (String)fieldList.get(k) + ":" + var32.getMessage());
                                if (isValidateSucess) {
                                    isValidateSucess = false;
                                }

                                rowSucess = false;
                                continue;
                            }

                            if (!"".equals(cellValue)) {
                                --blankCnt;
                            }
                        }

                        if (blankCnt >= cellNum) {
                            break;
                        }

                        /*Locale locale = null;
                        boolean isSucess = this.validateRowData(tinstance, locale, columnMapping, fieldList);
                        if (!isSucess) {
                            if (isValidateSucess) {
                                isValidateSucess = false;
                            }

                            rowSucess = false;
                        }*/

                        if (callBack.getExcelReadCallBack() != null) {
                            try {
                                callBack.getExcelReadCallBack().readRowCallBack(tinstance, isValidateSucess);
                            } catch (Exception var31) {
                                //logger.error(var31.getMessage(), var31);
                                var31.printStackTrace();
                                String errorMsg = var31.getMessage();
                                if (isValidateSucess) {
                                    isValidateSucess = false;
                                }

                                rowSucess = false;
                                this.setExcelErrorMsg(tinstance, errorMsg);
                            }
                        }

                        if (!rowSucess) {
                            errorList.add(tinstance);
                            //if (errorList.size() >= FrameworkCommonConstants.IMPORT_MAX_ERRORS_ROWS) {
                            if (errorList.size() >= 30) {
                                break;
                            }
                        }

                        dataList.add(tinstance);
                    }
                }

                if (dataList==null||dataList.isEmpty()) {
                    throw new RuntimeException("导入的excel文件为空");
                } else {
                    resultDto.setSucess(isValidateSucess);
                    return resultDto;
                }
            }
        } catch (Exception var33) {
            throw new RuntimeException(var33);
        } finally {
            ;
        }
    }

    //设置数据行号
    private void setExcelRowNO(T tinstance, int rowNo) {
        Class dtoClass = tinstance.getClass();

        try {
            Method setRowNOMethod = dtoClass.getMethod("setRowNO", Integer.class);
            this.setObjectValueByMethod(setRowNOMethod, tinstance, rowNo);
        } catch (Exception var6) {
            //logger.error(var6.getMessage(), var6);
            throw new RuntimeException("设置行号方法不存在");
        }
    }

    private Object getObjectValueByDefineType(T tinstance, ExcelDataType dataType, Map<Integer, ExcelReadDefaultImpl<T>.ExcelMapping> columnMapping) {
        try {
            Class<?> dtoClass = tinstance.getClass();
            Iterator var5 = columnMapping.entrySet().iterator();

            ExcelReadDefaultImpl.ExcelMapping mapping;
            ExcelColumnDefine columnDefine;
            do {
                if (!var5.hasNext()) {
                    return null;
                }

                Map.Entry<Integer, ExcelMapping> entry = (Map.Entry)var5.next();
                mapping = (ExcelReadDefaultImpl.ExcelMapping)entry.getValue();
                columnDefine = mapping.getColumnDefine();
            } while(dataType != columnDefine.dataType());

            Field field = mapping.getField();
            String fieldName = field.getName();
            String getFieldName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
            Method getValueMethod = dtoClass.getMethod(getFieldName);
            Object getValue = getValueMethod.invoke(tinstance);
            return getValue;
        } catch (Exception var14) {
            //logger.error(var14.getMessage(), var14);
            var14.printStackTrace();
            throw new RuntimeException("get 方法不存在");
        }
    }

    private void setExcelErrorMsg(T tinstance, String errorMsg) {
        Class dtoClass = tinstance.getClass();

        try {
            Method setErrorMsgMethod = dtoClass.getMethod("setErrorMsg", String.class);
            Method getErrorMsgMethod = dtoClass.getMethod("getErrorMsg");
            String errorMsgNow = (String)getErrorMsgMethod.invoke(tinstance);
            //if (StringUtils.isNullOrEmpty(errorMsgNow)) {
            if(errorMsgNow==null||"".equals(errorMsgNow)){
                this.setObjectValueByMethod(setErrorMsgMethod, tinstance, errorMsg);
            } else {
                this.setObjectValueByMethod(setErrorMsgMethod, tinstance, errorMsgNow + " ; " + errorMsg);
            }

        } catch (Exception var7) {
            //logger.error(var7.getMessage(), var7);
            var7.printStackTrace();
            throw new RuntimeException("设置错误信息的方法不存在");
        }
    }

    private boolean validateRowData(T tinstance, Locale locale, final Map<Integer, ExcelReadDefaultImpl<T>.ExcelMapping> columnMapping, final List<String> fieldList) {
        String errorMsg = this.validateService.validateObject(tinstance, new ValidateMessageCallBack() {
            public String messageCallBack(String constinatField, String message, String defaultMessage) {
                int errorFieldColumnx;
                if ((errorFieldColumnx = ExcelReadDefaultImpl.this.isExcelImportField(constinatField, columnMapping)) != -1) {
                    return message != null ? message : (String)fieldList.get(errorFieldColumnx - 1) + ":" + defaultMessage;
                } else {
                    return null;
                }
            }
        });
        if (!(errorMsg==null||"".equals(errorMsg))) {
            this.setExcelErrorMsg(tinstance, errorMsg.toString());
            return false;
        } else {
            return true;
        }
    }

    private int isExcelImportField(String fieldName, Map<Integer, ExcelReadDefaultImpl<T>.ExcelMapping> columnMapping) {
        Iterator var3 = columnMapping.entrySet().iterator();

        Map.Entry entry;
        String fieldExcelName;
        do {
            if (!var3.hasNext()) {
                return -1;
            }

            entry = (Map.Entry)var3.next();
            ExcelReadDefaultImpl<T>.ExcelMapping entryValue = (ExcelReadDefaultImpl.ExcelMapping)entry.getValue();
            fieldExcelName = entryValue.getField().getName();
        } while(!fieldExcelName.equals(fieldName));

        return (Integer)entry.getKey();
    }

    private void setObjectValue(Object cellValue, T tinstance, ExcelReadDefaultImpl<T>.ExcelMapping excelMapping, Map<Integer, ExcelReadDefaultImpl<T>.ExcelMapping> columnMapping) {
        try {
            Field field = excelMapping.getField();
            Method setMethod = excelMapping.getSetMethod();
            ExcelColumnDefine columnDefine = excelMapping.getColumnDefine();
            //if (!StringUtils.isNullOrEmpty(cellValue)) {
            if(!(cellValue==null||"".equals(cellValue.toString()))){
                Class<?> valueClass = cellValue.getClass();
                Class<?> fieldClass = field.getType();
                ExcelDataType dataType = columnDefine.dataType();
                if (dataType != null && dataType != ExcelDataType.NotDefine) {//不是未定义数据类型
                    if (dataType == ExcelDataType.Dict) {//如果是字典类型
                        Integer dataCode = columnDefine.dataCode();
                        if (dataCode != null && dataCode != -1) {
                            Integer codeId = 0;//this.dictCacheSerivce.getCodeIdByDesc(dataCode, cellValue + "");
                            this.setObjectValueByMethod(setMethod, tinstance, codeId);
                            return;
                        }

                        throw new RuntimeException("TC_CODE 字典数据定义不正确");
                    }

                    Long provinceId;
                    if (dataType == ExcelDataType.Region_Provice) {//省份数据
                        provinceId = 0L;//this.regionCacheService.getProvinceIdByName(cellValue + "");
                        this.setObjectValueByMethod(setMethod, tinstance, provinceId);
                        return;
                    }

                    Long cityId;
                    if (dataType == ExcelDataType.Region_City) {
                        provinceId = (Long)this.getObjectValueByDefineType(tinstance, ExcelDataType.Region_Provice, columnMapping);
                        cityId = 0L;//this.regionCacheService.getCityIdByName(provinceId, cellValue + "");
                        this.setObjectValueByMethod(setMethod, tinstance, cityId);
                        return;
                    }

                    if (dataType == ExcelDataType.Region_Country) {
                        provinceId = (Long)this.getObjectValueByDefineType(tinstance, ExcelDataType.Region_Provice, columnMapping);
                        cityId = (Long)this.getObjectValueByDefineType(tinstance, ExcelDataType.Region_City, columnMapping);
                        Long countryId = 0L;//this.regionCacheService.getCountryIdByName(provinceId, cityId, cellValue + "");
                        this.setObjectValueByMethod(setMethod, tinstance, countryId);
                        return;
                    }
                } else {
                    if (valueClass == fieldClass || fieldClass.toString().equals(valueClass.toString()) || valueClass.isAssignableFrom(fieldClass)) {
                        this.setObjectValueByMethod(setMethod, tinstance, cellValue);
                        return;
                    }

                    if (Date.class.toString().equals(fieldClass.toString()) && String.class.toString().equals(valueClass.toString())) {
                        String dateFormat = null;
                        //if (StringUtils.isNullOrEmpty(columnDefine.format())) {
                        if(columnDefine.format()==null||"".equals(columnDefine.format())){
                            dateFormat = "yyyy-MM-dd";
                        } else {
                            dateFormat = columnDefine.format();
                        }

                        Date formatDate = new SimpleDateFormat(dateFormat).parse(cellValue.toString());//DateUtil.parseDate(cellValue.toString(), dateFormat);
                        this.setObjectValueByMethod(setMethod, tinstance, formatDate);
                        return;
                    }
                    //方法类型为Integer 并且解析值类型为String 或 Double
                    if (Integer.class.toString().equals(fieldClass.toString()) && (String.class.toString().equals(valueClass.toString()) || Double.class.toString().equals(valueClass.toString()))) {
                        this.setObjectValueByMethod(setMethod, tinstance, (int)Double.parseDouble(cellValue.toString()));
                        return;
                    }

                    if (!Long.class.toString().equals(fieldClass.toString()) || (!String.class.toString().equals(valueClass.toString()) && !Double.class.toString().equals(valueClass.toString()))) {
                        if (!Double.class.toString().equals(fieldClass.toString()) || !String.class.toString().equals(valueClass.toString()) && !Integer.class.toString().equals(valueClass.toString())) {
                            if (String.class.toString().equals(fieldClass.toString()) && Double.class.toString().equals(valueClass.toString())) {
                                DecimalFormat df = new DecimalFormat("0.####");
                                String formatValue = df.format((Double)cellValue);

                                //this.setObjectValueByMethod(setMethod, tinstance, NumberUtil.getShortString((Double)cellValue));
                                this.setObjectValueByMethod(setMethod, tinstance, formatValue);
                                return;
                            }

                            return;
                        }

                        this.setObjectValueByMethod(setMethod, tinstance, Double.parseDouble(cellValue.toString()));
                        return;
                    }

                    this.setObjectValueByMethod(setMethod, tinstance, (long)Double.parseDouble(cellValue.toString()));
                    return;
                }
            }

        } catch (Exception var14) {
            var14.printStackTrace();
            throw new RuntimeException(cellValue + "-->数据类型不正确");
        }
    }

    private void setObjectValueByMethod(Method setMethod, T tinstance, Object... args) {
        try {
            setMethod.invoke(tinstance, args);
        } catch (Exception var5) {
            //logger.error(var5.getMessage(), var5);
            var5.printStackTrace();
            throw new RuntimeException("SetValue 出错");
        }
    }

    /**
     * 获取列的映射关系
     * @param dtoClass
     * @return
     */
    private Map<Integer, ExcelReadDefaultImpl<T>.ExcelMapping> getExcelColumnMapping(Class<?> dtoClass) {
        Field[] fieldList = dtoClass.getDeclaredFields();
        Map<Integer, ExcelReadDefaultImpl<T>.ExcelMapping> columnMapping = new HashMap();
        if (fieldList != null && fieldList.length > 0) {
            for(int i = 0; i < fieldList.length; ++i) {
                Field field = fieldList[i];
                ExcelColumnDefine excelColumnDefine = (ExcelColumnDefine)field.getAnnotation(ExcelColumnDefine.class);
                if (excelColumnDefine != null) {
                    Method setValueMethod = null;

                    try {
                        String fieldName = field.getName();
                        String setFieldName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
                        setValueMethod = dtoClass.getMethod(setFieldName, field.getType());
                    } catch (Exception var10) {
                        //logger.error(var10.getMessage(), var10);
                        throw new RuntimeException("未设置set 方法");
                    }

                    columnMapping.put(excelColumnDefine.value(), new ExcelReadDefaultImpl.ExcelMapping(field, excelColumnDefine, setValueMethod));
                }
            }
        }else{
            throw new RuntimeException("未设置映射字段");
        }

        return columnMapping;
    }

    private ExcelDefine getExcelDefine(Class<?> dtoClass) {
        ExcelDefine excelDefine = (ExcelDefine)dtoClass.getAnnotation(ExcelDefine.class);
        return excelDefine;
    }

    private int getStartRow(Class<?> dtoClass) {
        ExcelDefine excelDefine = this.getExcelDefine(dtoClass);
        return excelDefine != null ? excelDefine.startRow() : 1;
    }

    public ImportResultDto<T> analyzeExcelFirstSheet(MultipartFile excelFile, AbstractExcelReadCallBack<T> callBack) throws IOException {
        List<AbstractExcelReadCallBack<T>> callBackList = new ArrayList();
        callBackList.add(callBack);
        Map<String, ImportResultDto<T>> excelMap = this.analyzeExcel(excelFile, callBackList);
        //if (!CommonUtils.isNullOrEmpty(excelMap)) {
        if(!(excelMap==null||excelMap.isEmpty())){
            Iterator<ImportResultDto<T>> iterator = excelMap.values().iterator();
            if (iterator.hasNext()) {
                return (ImportResultDto)iterator.next();
            } else {
                throw new RuntimeException("excel 不正确");
            }
        } else {
            throw new RuntimeException("excel 不正确");
        }
    }

    private static Object getCellStringValue(Cell cell) {
        if (cell != null) {
            switch(cell.getCellType()) {
                case 0:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    } else {
                        if (cell.getCellStyle().getDataFormat() == 58) {
                            double value = cell.getNumericCellValue();
                            return org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                        }

                        return cell.getNumericCellValue();
                    }
                case 1:
                    String cellStringValue = cell.getRichStringCellValue().toString();
                    //if (StringUtils.isNullOrEmpty(cellStringValue.trim())) {
                    if(cellStringValue==null||"".equals(cellStringValue.trim())){
                        cellStringValue = " ";
                    }

                    return cellStringValue;
                case 2:
                    cell.setCellType(0);
                    return String.valueOf(cell.getNumericCellValue());
                case 3:
                    return "";
                case 4:
                    return "";
                case 5:
                    return "";
                default:
                    return "";
            }
        } else {
            return "";
        }
    }

    class ExcelMapping {
        private Field field;
        private ExcelColumnDefine columnDefine;
        private Method setMethod;

        ExcelMapping(Field field, ExcelColumnDefine columnDefine, Method setMethod) {
            this.field = field;
            this.columnDefine = columnDefine;
            this.setMethod = setMethod;
        }

        public Field getField() {
            return this.field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public ExcelColumnDefine getColumnDefine() {
            return this.columnDefine;
        }

        public void setColumnDefine(ExcelColumnDefine columnDefine) {
            this.columnDefine = columnDefine;
        }

        public Method getSetMethod() {
            return this.setMethod;
        }

        public void setSetMethod(Method setMethod) {
            this.setMethod = setMethod;
        }
    }
}

package com.mepeng.cn.SevenPen.order.excel.controller;

import com.alibaba.fastjson.JSON;
import com.mepeng.cn.SevenPen.order.excel.AbstractExcelReadCallBack;
import com.mepeng.cn.SevenPen.order.excel.Dto.PrivateNumInfo;
import com.mepeng.cn.SevenPen.order.excel.ExcelRead;
import com.mepeng.cn.SevenPen.order.excel.ExcelReadCallBack;
import com.mepeng.cn.SevenPen.order.excel.ImportResultDto;
import com.mepeng.cn.SevenPen.order.excel.gen.ExcelExportColumn;
import com.mepeng.cn.SevenPen.order.excel.gen.ExcelGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/excel")
public class ExcelImportController {
    private static final Logger logger = LoggerFactory.getLogger(ExcelImportController.class);
    @Autowired
    private LeakyBucket leakyBucket;
    @Autowired
    private ExcelRead<PrivateNumInfo> excelReadService;

    //@Value("${bucketsize}")
    //private int bucketsize=5;

    /**
     * 限制流
     * @param response
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping("/exportDataExcelExportColumnDTO")
    public void exportDataExcelExportColumnDTO(HttpServletResponse response){
        //LeakyBucket leakyBucket = new LeakyBucket(bucketsize);
        boolean acquire = leakyBucket.acquire();
        try {
            System.out.println("exportDataExcelExportColumnDTO is doing----");
            if (!acquire) {
                //限流
                logger.info("限流了");
                response.setStatus(603);
            } else {
                try {
                    leakyBucket.increase();
                    System.out.println("is doing 66666666");
                    Thread.sleep(1000_000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    leakyBucket.decrease();//释放并发数
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 导入
     * @param importFile
     * @param uriCB
     * @param rpDto
     * @throws Exception
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public void importPrivateBindInfos(@RequestParam(value = "file") MultipartFile importFile, UriComponentsBuilder uriCB, final PrivateNumInfo rpDto)  throws Exception{
        ImportResultDto<PrivateNumInfo> importResult = excelReadService.analyzeExcelFirstSheet(importFile,new AbstractExcelReadCallBack<PrivateNumInfo>(PrivateNumInfo.class,new ExcelReadCallBack<PrivateNumInfo>() {
            @Override
            public void readRowCallBack(PrivateNumInfo rowDto, boolean isValidateSucess) {
                try{
                    // 写业务代码 //privateNumberMngService.importPrivateNumInfo(rowDto,isValidateSucess);
                    System.out.println("json Dto:"+JSON.toJSONString(rowDto));
                }catch(Exception e){
                    throw e;
                }
            }
        }));

        if(!importResult.isSucess()){
            System.out.println(importResult.getErrorList());
            //throw new ServiceBizException("导入出错,请见错误列表",importResult.getErrorList()) ;
            throw new RuntimeException("导入出错,请见错误列表 错误条数:"+importResult.getErrorList().size());
        }
    }

    @Autowired
    private ExcelGenerator excelService;

    /**
     * 导出excel
     * @param queryParam
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/export/excel", method = RequestMethod.GET)
    public void exportPrivateNumInfos(@RequestParam Map<String, String> queryParam, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        List<Map> resultList = new ArrayList<>();
        Map<String, List<Map>> excelData = new HashMap<>();
        excelData.put("隐私号码导入模版", resultList);
        List<ExcelExportColumn> exportColumnList = new ArrayList<>();
        exportColumnList.add(new ExcelExportColumn("PRIVATE_NUM","隐私号码"));
        exportColumnList.add(new ExcelExportColumn("X_STATUS","隐私号码状态"));
        exportColumnList.add(new ExcelExportColumn("CITY_NO","城市码"));
        exportColumnList.add(new ExcelExportColumn("CITY_NAME","城市名称"));
        Map m1 = new HashMap();
        m1.put("PRIVATE_NUM","+8617170733774");
        m1.put("X_STATUS","10011001");
        m1.put("CITY_NO","7220");
        m1.put("CITY_NAME","柳州");
        resultList.add(m1);
        excelService.generateExcel(excelData, exportColumnList, "隐私号码导入模版.xls", request, response);
    }
}

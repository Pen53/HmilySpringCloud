package com.mepeng.cn.SevenPen.order.excel.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mepeng.cn.SevenPen.order.excel.DataImportDto;
import com.mepeng.cn.SevenPen.order.excel.ExcelColumnDefine;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class PrivateNumInfo  extends DataImportDto {
    //名称
    @ExcelColumnDefine(value=1)
    private String name;
    //城市
    @ExcelColumnDefine(value=2)
    private String city;
    //年龄
    @ExcelColumnDefine(value=3)
    private String age;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String subscribeTime; //绑定时的服务端系统时间
    private Boolean recordFlag; //是否通话录音
    private String recordHintTone; //录音提示音文件名
}

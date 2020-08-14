package com.mepeng.cn.SevenPen.order.kryo.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class PenHimlyEntity {
    private Long id;
    private String targetClass;
    private String targetMethod;
    private String confirmMethod;
    private String cancelMethod;
    private Date createTime;
    private Date lastTime;
    private byte[] data;
}

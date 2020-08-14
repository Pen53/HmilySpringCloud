package com.mepeng.cn.SevenPen.order.kryo.Dto;

import lombok.Data;

import java.util.List;

@Data
public class TestSerialization {
    private String text;
    private String name;
    private Long id;
    private Boolean flag;
    private List<?> list;
    private SubTestSerialization subTestSerialization;
}

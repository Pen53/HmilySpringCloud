package com.mepeng.cn.SevenPen.order.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Test implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 创建时间.
     */
    private Date createTime;
    /**
     * 状态.
     */
    private Integer status;
    /**
     * uuid 操作唯一ID
     */
    private String uuid;
}


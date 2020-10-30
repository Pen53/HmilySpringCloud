package com.mepeng.cn.SevenPen.order.excel;

public interface ValidateService {
    <T> String validateObject(T var1);

    <T> String validateObject(T var1, ValidateMessageCallBack var2);

    <T> boolean isValidObject(T var1);
}

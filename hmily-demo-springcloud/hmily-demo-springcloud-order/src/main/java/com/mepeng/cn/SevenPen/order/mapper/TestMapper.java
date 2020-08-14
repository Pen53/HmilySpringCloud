package com.mepeng.cn.SevenPen.order.mapper;

import com.mepeng.cn.SevenPen.order.entity.Test;

public interface TestMapper {
    public int insert(Test test);

    public int update(Test test);

    public int delete(Test test);

    public Test selectById(Long id);

    public Test selectByUuid(String uuid);
}

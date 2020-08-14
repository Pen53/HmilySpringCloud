package com.mepeng.cn.SevenPen.order.mapper;


import com.mepeng.cn.SevenPen.order.entity.Demo;
import com.mepeng.cn.SevenPen.order.kryo.Dto.PenHimlyEntity;

import java.util.List;
import java.util.Map;

public interface DemoMapper {
    public int insert(Demo demo);

    public int update(Demo demo);

    public int delete(Demo demo);

    public Demo selectById(Long id);

    public Demo selectByUuid(String uuid);

    List<Map<String, Object>> demoLocalSQL(Map<String, Object> map);

    int insertPenHimlyEntity(PenHimlyEntity penHimlyEntity);

    PenHimlyEntity selectPenHimlyEntityById(Long id);
}

package com.mepeng.cn.SevenPen.order.excel;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class ImportResultDto <T extends DataImportDto> implements Serializable {
    private static final long serialVersionUID = -5294248904920920280L;
    private boolean isSucess;
    private ArrayList<T> dataList;
    private ArrayList<T> errorList;
}

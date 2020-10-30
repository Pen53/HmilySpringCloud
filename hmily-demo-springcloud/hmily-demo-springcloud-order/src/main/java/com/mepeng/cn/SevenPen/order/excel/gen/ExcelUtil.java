package com.mepeng.cn.SevenPen.order.excel.gen;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class ExcelUtil {

    public static void setExportFileName(HttpServletRequest request, HttpServletResponse response, String fileName) {
        setExportFileName(request, response, fileName, (Long)null);
    }

    public static void setExportFileName(HttpServletRequest request, HttpServletResponse response, String fileName, Long fileLength) {
        try {
            String userAgent = request.getHeader("user-agent");
            System.out.println("user-agent=" + userAgent);
            String mineType = (new MimetypesFileTypeMap()).getContentType(fileName);
            response.setContentType(mineType);
            String rtn = null;
            if (null != userAgent) {
                userAgent = userAgent.toLowerCase();
                rtn = "filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF8");
                System.out.println("default");
                System.out.println("rtn=" + rtn);
            } else {
                rtn = "filename=\"" + URLEncoder.encode(fileName, "UTF8") + '"';
                System.out.println("other browser");
                System.out.println("rtn=" + rtn);
            }

            System.out.println("attachment=attachment;" + rtn);
            System.out.println("\n");
            response.setHeader("Content-Disposition", "attachment;" + rtn);
            if (fileLength != null) {
                response.addHeader("Content-Length", "" + fileLength);
            }

        } catch (Exception var7) {
            var7.printStackTrace();
            throw new RuntimeException("获取导出文件名称失败", var7);
        }
    }
}

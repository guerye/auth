package com.wisdom.auth.provider.util;



import com.riozenc.titanTool.common.ClassDAOXmlUtil;
import com.wisdom.auth.provider.mapper.model.DeptInfo;


/**
 * 根据Domain 创建xml
 */
public class SystemUtil {
    public static void main(String[] args) throws Exception{
        String docpath = System.getProperty("user.dir");
        System.out.println(docpath);//user.dir指定了当前的路径
        docpath = docpath.replaceAll("\\\\","/");
        System.out.println(docpath);//user.dir指定了当前的路径

        docpath += "/src/main/java/com/wisdom/auth/provider/mapper";
        docpath += "/model";
        System.out.println(docpath);
//        String docpath ="C:/workspaces/wisdomZhgl/ftp/dao/ftp/dao";
        ClassDAOXmlUtil.buildXML(docpath, DeptInfo.class,"DEPT_INFO");
    }



}
package com.jxd.oa.constants;


import com.jxd.oa.bean.Department;
import com.jxd.oa.bean.Role;
import com.jxd.oa.bean.User;

/**
 * *****************************************************
 *
 * @Description : 常量类
 * @Author : cy cy20061121@163.com
 * @Creation Date : 2013-6-24 下午8:58:47
 * *****************************************************
 */

public class Constant {

    //服务器地址
    public final static String BASE_URL = "http://116.255.136.99/jxdoa/";
    //服务器头像存入 地址
    public final static String SERVER_PHOTO_FOLDER = "yxuploadfile/images/";
    //下载更新UI广播uri
    public final static String ACTION_REFRESH = "com.oa.uiRefresh.action";
    public static final String ACTION_EXIT = "com.oa.exitApp.action";
    //圆角
    public final static int ROUND_CORNER = 6;
    public static final int PHOTO_SIZE = 128;

    public static final Object[][] SYNC_DATA_TAG = {
            {Department.class, "部门"},
            {User.class, "用户"},
            {Role.class, "角色"}
    };

    public static final String FOLDER_DOWNLOAD = "download";

    public static final int CALL_PHONE = 1;
    public static final int SEND_MSG = 2;
}

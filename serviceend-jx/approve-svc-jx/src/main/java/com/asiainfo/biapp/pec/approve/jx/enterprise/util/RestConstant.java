package com.asiainfo.biapp.pec.approve.jx.enterprise.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoulc
 * @title: RestConstant
 * @date 2019/10/10 17:54
 */
public class RestConstant {
    public static final int NUMBER_INTEGER_0 = 0;
    public static final int NUMBER_INTEGER_1 = 1;
    public static final int NUMBER_INTEGER_3 = 3;
    public static final int NUMBER_SHORT_0 = 0;
    public static final int NUMBER_SHORT_1 = 1;
    public static final short NUMBER_SHORT__1 = -1;
    public static final String APPROVE_RESULT = "1";
    public static final String APPROVE_DESC = "审批通过";
    public static final String NUMBER_STRING_0 = "0";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String SMS_CHANNEL_ADIV_ID = "2";

    public static final String PARAMS_ENCONDING = "utf-8";

    public static final String CAMPSEG_STATUS = "54,59,91";

    public static final String MCD_EXTERNAL_FEEDBACK_FILENAME = "MCD_FK_";
    public static final String LINE_SEPARATOR = "\r\n";
    public static final String EXPORT_FILE_SEPARATOR = ",";

    private static final Map<String, String> MCD_CHANNEL_GROUPID_PREFIX = new HashMap<String, String>(){{
        put("701", "YXK");
        put("702", "BT");
    }};
    public static String getGroupIdPrefix(String key) {
        return MCD_CHANNEL_GROUPID_PREFIX.get(key);
    }
}

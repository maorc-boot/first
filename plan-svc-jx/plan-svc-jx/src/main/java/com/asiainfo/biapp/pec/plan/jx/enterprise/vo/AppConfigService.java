package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import java.util.concurrent.ConcurrentMap;

public class AppConfigService {


    /**
     * 实施的省份
     */
    private static String PROFILE_ACTIVE;
    private static ConcurrentMap<String, Object> SYS_DIC;

    public static void setProfile(String profile) {
        PROFILE_ACTIVE = profile;
    }
    public static String getProfile() {
        return PROFILE_ACTIVE;
    }
    public static void setSysDic(ConcurrentMap<String, Object> sysDic) {
        SYS_DIC = sysDic;
    }
    public static ConcurrentMap<String, Object> getSysDic() {
        return SYS_DIC;
    }

    /**
     * 从MCD_SYS_DIC中查找key-value数据 如果查找则返回value
     * 查询不到则返回null
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) throws Exception {
        if (SYS_DIC == null) {
            throw new Exception("系统字典数据加载失败");
        }else {
            String value = (String) SYS_DIC.get(key);
            return value;
        }

    }

    ;
}

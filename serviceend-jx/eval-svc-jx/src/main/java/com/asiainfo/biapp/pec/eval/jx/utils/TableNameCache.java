package com.asiainfo.biapp.pec.eval.jx.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mamp
 * @date 2022/9/9
 */
public interface TableNameCache {

    /**
     * 已创建表名缓存
     */
    Map<String ,Boolean> TabbleNameMap = new ConcurrentHashMap<>();

}

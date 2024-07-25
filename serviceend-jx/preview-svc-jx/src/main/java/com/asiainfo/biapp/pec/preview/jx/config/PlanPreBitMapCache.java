package com.asiainfo.biapp.pec.preview.jx.config;


import com.asiainfo.biapp.pec.common.jx.service.IBitMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mamp
 * @date 2022/6/28
 */
public class PlanPreBitMapCache {

    /**
     * 产品偏好BitMap
     */
    public static Map<String, Map<String, IBitMap>> planPreBitMaps = new ConcurrentHashMap();

    /**
     * 临时BitMap
     */
    public static Map<String, Map<String, IBitMap>> tmpBitMaps = new ConcurrentHashMap();

    /**
     * 产品偏好score
     */
    public static Map<String, Map<String, Double>> planPreScores = new ConcurrentHashMap();

    /**
     * 临时score
     */
    public static Map<String, Map<String, Double>> tmpScores = new ConcurrentHashMap();

    /**
     * 本地时间戳
     */
    public static String LOCAL_TIMESTAMP = "";
    /**
     * Bitmap文件名称
     */
    public static String PLAN_PRE_BITMAPS_FILE_NAME = "PLAN_PRE_BITMAPS_FILE";
    /**
     * 偏好平均值文件名称
     */
    public static String PLAN_PRE_SCORE_FILE_NAME = "PLAN_PRE_SCORE_FILE";

}

package com.asiainfo.biapp.pec.preview.jx.util;

/**
 * @author mamp
 * @date 2022/9/22
 */
public interface PreviewConst {

    /**
     * 草稿
     */
    Integer CAMPSEG_STATUS_CRAFT = 20;
    /**
     * 预演中
     */
    Integer CAMPSEG_STATUS_PREVIEW_RUNNING = 31;
    /**
     * 预演完成
     */
    Integer CAMPSEG_STATUS_PREVIEW_SUCCESS = 32;
    /**
     * 预演失败
     */
    Integer CAMPSEG_STATUS_PREVIEW_FAIL = 33;

    /**
     * 未预演
     */
    Integer PREVIEW_STATUS_NOT = 0;
    /**
     * 预演任务已提交
     */
    Integer PREVIEW_STATUS_SUBMITED = 101;
    /**
     * 预演中
     */
    Integer PREVIEW_STATUS_RUNNING = 1;
    /**
     * 预演完成
     */
    Integer PREVIEW_STATUS_DONE = 2;
    /**
     * 预演异常
     */
    Integer PREVIEW_STATUS_ERROR = 3;


    String CUST_CHN_PRE_CAL = "CUST_CHN_PRE_CAL";

    String CHN_PRE_BITMAP_FILE_DIR = "D:\\PRE_BITMAP\\";
    String CUST_BITMAP_FILE_DIR = "D:\\CUST_BITMAP\\";
    String CUST_FILE_DIR = "D:\\custgroup\\";

    /**渠道偏好模型文件接口机IP*/
    String CHN_PRE_FILE_HOST = "mcd.chn.pre.host";
    /**渠道偏好模型文件接口机端口*/
    String CHN_PRE_FILE_PORT = "mcd.chn.pre.port";
    /**渠道偏好模型文件接口机登录用户*/
    String CHN_PRE_FILE_USER = "mcd.chn.pre.user";
    /**渠道偏好模型文件接口机登录密码*/
    String CHN_PRE_FILE_PASSWD = "mcd.chn.pre.password";
    /**渠道偏好模型文件接口机目录*/
    String CHN_PRE_FILE_SERVER_PATH = "mcd.chn.pre.serverPath";
    /**渠道偏好模型文件编码*/
    String CHN_PRE_FILE_ENCODING = "mcd.chn.pre.encoding";
    /**渠道偏好模型文件本地路径*/
    String CHN_PRE_FILE_LOCAL_PATH = "mcd.chn.pre.locaPath";
    /**渠道偏好模型文件本地备份路径*/
    String CHN_PRE_FILE_BAK_PATH = "mcd.chn.pre.bakPath";

    /**渠道偏好模型文件本地备份路径*/
    String CHN_PRE_MODLE_FILE_NAME = "mcd.chn.pre.query.file.name";

    String PREVIEW_LOCK_PREFIX = "PREVIEW_LOCK_";

    String PLAN_PRE_REDIS_TOPIC = "PLAN_PRE_REDIS_TOPIC";

    String PLAN_PRE_TIMESTAMP = "PLAN_PRE_TIMESTAMP";

}

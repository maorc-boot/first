package com.asiainfo.biapp.pec.approve.jx.Enum;

import lombok.Getter;

/**
 * @author wanghao
 * 审批岗位类型 0人员 1部门 2角色 3区县 4 网格
 */

@Getter
public class ApproverType {
    /**
     * 人员
     */
    public static final String PERSONNEL = "0";

    /**
     * 部门
     */
    public static final String BRANCH = "1";

    /**
     * 角色
     */
    public static final String ROLE = "2";

    /**
     * 区县
     */
    public static final String COUNTY = "3";

    /**
     * 网格
     */
    public static final String GRID = "4";
}

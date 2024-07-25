package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCampCoordinationTaskListModel;
import com.asiainfo.biapp.pec.plan.jx.coordination.model.McdCampCoordinationTaskModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @author ranpf
 * @date 2023/6/9
 */

@Data
public class McdSaveStrategicCoordinationReq {


    @ApiModelProperty(value = "策略池任务实例")
    private McdCampCoordinationTaskModel taskModel;

    @ApiModelProperty(value = "策略池任务策略集合")
    private List<McdCampCoordinationTaskListModel> list;


}

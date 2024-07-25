package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.plan.model.McdCampDef;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveTestRequestDTO {

    private List<PlanExtInfo> planExtInfoList;
    private McdCampDef mcdCampDef;
}

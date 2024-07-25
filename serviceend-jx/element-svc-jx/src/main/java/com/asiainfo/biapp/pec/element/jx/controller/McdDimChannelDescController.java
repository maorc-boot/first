package com.asiainfo.biapp.pec.element.jx.controller;


import com.asiainfo.biapp.pec.element.jx.entity.McdDimChannelDesc;
import com.asiainfo.biapp.pec.element.jx.service.McdDimChannelDescService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcd-dim-channel-desc")
@Api(value = "江西:渠道说明", tags = {"渠道说明"})
@Slf4j
public class McdDimChannelDescController {

    @Autowired
    private McdDimChannelDescService mcdDimChannelDescService;

    @PostMapping("/query")
    public McdDimChannelDesc queryById(@RequestBody McdDimChannelDesc mcdDimChannelDesc) {
        return mcdDimChannelDescService.queryById(mcdDimChannelDesc.getChannelId());
    }
}

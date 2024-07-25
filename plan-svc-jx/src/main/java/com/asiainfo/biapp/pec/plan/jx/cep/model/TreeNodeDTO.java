package com.asiainfo.biapp.pec.plan.jx.cep.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mamp
 * @date 2022/5/5
 */
@Getter
@Setter
@TableName("CEP_DIM_EVENT_CLASS")
@ApiModel(value = "事件分类信息", description = "事件分类信息")

public class TreeNodeDTO {

    private String id;
    private String parentId;
    private String label;
    private List<TreeNodeDTO> children = new ArrayList<>();

    public void add(TreeNodeDTO node) {
        if ("0".equals(node.parentId)) {
            this.children.add(node);
        } else if (node.parentId.equals(this.id)) {
            this.children.add(node);
        } else {
            //递归调用add()添加子节点
            for (TreeNodeDTO tmp_node : children) {
                tmp_node.add(node);
            }
        }
    }
}

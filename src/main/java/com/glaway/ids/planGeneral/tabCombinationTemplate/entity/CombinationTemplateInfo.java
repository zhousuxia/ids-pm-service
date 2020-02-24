package com.glaway.ids.planGeneral.tabCombinationTemplate.entity;

import com.glaway.foundation.common.entity.GLObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 组合模板信息
 * @author: sunmeng
 * @ClassName: CombinationTemplateInfo
 * @Date: 2019/8/30-13:31
 * @since
 */
@Entity(name = "CombinationTemplateInfo")
@Table(name = "pm_combination_template_info")
@Getter
@Setter
public class CombinationTemplateInfo extends GLObject {

    //组合模板名称
    @Basic
    private String name;

    //页签模版Id
    @Basic
    private String typeId;

    //页签组合模版Id
    @Basic
    private String tabCombinationTemplateId;

    //业务条件
    @Basic
    private String displayAccess;

    //排序字段
    @Basic
    private int orderNum = 0;
}

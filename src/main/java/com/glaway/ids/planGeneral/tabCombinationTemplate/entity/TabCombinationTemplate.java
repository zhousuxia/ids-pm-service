package com.glaway.ids.planGeneral.tabCombinationTemplate.entity;

import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.foundation.common.entity.GLObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 页签组合模板
 * @author: sunmeng
 * @ClassName: TabCombinationTemplate
 * @Date: 2019/8/29-18:47
 * @since
 */
@Entity(name = "TabCombinationTemplate")
@Table(name = "pm_tab_combination_template")
@Getter
@Setter
public class TabCombinationTemplate extends BusinessObject {

    //模版名称
    @Basic
    private String name;

    @Basic
    private String status;

    //编号
    @Basic
    private String code;

    //活动类型Id
    @Basic
    private String activityId;

    //模版类型(活动类型管理-活动名称)
    @Basic
    private String templateName;

    //备注
    @Basic
    private String remake;

    //逻辑删除标识，"1"默认，"0"表示删除
    @Basic
    private String avaliable = "1";

    /**
     * <!-- begin-user-doc -->流程实例Id <!-- end-user-doc -->
     *
     * @generated
     */
    @Basic()
    private String processInstanceId = null;

}

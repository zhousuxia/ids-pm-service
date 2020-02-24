package com.glaway.ids.qualityTest.entity;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.entity.GLObject;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @Description: 质量检查单
 * @author:
 * @ClassName: CofigFormTest
 * @Date: 2019/10/29-11:43
 * @since
 */
@Data
@Table(name = "PM_COFIGFORM_TEST")
@Entity(name = "CofigFormTest")
public class CofigFormTest extends GLObject {

    /**
     * 质量单名
     */
    @Basic
    private String name;

    /**
     * 检查人
     */
    @Basic
    private String checkPerson;

    /**
     * 检查人信息
     */
    @Transient
    private String checkInfo;

    /**
     * 周期
     */
    @Basic
    private String period;

    /**
     * 结论
     */
    @Basic
    private String approve;

    /**
     * 备注
     */
    @Basic
    private String remark;

    /**
     * 计划关联id
     */
    @Basic
    private String planId;
}

package com.glaway.ids.qualityTest.entity;

import com.glaway.foundation.common.entity.GLObject;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 质量检查项
 * @author:
 * @ClassName: CofigDataGridTest
 * @Date: 2019/10/29-13:22
 * @since
 */
@Data
@Table(name = "PM_COFIGDATAGRID_TEST")
@Entity(name = "CofigDataGridTest")
public class CofigDataGridTest extends GLObject {

    /**
     * 名称
     */
    @Basic
    private String name;

    /**
     * 规则
     */
    @Basic
    private String rule;

    /**
     * 计划关联id
     */
    @Basic
    private String planId;
}

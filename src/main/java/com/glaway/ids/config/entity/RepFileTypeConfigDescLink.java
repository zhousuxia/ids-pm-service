

package com.glaway.ids.config.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.glaway.foundation.common.entity.GLObject;


/*
 * 文件名：RepFileTypeConfigDescLink.java
 * 版权：Copyright by www.glaway.com
 * 描述： 文档类型设置描述与文档类型的关联对象
 * 修改人：zhousuxia
 * 修改时间：2018年7月26日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

@Entity(name = "RepFileTypeConfigDescLink")
@Table(name = "PM_REPFILETYPE_DESC_LINK")
public class RepFileTypeConfigDescLink extends GLObject{
    
    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 文档类型id
     */
    @Basic()
    private String repFileTypeId = null;
    
    /**
     * 描述
     */
    @Basic()
    @Column(length = 4000)
    private String description = null;

    public String getRepFileTypeId() {
        return repFileTypeId;
    }

    public void setRepFileTypeId(String repFileTypeId) {
        this.repFileTypeId = repFileTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}

package com.glaway.ids.planGeneral.activityTypeManage.entity;

import com.glaway.foundation.common.entity.GLObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 活动类型管理信息
 * <p>
 * Created by LHR on 2019/8/26.
 */
@Entity(name = "activityTypeManage")
@Table(name = "PM_ACTIVITY_TYPE_MANAGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityTypeManage extends GLObject {

    @Basic()
    private String code;

    @Basic()
    private String name;

    @Basic()
    private String status = "enable";

    @Basic()
    private String remark;

    @Basic()
    private String avaliable = "1";

    /**
     * "0"系统内置,"1不是系统内置
     */
    @Basic()
    private String source = "1";

    @Transient()
    private String statusName;
}

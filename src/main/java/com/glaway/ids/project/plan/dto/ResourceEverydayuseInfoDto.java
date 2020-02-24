package com.glaway.ids.project.plan.dto;


import com.glaway.foundation.common.entity.GLObject;
import com.glaway.foundation.common.vdata.GLVData;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * A representation of the model object '<em><b>ResourceEverydayuseInfo</b></em>
 * '. <!-- begin-user-doc --> 资源每日使用信息表<!-- end-user-doc -->
 * @author wangyangzan
 */
@Data
public class ResourceEverydayuseInfoDto extends GLVData {

    /**
     * <!-- begin-user-doc -->项目ID<!-- end-user-doc -->
     * 
     * @generated
     */
    private String projectId = null;

    /**
     * <!-- begin-user-doc -->资源ID<!-- end-user-doc -->
     * 
     * @generated
     */
    private String resourceId = null;

    /**
     * <!-- begin-user-doc -->使用对象类型<!-- end-user-doc -->
     * 
     * @generated
     */
    private String useObjectType = null;

    /**
     * <!-- begin-user-doc -->使用对象ID<!-- end-user-doc -->
     * 
     * @generated
     */
    private String useObjectId = null;

    /**
     * <!-- begin-user-doc -->使用率<!-- end-user-doc -->
     * 
     * @generated
     */
    private String useRate = null;

    /**
     * <!-- begin-user-doc -->使用时间<!-- end-user-doc -->
     * 
     * @generated
     */
    private Date useDate = null;


    @Override
    public String toString() {
        return "ResourceEverydayuseInfo " + " [projectId: " + getProjectId() + "]"
               + " [resourceId: " + getResourceId() + "]" + " [useObjectType: "
               + getUseObjectType() + "]" + " [useObjectId: " + getUseObjectId() + "]"
               + " [useRate: " + getUseRate() + "]" + " [useDate: " + getUseDate() + "]";
    }
}

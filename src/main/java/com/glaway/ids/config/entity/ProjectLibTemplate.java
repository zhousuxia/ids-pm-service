package com.glaway.ids.config.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.glaway.foundation.common.dto.TSUserDto;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.glaway.foundation.common.entity.GLObject;
import com.glaway.foundation.system.event.EventSource;


/**
 * A representation of the model object '<em><b>ProjectLibTemplate</b></em>'.
 * <!-- begin-user-doc -->配置-项目库权限模板<!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ProjectLibTemplate")
@Table(name = "CM_PROJLIB_TEMPLATE")
public class ProjectLibTemplate extends GLObject implements EventSource {

    /**
     * <!-- begin-user-doc -->是否有效<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String avaliable = "1";

    /**
     * <!-- begin-user-doc -->名称 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->备注<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length = 2000)
    private String remark = null;

    /**
     * <!-- begin-user-doc -->状态:默认为“1”，表示启用；“0”表示禁用。<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String status = null;

    /**
     * <!-- begin-user-doc -->创建人<!-- end-user-doc -->
     */
    private TSUserDto creator;

    /**
     * Returns the value of '<em><b>avaliable</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>avaliable</b></em>' feature
     * @generated
     */
    public String getAvaliable() {
        return avaliable;
    }

    /**
     * Sets the '{@link ProjectLibTemplate#getAvaliable() <em>avaliable</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newAvaliable
     *            the new value of the '{@link ProjectLibTemplate#getAvaliable() avaliable}'
     *            feature.
     * @generated
     */
    public void setAvaliable(String newAvaliable) {
        avaliable = newAvaliable;
    }

    /**
     * Returns the value of '<em><b>name</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>name</b></em>' feature
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the '{@link ProjectLibTemplate#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link ProjectLibTemplate#getName() name}' feature.
     * @generated
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the value of '<em><b>remark</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>remark</b></em>' feature
     * @generated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets the '{@link ProjectLibTemplate#getRemark() <em>remark</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRemark
     *            the new value of the '{@link ProjectLibTemplate#getRemark() remark}' feature.
     * @generated
     */
    public void setRemark(String newRemark) {
        remark = newRemark;
    }

    /**
     * Returns the value of '<em><b>status</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>status</b></em>' feature
     * @generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the '{@link ProjectLibTemplate#getStatus() <em>status</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStatus
     *            the new value of the '{@link ProjectLibTemplate#getStatus() status}' feature.
     * @generated
     */
    public void setStatus(String newStatus) {
        status = newStatus;
    }

    public TSUserDto getCreator() {
        return creator;
    }

    public void setCreator(TSUserDto creator) {
        this.creator = creator;
    }

    /**
     * A toString method which prints the values of all EAttributes of this instance.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        return "ProjectLibTemplate " + " [avaliable: " + getAvaliable() + "]" + " [name: "
               + getName() + "]" + " [remark: " + getRemark() + "]" + " [status: " + getStatus()
               + "]";
    }
}

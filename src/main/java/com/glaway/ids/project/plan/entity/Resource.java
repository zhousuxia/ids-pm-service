package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.businessobject.entity.BusinessObject;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.Date;


/**
 * A representation of the model object '<em><b>Resource</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @author wangyangzan
 * @generated
 */
@DiscriminatorValue("")
@Entity(name = "CmResource")
@Table(name = "CM_RESOURCE")
public class Resource extends BusinessObject {

    /**
     * <!-- begin-user-doc -->资源编号
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String no = null;

    /**
     * <!-- begin-user-doc -->资源名称
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->父节点
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String parentId = null;

    /**
     * 父节点
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumns({@JoinColumn(name = "parentId", insertable = false, updatable = false)})
    @ForeignKey(name = "none")
    private ResourceFolder parent = null;

    /**
     * <!-- begin-user-doc -->可用时间类型
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useableTimeType = null;

    /**
     * <!-- begin-user-doc -->可用时间段
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String useableTimeSection = null;

    /**
     * <!-- begin-user-doc -->开始时间
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date startTime = null;

    /**
     * <!-- begin-user-doc -->结束时间
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Temporal(TemporalType.DATE)
    private Date endTime = null;

    /**
     * <!-- begin-user-doc -->关键资源
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String keyResource = null;

    /**
     * <!-- begin-user-doc -->状态
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String stopFlag = null;

    /**
     * <!-- begin-user-doc -->占用预警
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String occupationWarn = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Transient()
    @Basic()
    private String path = null;

    public ResourceFolder getParent() {
        return parent;
    }

    public void setParent(ResourceFolder parent) {
        this.parent = parent;
    }

    /**
     * Returns the value of '<em><b>no</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>no</b></em>' feature
     * @generated
     */
    public String getNo() {
        return no;
    }

    /**
     * Sets the '{@link Resource#getNo() <em>no</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newNo
     *            the new value of the '{@link Resource#getNo() no}' feature.
     * @generated
     */
    public void setNo(String newNo) {
        no = newNo;
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
     * Sets the '{@link Resource#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link Resource#getName() name}' feature.
     * @generated
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the value of '<em><b>parentId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>parentId</b></em>' feature
     * @generated
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the '{@link Resource#getParentId() <em>parentId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newParentId
     *            the new value of the '{@link Resource#getParentId() parentId}' feature.
     * @generated
     */
    public void setParentId(String newParentId) {
        parentId = newParentId;
    }

    /**
     * Returns the value of '<em><b>useableTimeType</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>useableTimeType</b></em>' feature
     * @generated
     */
    public String getUseableTimeType() {
        return useableTimeType;
    }

    /**
     * Sets the '{@link Resource#getUseableTimeType() <em>useableTimeType</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseableTimeType
     *            the new value of the '{@link Resource#getUseableTimeType() useableTimeType}'
     *            feature.
     * @generated
     */
    public void setUseableTimeType(String newUseableTimeType) {
        useableTimeType = newUseableTimeType;
    }

    /**
     * Returns the value of '<em><b>useableTimeSection</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>useableTimeSection</b></em>' feature
     * @generated
     */
    public String getUseableTimeSection() {
        return useableTimeSection;
    }

    /**
     * Sets the '{@link Resource#getUseableTimeSection() <em>useableTimeSection</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newUseableTimeSection
     *            the new value of the '{@link Resource#getUseableTimeSection() useableTimeSection}
     *            ' feature.
     * @generated
     */
    public void setUseableTimeSection(String newUseableTimeSection) {
        useableTimeSection = newUseableTimeSection;
    }

    /**
     * Returns the value of '<em><b>startTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>startTime</b></em>' feature
     * @generated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Sets the '{@link Resource#getStartTime() <em>startTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStartTime
     *            the new value of the '{@link Resource#getStartTime() startTime}' feature.
     * @generated
     */
    public void setStartTime(Date newStartTime) {
        startTime = newStartTime;
    }

    /**
     * Returns the value of '<em><b>endTime</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>endTime</b></em>' feature
     * @generated
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Sets the '{@link Resource#getEndTime() <em>endTime</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newEndTime
     *            the new value of the '{@link Resource#getEndTime() endTime}' feature.
     * @generated
     */
    public void setEndTime(Date newEndTime) {
        endTime = newEndTime;
    }

    /**
     * Returns the value of '<em><b>keyResource</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>keyResource</b></em>' feature
     * @generated
     */
    public String getKeyResource() {
        return keyResource;
    }

    /**
     * Sets the '{@link Resource#getKeyResource() <em>keyResource</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newKeyResource
     *            the new value of the '{@link Resource#getKeyResource() keyResource}' feature.
     * @generated
     */
    public void setKeyResource(String newKeyResource) {
        keyResource = newKeyResource;
    }

    /**
     * Returns the value of '<em><b>stopFlag</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>stopFlag</b></em>' feature
     * @generated
     */
    public String getStopFlag() {
        return stopFlag;
    }

    /**
     * Sets the '{@link Resource#getStopFlag() <em>stopFlag</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStopFlag
     *            the new value of the '{@link Resource#getStopFlag() stopFlag}' feature.
     * @generated
     */
    public void setStopFlag(String newStopFlag) {
        stopFlag = newStopFlag;
    }

    /**
     * Returns the value of '<em><b>occupationWarn</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>occupationWarn</b></em>' feature
     * @generated
     */
    public String getOccupationWarn() {
        return occupationWarn;
    }

    /**
     * Sets the '{@link Resource#getOccupationWarn() <em>occupationWarn</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOccupationWarn
     *            the new value of the '{@link Resource#getOccupationWarn() occupationWarn}'
     *            feature.
     * @generated
     */
    public void setOccupationWarn(String newOccupationWarn) {
        occupationWarn = newOccupationWarn;
    }

    /**
     * Returns the value of '<em><b>path</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>path</b></em>' feature
     * @generated
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the '{@link Resource#getPath() <em>path</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPath
     *            the new value of the '{@link Resource#getPath() path}' feature.
     * @generated
     */
    public void setPath(String newPath) {
        path = newPath;
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
        return "Resource " + " [no: " + getNo() + "]" + " [name: " + getName() + "]"
               + " [parentId: " + getParentId() + "]" + " [useableTimeType: "
               + getUseableTimeType() + "]" + " [useableTimeSection: " + getUseableTimeSection()
               + "]" + " [startTime: " + getStartTime() + "]" + " [endTime: " + getEndTime() + "]"
               + " [keyResource: " + getKeyResource() + "]" + " [stopFlag: " + getStopFlag() + "]"
               + " [occupationWarn: " + getOccupationWarn() + "]" + " [path: " + getPath() + "]";
    }
}

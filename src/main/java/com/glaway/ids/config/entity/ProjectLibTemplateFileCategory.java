package com.glaway.ids.config.entity;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.glaway.foundation.common.entity.GLObject;
import com.glaway.foundation.system.event.EventSource;


/**
 * A representation of the model object '<em><b>ProjectLibTemplateFileCategory</b></em>'.
 * <!-- begin-user-doc -->配置-项目库权限模板文件目录<!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ProjectLibTemplateFileCategory")
@Table(name = "CM_PROJLIB_TEMPLATE_FILE")
public class ProjectLibTemplateFileCategory extends GLObject implements EventSource {

    /**
     * <!-- begin-user-doc -->目录名称<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->父ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String parentId = null;

    /**
     * <!-- begin-user-doc -->排序<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private long orderNum = 0;

    /**
     * <!-- begin-user-doc -->所属模板ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String templateId = null;

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
     * Sets the '{@link ProjectLibTemplateFileCategory#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link ProjectLibTemplateFileCategory#getName() name}'
     *            feature.
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
     * Sets the '{@link ProjectLibTemplateFileCategory#getParentId() <em>parentId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newParentId
     *            the new value of the '{@link ProjectLibTemplateFileCategory#getParentId()
     *            parentId}' feature.
     * @generated
     */
    public void setParentId(String newParentId) {
        parentId = newParentId;
    }

    /**
     * Returns the value of '<em><b>orderNum</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>orderNum</b></em>' feature
     * @generated
     */
    public long getOrderNum() {
        return orderNum;
    }

    /**
     * Sets the '{@link ProjectLibTemplateFileCategory#getOrderNum() <em>orderNum</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newOrderNum
     *            the new value of the '{@link ProjectLibTemplateFileCategory#getOrderNum()
     *            orderNum}' feature.
     * @generated
     */
    public void setOrderNum(long newOrderNum) {
        orderNum = newOrderNum;
    }

    /**
     * Returns the value of '<em><b>templateId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>templateId</b></em>' feature
     * @generated
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * Sets the '{@link ProjectLibTemplateFileCategory#getTemplateId() <em>templateId</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTemplateId
     *            the new value of the '{@link ProjectLibTemplateFileCategory#getTemplateId()
     *            templateId}' feature.
     * @generated
     */
    public void setTemplateId(String newTemplateId) {
        templateId = newTemplateId;
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
        return "ProjectLibTemplateFileCategory " + " [name: " + getName() + "]" + " [parentId: "
               + getParentId() + "]" + " [orderNum: " + getOrderNum() + "]" + " [templateId: "
               + getTemplateId() + "]";
    }
}

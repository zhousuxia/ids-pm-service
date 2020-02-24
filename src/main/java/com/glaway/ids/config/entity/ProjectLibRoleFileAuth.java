package com.glaway.ids.config.entity;


import javax.persistence.*;

import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.system.lifecycle.entity.Permission;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.glaway.foundation.businessobject.entity.BusinessObject;


/**
 * A representation of the model object '<em><b>ProjectLibRoleFileAuth</b></em>'.
 * <!-- begin-user-doc -->配置-项目库权限模板文件目录权限<!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ProjectLibRoleFileAuth")
@Table(name = "CM_PROJLIB_ROLE_FILE_AUTH")
public class ProjectLibRoleFileAuth extends BusinessObject implements Permission {

    /**
     * <!-- begin-user-doc -->目录ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String fileId = null;

    /**
     * <!-- begin-user-doc -->角色ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String roleId = null;


    @Transient()
    private String roleName = null;
    
    /**
     * <!-- begin-user-doc -->角色<!-- end-user-doc -->
     */
    private TSRoleDto role;

    /**
     * <!-- begin-user-doc -->权限编码<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String permissionCode = null;

    /**
     * <!-- begin-user-doc -->所属模板ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String templateId = null;

    /**
     * Returns the value of '<em><b>fileId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>fileId</b></em>' feature
     * @generated
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * Sets the '{@link ProjectLibRoleFileAuth#getFileId() <em>fileId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newFileId
     *            the new value of the '{@link ProjectLibRoleFileAuth#getFileId() fileId}' feature.
     * @generated
     */
    public void setFileId(String newFileId) {
        fileId = newFileId;
    }

    /**
     * Returns the value of '<em><b>roleId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>roleId</b></em>' feature
     * @generated
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * Sets the '{@link ProjectLibRoleFileAuth#getRoleId() <em>roleId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newRoleId
     *            the new value of the '{@link ProjectLibRoleFileAuth#getRoleId() roleId}' feature.
     * @generated
     */
    public void setRoleId(String newRoleId) {
        roleId = newRoleId;
    }

    /**
     * Returns the value of '<em><b>permissionCode</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>permissionCode</b></em>' feature
     * @generated
     */
    public String getPermissionCode() {
        return permissionCode;
    }

    /**
     * Sets the '{@link ProjectLibRoleFileAuth#getPermissionCode() <em>permissionCode</em>}'
     * feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPermissionCode
     *            the new value of the '{@link ProjectLibRoleFileAuth#getPermissionCode()
     *            permissionCode}' feature.
     * @generated
     */
    public void setPermissionCode(String newPermissionCode) {
        permissionCode = newPermissionCode;
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
     * Sets the '{@link ProjectLibRoleFileAuth#getTemplateId() <em>templateId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTemplateId
     *            the new value of the '{@link ProjectLibRoleFileAuth#getTemplateId() templateId}'
     *            feature.
     * @generated
     */
    public void setTemplateId(String newTemplateId) {
        templateId = newTemplateId;
    }

    public TSRoleDto getRole() {
        return role;
    }

    public void setRole(TSRoleDto role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
        return "ProjectLibRoleFileAuth " + " [fileId: " + getFileId() + "]" + " [roleId: "
               + getRoleId() + "]" + " [permissionCode: " + getPermissionCode() + "]"
               + " [templateId: " + getTemplateId() + "]";
    }
}

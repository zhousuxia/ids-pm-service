package com.glaway.ids.config.entity;


import com.glaway.foundation.businessobject.entity.BusinessObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A representation of the model object '<em><b>BusinessConfig</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "BusinessConfig")
@Table(name = "CM_BUSINESS_CONFIG")
public class BusinessConfig extends BusinessObject {

    /**
     * <!-- begin-user-doc -->配置编号 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String no = "";

    /**
     * <!-- begin-user-doc -->配置名称 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc --> 配置类型<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String configType = null;

    /**
     * <!-- begin-user-doc -->配置信息 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    @Column(length=1024)
    private String configComment = null;

    /**
     * <!-- begin-user-doc -->状态
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String stopFlag = null;
    
    /**
     * <!-- begin-user-doc -->父节点ID <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String parentId = "";

    /**
     * <!-- begin-user-doc -->路径 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String path = null;
    
    
    @Basic()
    private String rankQuality=null;
    /**
     * <!-- begin-user-doc -->color <!-- end-user-doc -->
     * 
     * @generated
     */
    @Transient()
    private String result;
    
    /**
     * <!-- begin-user-doc -->子孙节点 <!-- end-user-doc -->
     * 
     * @generated
     */
    @Transient()
    private List<BusinessConfig> children = new ArrayList<BusinessConfig>();

    
    public String getRankQuality() {
		return rankQuality;
	}

	public void setRankQuality(String rankQuality) {
		this.rankQuality = rankQuality;
	}

	/**
     * Returns the value of '<em><b>no</b></em>' feature. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return the value of '<em><b>no</b></em>' feature
     * @generated
     */
    public String getNo() {
        return no;
    }

    /**
     * Sets the '{@link BusinessConfig#getNo() <em>no</em>}' feature.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param newNo
     *            the new value of the '{@link BusinessConfig#getNo() no}' feature.
     * @generated
     */
    public void setNo(String newNo) {
        no = newNo;
    }

    /**
     * Returns the value of '<em><b>name</b></em>' feature.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>name</b></em>' feature
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the '{@link BusinessConfig#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link BusinessConfig#getName() name}' feature.
     * @generated
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the value of '<em><b>configType</b></em>' feature.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>configType</b></em>' feature
     * @generated
     */
    public String getConfigType() {
        return configType;
    }

    /**
     * Sets the '{@link BusinessConfig#getConfigType() <em>configType</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newConfigType
     *            the new value of the '{@link BusinessConfig#getConfigType() configType}' feature.
     * @generated
     */
    public void setConfigType(String newConfigType) {
        configType = newConfigType;
    }

    /**
     * Returns the value of '<em><b>configComment</b></em>' feature.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return the value of '<em><b>configComment</b></em>' feature
     * @generated
     */
    public String getConfigComment() {
        return configComment;
    }

    /**
     * Sets the '{@link BusinessConfig#getConfigComment() <em>configComment</em>}' feature. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newConfigComment
     *            the new value of the '{@link BusinessConfig#getConfigComment() configComment}'
     *            feature.
     * @generated
     */
    public void setConfigComment(String newConfigComment) {
        configComment = newConfigComment;
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
     * Sets the '{@link BusinessConfig#getStopFlag() <em>stopFlag</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStopFlag
     *            the new value of the '{@link BusinessConfig#getStopFlag() stopFlag}' feature.
     * @generated
     */
    public void setStopFlag(String newStopFlag) {
        stopFlag = newStopFlag;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<BusinessConfig> getChildren() {
        return children;
    }

    public void setChildren(List<BusinessConfig> children) {
        this.children = children;
    }
    
    
    /**
     * Adds to the <em>children</em> feature.
     *
     * @param childrenValue
     *            the value to add
     * @return true if the value is added to the collection (it was not yet
     *         present in the collection), false otherwise
     * @generated
     */
    public boolean addToChildren(BusinessConfig childrenValue) {
        if (!children.contains(childrenValue)) {
            boolean result = children.add(childrenValue);
            return result;
        }
        return false;
    }

    /**
     * Removes from the <em>children</em> feature.
     *
     * @param childrenValue
     *            the value to remove
     * @return true if the value is removed from the collection (it existed in
     *         the collection before removing), false otherwise
     * @generated
     */
    public boolean removeFromChildren(BusinessConfig childrenValue) {
        if (children.contains(childrenValue)) {
            boolean result = children.remove(childrenValue);
            return result;
        }
        return false;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Clears the <em>children</em> feature.
     * 
     * @generated
     */
    public void clearChildren() {
        while (!children.isEmpty()) {
            removeFromChildren(children.iterator().next());
        }
    }
}

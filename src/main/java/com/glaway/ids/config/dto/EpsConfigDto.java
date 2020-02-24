package com.glaway.ids.config.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.glaway.foundation.businessobject.entity.BusinessObject;
import com.glaway.foundation.common.vdata.GLVData;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A representation of the model object '<em><b>EpsConfig</b></em>'. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class EpsConfigDto extends GLVData {

    /**
     * <!-- begin-user-doc --> 项目分类名称<!-- end-user-doc -->
     * 
     * @generated
     */
    private String name = null;

    /**
     * <!-- begin-user-doc -->编号 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String no = null;

    /**
     * <!-- begin-user-doc -->父节点ID <!-- end-user-doc -->
     * 
     * @generated
     */
    private String parentId = "";

    /**
     * <!-- begin-user-doc -->路径 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String path = null;

    /**
     * <!-- begin-user-doc -->状态 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String stopFlag = null;

    /**
     * <!-- begin-user-doc -->配置描述 <!-- end-user-doc -->
     * 
     * @generated
     */
    private String configComment = "";
    /**
     * 项目分类排序属性
     */
    private String rankQuality= null;

    /**
     * <!-- begin-user-doc -->子孙节点 <!-- end-user-doc -->
     * 
     * @generated
     */
    private List<EpsConfigDto> children = new ArrayList<EpsConfigDto>();
    
    

	public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /**
     * <!-- begin-user-doc -->color <!-- end-user-doc -->
     * 
     * @generated
     */
    @Transient()
    private String result;
    
    /**
     * Returns the value of '<em><b>name</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>name</b></em>' feature
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the '{@link EpsConfigDto#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link EpsConfigDto#getName() name}' feature.
     * @generated
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the value of '<em><b>no</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>no</b></em>' feature
     * @generated
     */
    public String getNo() {
        return no;
    }

    /**
     * Sets the '{@link EpsConfigDto#getNo() <em>no</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newNo
     *            the new value of the '{@link EpsConfigDto#getNo() no}' feature.
     * @generated
     */
    public void setNo(String newNo) {
        no = newNo;
    }

    /**
     * Returns the value of '<em><b>parentId</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>parentId</b></em>' feature
     * @generated
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the '{@link EpsConfigDto#getParentId() <em>parentId</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newParentId
     *            the new value of the '{@link EpsConfigDto#getParentId() parentId}' feature.
     * @generated
     */
    public void setParentId(String newParentId) {
        parentId = newParentId;
    }

    /**
     * Returns the value of '<em><b>path</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>path</b></em>' feature
     * @generated
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the '{@link EpsConfigDto#getPath() <em>path</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newPath
     *            the new value of the '{@link EpsConfigDto#getPath() path}' feature.
     * @generated
     */
    public void setPath(String newPath) {
        path = newPath;
    }

    /**
     * Returns the value of '<em><b>stopFlag</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>stopFlag</b></em>' feature
     * @generated
     */
    public String getStopFlag() {
        return stopFlag;
    }

    /**
     * Sets the '{@link EpsConfigDto#getStopFlag() <em>stopFlag</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newStopFlag
     *            the new value of the '{@link EpsConfigDto#getStopFlag() stopFlag}' feature.
     * @generated
     */
    public void setStopFlag(String newStopFlag) {
        stopFlag = newStopFlag;
    }

    /**
     * Returns the value of '<em><b>configComment</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>configComment</b></em>' feature
     * @generated
     */
    public String getConfigComment() {
        return configComment;
    }

    /**
     * Sets the '{@link EpsConfigDto#getConfigComment() <em>configComment</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newConfigComment
     *            the new value of the '{@link EpsConfigDto#getConfigComment() configComment}'
     *            feature.
     * @generated
     */
    public void setConfigComment(String newConfigComment) {
        configComment = newConfigComment;
    }

    /**
     * Returns the value of '<em><b>children</b></em>' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>children</b></em>' feature
     * @generated
     */
    public List<EpsConfigDto> getChildren() {
        return children;
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
    public boolean addToChildren(EpsConfigDto childrenValue) {
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
    public boolean removeFromChildren(EpsConfigDto childrenValue) {
        if (children.contains(childrenValue)) {
            boolean result = children.remove(childrenValue);
            return result;
        }
        return false;
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

    /**
     * Sets the '{@link EpsConfigDto#getChildren() <em>children</em>}' feature.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param newChildren
     *            the new value of the '{@link EpsConfigDto#getChildren() children}' feature.
     * @generated
     */
    public void setChildren(List<EpsConfigDto> newChildren) {
        children = newChildren;
    }

	

	public String getRankQuality() {
		return rankQuality;
	}

	public void setRankQuality(String rankQuality) {
		this.rankQuality = rankQuality;
	}

	@Override
	public String toString() {
		return "EpsConfig [name=" + name + ", no=" + no + ", parentId=" + parentId + ", path=" + path + ", stopFlag="
				+ stopFlag + ", configComment=" + configComment + ", rankQuality=" + rankQuality + ", children="
				+ children + ", result=" + result + "]";
	}

	

	
	
   
}

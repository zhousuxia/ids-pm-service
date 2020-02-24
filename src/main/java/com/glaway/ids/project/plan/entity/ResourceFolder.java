package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.businessobject.entity.BusinessObject;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;


/**
 * A representation of the model object '<em><b>ResourceFolder</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @author wangyangzan
 * @generated
 */
@Entity(name = "ResourceFolder")
@Table(name = "CM_RESOURCE_FOLDER")
public class ResourceFolder extends BusinessObject {

    /**
     * <!-- begin-user-doc -->资源目录名称
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->父目录ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String parentId = null;

    /**
     * <!-- begin-user-doc -->路径
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String path = null;

    /**
     * <!-- begin-user-doc -->节点顺序
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String nodeOrder = null;
    
    /**
     * <!-- begin-user-doc -->排序
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private long orderNum = 0;

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 子孙节点
     */
    @Transient()
    private List<ResourceFolder> children = new ArrayList<ResourceFolder>();

    /**
     * Returns the value of '<em><b>children</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>children</b></em>' feature
     */
    public List<ResourceFolder> getChildren() {
        return children;
    }

    /**
     * Adds to the <em>children</em> feature.
     *
     * @param childrenValue
     *            the value to add
     * @return true if the value is added to the collection (it was not yet present in the
     *         collection), false otherwise
     */
    public boolean addToChildren(ResourceFolder childrenValue) {
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
     * @return true if the value is removed from the collection (it existed in the collection
     *         before removing), false otherwise
     */
    public boolean removeFromChildren(ResourceFolder childrenValue) {
        if (children.contains(childrenValue)) {
            boolean result = children.remove(childrenValue);
            return result;
        }
        return false;
    }

    /**
     * Clears the <em>children</em> feature.
     */
    public void clearChildren() {
        while (!children.isEmpty()) {
            removeFromChildren(children.iterator().next());
        }
    }

    /**
     * Sets the '{@link ResourceFolder#getChildren() <em>children</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newChildren
     *            the new value of the '{@link ResourceFolder#getChildren() children}' feature.
     */
    public void setChildren(List<ResourceFolder> newChildren) {
        children = newChildren;
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
     * Sets the '{@link ResourceFolder#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link ResourceFolder#getName() name}' feature.
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
     * Sets the '{@link ResourceFolder#getParentId() <em>parentId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newParentId
     *            the new value of the '{@link ResourceFolder#getParentId() parentId}' feature.
     * @generated
     */
    public void setParentId(String newParentId) {
        parentId = newParentId;
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
     * Sets the '{@link ResourceFolder#getPath() <em>path</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newPath
     *            the new value of the '{@link ResourceFolder#getPath() path}' feature.
     * @generated
     */
    public void setPath(String newPath) {
        path = newPath;
    }

    /**
     * Returns the value of '<em><b>nodeOrder</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>nodeOrder</b></em>' feature
     * @generated
     */
    public String getNodeOrder() {
        return nodeOrder;
    }

    /**
     * Sets the '{@link ResourceFolder#getNodeOrder() <em>nodeOrder</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newNodeOrder
     *            the new value of the '{@link ResourceFolder#getNodeOrder() nodeOrder}' feature.
     * @generated
     */
    public void setNodeOrder(String newNodeOrder) {
        nodeOrder = newNodeOrder;
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
        return "ResourceFolder " + " [name: " + getName() + "]" + " [parentId: " + getParentId()
               + "]" + " [path: " + getPath() + "]" + " [nodeOrder: " + getNodeOrder() + "]";
    }
}

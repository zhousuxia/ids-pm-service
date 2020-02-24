package com.glaway.ids.project.plan.entity;


import com.glaway.foundation.common.entity.GLObject;
import com.glaway.foundation.system.event.EventSource;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;


/**
 * A representation of the model object '<em><b>NameStandard</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @author wangyangzan
 * @generated
 */
@Entity(name = "NameStandard")
@Table(name = "CM_NAME_STANDARD")
public class NameStandard extends GLObject implements EventSource {

    /**
     * <!-- begin-user-doc -->编号
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String no = null;

    /**
     * <!-- begin-user-doc -->活动名称库
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String name = null;

    /**
     * <!-- begin-user-doc -->活动分类
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String activeCategory = null;

    /**
     * <!-- begin-user-doc -->状态
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String stopFlag = null;

    /**
     * <!-- begin-user-doc -->有效性
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String avaliable = "1";

    /**
     * 交付项
     */
    @Transient()
    private List<DeliveryStandard> deliverableList = new ArrayList<DeliveryStandard>();

    public List<DeliveryStandard> getDeliverableList() {
        return deliverableList;
    }

    public void setDeliverableList(List<DeliveryStandard> deliverableList) {
        this.deliverableList = deliverableList;
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
     * Sets the '{@link NameStandard#getNo() <em>no</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newNo
     *            the new value of the '{@link NameStandard#getNo() no}' feature.
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
     * Sets the '{@link NameStandard#getName() <em>name</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newName
     *            the new value of the '{@link NameStandard#getName() name}' feature.
     * @generated
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the value of '<em><b>activeCategory</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>activeCategory</b></em>' feature
     * @generated
     */
    public String getActiveCategory() {
        return activeCategory;
    }

    /**
     * Sets the '{@link NameStandard#getActiveCategory() <em>activeCategory</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newActiveCategory
     *            the new value of the '{@link NameStandard#getActiveCategory() activeCategory}'
     *            feature.
     * @generated
     */
    public void setActiveCategory(String newActiveCategory) {
        activeCategory = newActiveCategory;
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
     * Sets the '{@link NameStandard#getStopFlag() <em>stopFlag</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newStopFlag
     *            the new value of the '{@link NameStandard#getStopFlag() stopFlag}' feature.
     * @generated
     */
    public void setStopFlag(String newStopFlag) {
        stopFlag = newStopFlag;
    }

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
     * Sets the '{@link NameStandard#getAvaliable() <em>avaliable</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newAvaliable
     *            the new value of the '{@link NameStandard#getAvaliable() avaliable}' feature.
     * @generated
     */
    public void setAvaliable(String newAvaliable) {
        avaliable = newAvaliable;
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
        return "NameStandard " + " [no: " + getNo() + "]" + " [name: " + getName() + "]"
               + " [activeCategory: " + getActiveCategory() + "]" + " [stopFlag: " + getStopFlag()
               + "]" + " [avaliable: " + getAvaliable() + "]";
    }
}

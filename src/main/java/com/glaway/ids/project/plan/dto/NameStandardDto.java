package com.glaway.ids.project.plan.dto;


import java.util.ArrayList;
import java.util.List;

import com.glaway.foundation.common.vdata.GLVData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * A representation of the model object '<em><b>NameStandard</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @author wangyangzan
 * @generated
 */
@ApiModel(value="活动名称信息模型")
public class NameStandardDto extends GLVData {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @ApiModelProperty(value="编号")
    private String no = null;

    /**
     * <!-- begin-user-doc -->活动名称库
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @ApiModelProperty(value="名称")
    private String name = null;

    /**
     * <!-- begin-user-doc -->活动分类
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @ApiModelProperty(value="活动分类")
    private String activeCategory = null;

    /**
     * <!-- begin-user-doc -->状态
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @ApiModelProperty(value="状态")
    private String stopFlag = null;

    /**
     * <!-- begin-user-doc -->有效性
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @ApiModelProperty(value="有效性")
    private String avaliable = "1";

    /**
     * 交付项
     */
    @ApiModelProperty(value="关联交付项信息")
    private List<DeliveryStandardDto> deliverableList = new ArrayList<DeliveryStandardDto>();

    public List<DeliveryStandardDto> getDeliverableList() {
        return deliverableList;
    }

    public void setDeliverableList(List<DeliveryStandardDto> deliverableList) {
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newNo
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newName
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newActiveCategory
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newStopFlag
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param newAvaliable
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

package com.glaway.ids.project.projectmanager.entity;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.glaway.foundation.businessobject.entity.BusinessObject;


/**
 * 项目文档关系对象
 * @author wyz
 * @version 2018年8月6日
 * @see ProjDocRelation
 * @since
 */
@Entity(name = "ProjDocRelation")
@Table(name = "PM_PROJ_DOC_RELATION")
public class ProjDocRelation extends BusinessObject {

    /**
     * <!-- begin-user-doc -->引用该文档的ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String quoteId = null;

    /**
     * 文档实例类
     */

    private RepFileDto repFile;

    /**
     * <!-- begin-user-doc -->文档ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String docId = null;

    /**
     * Returns the value of '<em><b>quoteId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>quoteId</b></em>' feature
     * @generated
     */
    public String getQuoteId() {
        return quoteId;
    }

    /**
     * Sets the '{@link ProjDocRelation#getQuoteId() <em>quoteId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newQuoteId
     *            the new value of the '{@link ProjDocRelation#getQuoteId() quoteId}' feature.
     * @generated
     */
    public void setQuoteId(String newQuoteId) {
        quoteId = newQuoteId;
    }

    /**
     * Returns the value of '<em><b>docId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>docId</b></em>' feature
     * @generated
     */
    public String getDocId() {
        return docId;
    }

    /**
     * Sets the '{@link ProjDocRelation#getDocId() <em>docId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newDocId
     *            the new value of the '{@link ProjDocRelation#getDocId() docId}' feature.
     * @generated
     */
    public void setDocId(String newDocId) {
        docId = newDocId;
    }

    public RepFileDto getRepFile() {
        return repFile;
    }

    public void setRepFile(RepFileDto repFile) {
        this.repFile = repFile;
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
        return "ProjDocRelation " + " [quoteId: " + getQuoteId() + "]" + " [docId: " + getDocId()
               + "]";
    }
}

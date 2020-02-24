package com.glaway.ids.project.projecttemplate.entity;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.glaway.foundation.common.entity.GLObject;
import com.glaway.foundation.system.event.EventSource;


/**
 * A representation of the model object '<em><b>ProjectLibAuthTemplateLink</b></em>'.
 * <!-- begin-user-doc -->项目库权限模板与项目关联表<!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ProjTmpLibAuthLibTmpLink")
@Table(name = "PM_PROJ_TMPL_LIBTMP_LINK")
public class ProjTmpLibAuthLibTmpLink extends GLObject implements EventSource {

    /**
     * <!-- begin-user-doc -->项目模板ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projTmpId = null;

    /**
     * <!-- begin-user-doc -->项目库模板ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String libTmpId = null;

    
    public String getProjTmpId() {
        return projTmpId;
    }


    public void setProjTmpId(String projTmpId) {
        this.projTmpId = projTmpId;
    }


    public String getLibTmpId() {
        return libTmpId;
    }


    public void setLibTmpId(String libTmpId) {
        this.libTmpId = libTmpId;
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
        return "ProjTmpLibAuthLibTmpLink " + " [projTmpId: " + getProjTmpId() + "]"
               + " [libTmpId: " + getLibTmpId() + "]";
    }
}

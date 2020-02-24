package com.glaway.ids.project.projectmanager.entity;


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
@Entity(name = "ProjectLibAuthTemplateLink")
@Table(name = "PM_PROJECTLIB_TEMPLATE_LINK")
public class ProjectLibAuthTemplateLink extends GLObject implements EventSource {

    /**
     * <!-- begin-user-doc -->项目ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectId = null;

    /**
     * <!-- begin-user-doc -->模板ID<!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String templateId = null;

    /**
     * Returns the value of '<em><b>projectId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projectId</b></em>' feature
     * @generated
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Sets the '{@link ProjectLibAuthTemplateLink#getProjectId() <em>projectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjectId
     *            the new value of the '{@link ProjectLibAuthTemplateLink#getProjectId() projectId}
     *            ' feature.
     * @generated
     */
    public void setProjectId(String newProjectId) {
        projectId = newProjectId;
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
     * Sets the '{@link ProjectLibAuthTemplateLink#getTemplateId() <em>templateId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTemplateId
     *            the new value of the '{@link ProjectLibAuthTemplateLink#getTemplateId()
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
        return "ProjectLibAuthTemplateLink " + " [projectId: " + getProjectId() + "]"
               + " [templateId: " + getTemplateId() + "]";
    }
}

package com.glaway.ids.project.projecttemplate.entity;


import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.glaway.foundation.common.dto.FdTeamDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepLibraryDto;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import com.glaway.foundation.businessobject.entity.BusinessObject;


/**
 * A representation of the model object '<em><b>ProjTmplTeamLink</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "projecttemplate_ProjTmplTeamLink")
@Table(name = "PM_PROJ_TMPL_TEAM_LINK")
public class ProjTmplTeamLink extends BusinessObject {

    /**
     * <!-- begin-user-doc -->知识库ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String libId = null;

    private RepLibraryDto repLibrary;

    /**
     * <!-- begin-user-doc -->项目模板ＩＤ
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projTemplateId = null;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "projTemplateId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private ProjTemplate projTemplate;

    private FdTeamDto team;

    /**
     * <!-- begin-user-doc -->团队ＩＤ
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String teamId = null;

    /**
     * Returns the value of '<em><b>libId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>libId</b></em>' feature
     * @generated
     */
    public String getLibId() {
        return libId;
    }

    /**
     * Sets the '{@link ProjTmplTeamLink#getLibId() <em>libId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLibId
     *            the new value of the '{@link ProjTmplTeamLink#getLibId() libId}' feature.
     * @generated
     */
    public void setLibId(String newLibId) {
        libId = newLibId;
    }

    /**
     * Returns the value of '<em><b>projTemplateId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>projTemplateId</b></em>' feature
     * @generated
     */
    public String getProjTemplateId() {
        return projTemplateId;
    }

    /**
     * Sets the '{@link ProjTmplTeamLink#getProjTemplateId() <em>projTemplateId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjTemplateId
     *            the new value of the '{@link ProjTmplTeamLink#getProjTemplateId() projTemplateId}
     *            ' feature.
     * @generated
     */
    public void setProjTemplateId(String newProjTemplateId) {
        projTemplateId = newProjTemplateId;
    }

    /**
     * Returns the value of '<em><b>teamId</b></em>' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return the value of '<em><b>teamId</b></em>' feature
     * @generated
     */
    public String getTeamId() {
        return teamId;
    }

    /**
     * Sets the '{@link ProjTmplTeamLink#getTeamId() <em>teamId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTeamId
     *            the new value of the '{@link ProjTmplTeamLink#getTeamId() teamId}' feature.
     * @generated
     */
    public void setTeamId(String newTeamId) {
        teamId = newTeamId;
    }

    public ProjTemplate getProjTemplate() {
        return projTemplate;
    }

    public void setProjTemplate(ProjTemplate projTemplate) {
        this.projTemplate = projTemplate;
    }

    public FdTeamDto getTeam() {
        return team;
    }

    public void setTeam(FdTeamDto team) {
        this.team = team;
    }

    public RepLibraryDto getRepLibrary() {
        return repLibrary;
    }

    public void setRepLibrary(RepLibraryDto repLibrary) {
        this.repLibrary = repLibrary;
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
        return "ProjTmplTeamLink " + " [libId: " + getLibId() + "]" + " [projTemplateId: "
               + getProjTemplateId() + "]" + " [teamId: " + getTeamId() + "]";
    }

}

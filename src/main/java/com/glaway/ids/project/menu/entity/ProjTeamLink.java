package com.glaway.ids.project.menu.entity;


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
 * A representation of the model object '<em><b>ProjTeamLink</b></em>'.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
@Entity(name = "ProjTeamLink")
@Table(name = "PM_PROJ_TEAM_LINK")
public class ProjTeamLink extends BusinessObject {

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
     * <!-- begin-user-doc -->项目ID
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Basic()
    private String projectId = null;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Project project;


    private FdTeamDto team;

    /**
     * <!-- begin-user-doc -->团队ID
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
     * Sets the '{@link ProjTeamLink#getLibId() <em>libId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newLibId
     *            the new value of the '{@link ProjTeamLink#getLibId() libId}' feature.
     * @generated
     */
    public void setLibId(String newLibId) {
        libId = newLibId;
    }

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
     * Sets the '{@link ProjTeamLink#getProjectId() <em>projectId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newProjectId
     *            the new value of the '{@link ProjTeamLink#getProjectId() projectId}' feature.
     * @generated
     */
    public void setProjectId(String newProjectId) {
        projectId = newProjectId;
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
     * Sets the '{@link ProjTeamLink#getTeamId() <em>teamId</em>}' feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param newTeamId
     *            the new value of the '{@link ProjTeamLink#getTeamId() teamId}' feature.
     * @generated
     */
    public void setTeamId(String newTeamId) {
        teamId = newTeamId;
    }

    public RepLibraryDto getRepLibrary() {
        return repLibrary;
    }

    public void setRepLibrary(RepLibraryDto repLibrary) {
        this.repLibrary = repLibrary;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public FdTeamDto getTeam() {
        return team;
    }

    public void setTeam(FdTeamDto team) {
        this.team = team;
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
        return "ProjTeamLink " + " [libId: " + getLibId() + "]" + " [projectId: " + getProjectId()
               + "]" + " [teamId: " + getTeamId() + "]";
    }
}

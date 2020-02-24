package com.glaway.ids.project.menu.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.glaway.foundation.common.entity.GLObject;


/**
 * 项目最近N条操作历史
 * 
 * @author wangshen
 */
@SuppressWarnings("serial")
@Entity(name = "RecentlyProject")
@Table(name = "PM_RECENTLY_PROJECT")
public class RecentlyProject extends GLObject {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private Project project = null;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}

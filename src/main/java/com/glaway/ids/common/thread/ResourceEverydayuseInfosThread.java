/*
 * 文件名：OmsThread.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：cbchong
 * 修改时间：2015年6月8日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.common.thread;

import com.glaway.ids.common.constant.CommonConfigConstants;
import com.glaway.ids.common.service.ResourceEverydayuseServiceI;
import com.glaway.ids.project.plan.entity.ResourceLinkInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 〈资源每日使用信息更新进程〉
 * 
 * @author blcao
 * @version 2016年4月18日
 * @see ResourceEverydayuseInfosThread
 * @since
 */
public class ResourceEverydayuseInfosThread implements Runnable {

	/**
	 * 处理对象类型
	 */
	private String type;

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 操作类型
	 */
	private String operationType;

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	/**
	 * 操作时间
	 */
	private Date operationTime;

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	/**
	 * 项目ID
	 */
	private String projectId;

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 计划ID
	 */
	private String planId;

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	/**
	 * 资源使用信息
	 */
	private List<ResourceLinkInfo> linkInfoList = new ArrayList<ResourceLinkInfo>();

	public void setLinkInfoList(List<ResourceLinkInfo> linkInfoList) {
		this.linkInfoList = linkInfoList;
	}

	/**
	 * 资源每日使用信息处理
	 */
	private ResourceEverydayuseServiceI resourceEverydayuseService;

	public ResourceEverydayuseServiceI getResourceEverydayuseService() {
		return resourceEverydayuseService;
	}

	public void setResourceEverydayuseService(
			ResourceEverydayuseServiceI resourceEverydayuseService) {
		this.resourceEverydayuseService = resourceEverydayuseService;
	}

	@Override
	public void run() {
		if (CommonConfigConstants.OBJECT_TYPE_PROJECT.equals(this.type)) {
			this.resourceEverydayuseService
					.updateResourceEverydayuseInfosByProject(
							this.operationType, this.projectId,
							this.operationTime);
		} else if (CommonConfigConstants.OBJECT_TYPE_PLAN.equals(this.type)) {
			this.resourceEverydayuseService
					.updateResourceEverydayuseInfosByPlan(this.operationType,
							this.planId, this.operationTime);
		} else {
			this.resourceEverydayuseService.saveResourceEverydayuseInfos(
					this.operationType, this.linkInfoList, this.projectId);
		}
	}

}

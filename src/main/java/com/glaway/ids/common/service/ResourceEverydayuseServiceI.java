package com.glaway.ids.common.service;


import com.glaway.ids.project.plan.entity.ResourceEverydayuseInfo;
import com.glaway.ids.project.plan.entity.ResourceLinkInfo;

import java.util.Date;
import java.util.List;

/**
 * 资源每日使用信息处理
 * 
 * @author blcao
 * @version 2016年04月18日
 * @see ResourceEverydayuseServiceI
 * @since
 */
public interface ResourceEverydayuseServiceI {

	/**
	 * 将资源使用信息插入到每日使用表信息
	 *
	 * @param operationType 操作类型
	 *            :INSERT、UPDATE、DELETE
	 * @param resourceLinkList 资源列表
	 * @param projectId 项目id
	 */
	void saveResourceEverydayuseInfos(String operationType,
									  List<ResourceLinkInfo> resourceLinkList, String projectId);

	/**
	 * 更新资源每日使用信息
	 * @param oldUseObjectId  对象id
	 * @param newUseObjectId 对象id
	 * @param projectId 项目id
	 */
	 void updateResourceEverydayuseInfos(String oldUseObjectId,
                                         String newUseObjectId, String projectId);

	/**
	 * 项目暂停、关闭时，资源每日使用信息更新
	 *
	 * @param operationType 操作类型
	 *            :CLOSE、PAUSE
	 * @param projectId 项目id
	 * @param operationTime 操作时间
	 */
	void updateResourceEverydayuseInfosByProject(String operationType,
                                                 String projectId, Date operationTime);

	/**
	 * 计划删除、废弃、提前完工时，资源每日使用信息更新
	 * @param planId 计划id
	 * @param operationType 操作类型
	 *            :DELETE、DISCARD、BEFOREHAND
	 * @param operationTime 操作时间
	 */
	void updateResourceEverydayuseInfosByPlan(String operationType,
                                              String planId, Date operationTime);

	/**
	 * 删除资源时删除资源每日使用信息
	 * @param info 资源对象
	 */
	void deleteResourceEverydayuseInfos(ResourceEverydayuseInfo info);

	/**
	 * 项目暂停、关闭时，释放多余资源
	 * @param projectId 项目id
	 * @param operationTime 操作时间
	 */
	void delResourceUseByProjectAndOperationTime(String projectId,
                                                 Date operationTime);

	/**
	 * 删除计划时，删除计划相关的资源
	 * @param planId 计划id
	 */
	void delResourceUseByPlan(String planId);

	/**
	 * 计划废弃、提前完工时，释放多余资源
	 * @param planId 计划id
	 * @param operationTime 操作时间
	 */
	void delResourceUseByPlanAndOperationTime(String planId, Date operationTime);
}

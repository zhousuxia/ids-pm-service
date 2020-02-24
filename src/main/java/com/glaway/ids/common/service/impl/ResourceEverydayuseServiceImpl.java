package com.glaway.ids.common.service.impl;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.TimeUtil;
import com.glaway.ids.common.constant.CommonConfigConstants;
import com.glaway.ids.common.service.ResourceEverydayuseServiceI;
import com.glaway.ids.project.plan.entity.ResourceEverydayuseInfo;
import com.glaway.ids.project.plan.entity.ResourceLinkInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 资源每日使用信息处理
 * 
 * @author blcao
 * @version 2016年04月18日
 * @see ResourceEverydayuseServiceImpl
 * @since
 */
@Service("resourceEverydayuseService")
@Transactional
public class ResourceEverydayuseServiceImpl extends CommonServiceImpl implements
		ResourceEverydayuseServiceI {
	@Autowired
	private SessionFacade sessionFacade;

	@Override
	public void saveResourceEverydayuseInfos(String operationType,
											 List<ResourceLinkInfo> resourceLinkList, String projectId) {
		if (CommonConfigConstants.RESOURCEEVERYDAYUSE_OPERATIONTYPE_DELETE
				.equals(operationType)) {
			for (ResourceLinkInfo linkInfo : resourceLinkList) {
				ResourceEverydayuseInfo condition = new ResourceEverydayuseInfo();
				condition.setResourceId(linkInfo.getResourceId());
				condition.setUseObjectId(linkInfo.getUseObjectId());
				condition.setUseObjectType(linkInfo.getUseObjectType());
				deleteResourceEverydayuseInfos(condition);
			}

		} else {
			// 先删除、在插入
			List<ResourceEverydayuseInfo> list = new ArrayList<ResourceEverydayuseInfo>();
			for (ResourceLinkInfo linkInfo : resourceLinkList) {
				ResourceEverydayuseInfo condition = new ResourceEverydayuseInfo();
				condition.setResourceId(linkInfo.getResourceId());
				condition.setUseObjectId(linkInfo.getUseObjectId());
				condition.setUseObjectType(linkInfo.getUseObjectType());
				deleteResourceEverydayuseInfos(condition);
				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTime(linkInfo.getStartTime());
				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(linkInfo.getEndTime());
				// 获取资源使用天数
				int usedays = TimeUtil.getDaysBetween(startCalendar,
						endCalendar) + 1;
				for (int i = 0; i < usedays; i++) {
					ResourceEverydayuseInfo info = new ResourceEverydayuseInfo();
					info.setResourceId(linkInfo.getResourceId());
					info.setProjectId(projectId);
					info.setUseObjectId(linkInfo.getUseObjectId());
					info.setUseObjectType(linkInfo.getUseObjectType());
					info.setUseDate(TimeUtil.getExtraDate(
							linkInfo.getStartTime(), i));
					info.setUseRate(linkInfo.getUseRate());
					list.add(info);
				}
			}
			if(!CommonUtil.isEmpty(list)){
				batchSave(list);
			}
		}
	}

	@Override
	public void updateResourceEverydayuseInfosByProject(String operationType,
														String projectId, Date operationTime) {
		delResourceUseByProjectAndOperationTime(projectId, operationTime);
	}

	/**
	 * 项目暂停、关闭时，释放多余资源
	 *
	 * @param operationTime
	 */
	@Override
	public void delResourceUseByProjectAndOperationTime(String projectId,
														Date operationTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time1 = sdf.format(operationTime);
		sessionFacade
				.executeHql("delete ResourceEverydayuseInfo r where r.projectId = '"
						+ projectId
						+ "' and r.useObjectType = 'PLAN' and to_date( to_char(r.useDate,'yyyy-mm-dd') , 'yyyy-mm-dd') > to_date('"
						+ time1 + "', 'yyyy-mm-dd')");
	}

	/**
	 * 更新资源每日使用信息
	 */
	@Override
	public void updateResourceEverydayuseInfos(String oldUseObjectId,
			String newUseObjectId, String projectId) {
		sessionFacade
				.executeHql("update ResourceEverydayuseInfo r set r.useObjectId = '"
						+ newUseObjectId
						+ "', r.projectId = '"
						+ projectId
						+ "' where r.useObjectId = '" + oldUseObjectId + "'");
	}
	@Override
	public void updateResourceEverydayuseInfosByPlan(String operationType,
													 String planId, Date operationTime) {
		if (CommonConfigConstants.RESOURCEEVERYDAYUSE_PLAN_DELETE
				.equals(operationType)) {
			delResourceUseByPlan(planId);
		} else {
			delResourceUseByPlanAndOperationTime(planId, operationTime);
		}
	}

	/**
	 * 删除资源时删除资源每日使用信息
	 * @param info
	 */
	@Override
	public void deleteResourceEverydayuseInfos(ResourceEverydayuseInfo info) {
		sessionFacade
				.executeHql("delete ResourceEverydayuseInfo r where r.resourceId = '"
						+ info.getResourceId()
						+ "' and r.useObjectId = '"
						+ info.getUseObjectId()
						+ "' and r.useObjectType = '"
						+ info.getUseObjectType() + "'");
	}

	/**
	 * 删除计划时，删除计划相关的资源
	 *
	 * @param planId
	 */
	@Override
	public void delResourceUseByPlan(String planId) {
		sessionFacade
				.executeHql("delete ResourceEverydayuseInfo r where r.useObjectId = '"
						+ planId + "'");
	}

	/**
	 * 计划废弃、提前完工时，释放多余资源
	 *
	 * @param planId
	 * @param operationTime
	 */
	@Override
	public void delResourceUseByPlanAndOperationTime(String planId, Date operationTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time1 = sdf.format(operationTime);

		String sql = "delete ResourceEverydayuseInfo r where r.useObjectId = '"
				+ planId
				+ "' and r.useObjectType = 'PLAN' and to_date( to_char(r.useDate,'yyyy-mm-dd') , 'yyyy-mm-dd') > to_date('"
				+ time1 + "', 'yyyy-mm-dd')";
		sessionFacade.executeHql(sql);
	}
}

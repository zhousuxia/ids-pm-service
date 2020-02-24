/*
 * 文件名：MessageConstants.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：blcao
 * 修改时间：2016年6月15日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.message.constant;

/**
 * 描述：消息相关的属性常量
 * 
 * @author blcao
 * @version 2016年6月15日
 * @see MessageConstants
 * @since
 */

public class MessageConstants {
    /*************************************************************/
    /** 消息业务类型 **/
    /*************************************************************/
    /** 项目状态更新 */
    public static final String MESSAGE_STATUS_CHANGE = "状态更新";
	
    public static final String MESSAGE_PROJECT_STATUS_CHANGE = "项目状态更新";
    
    public static final String MESSAGE_PRODUCT_STATUS_CHANGE = "产品状态更新";

    /** 计划变更 */
    public static final String MESSAGE_PLAN_CHANGE = "计划变更";

    /** 计划完工 */
    public static final String MESSAGE_PLAN_COMPLETE = "计划完工";

    /** 计划废弃 */
    public static final String MESSAGE_PLAN_INVALID = "计划废弃";
    
    /** 计划逾期预警 */
    public static final String MESSAGE_PLAN_WARNANDOVER = "计划逾期预警";

    /*************************************************************/
    /** 消息接受者 **/
    /*************************************************************/
    /** 项目成员 */
    public static final String MESSAGE_RECEIVER_PROJECT_MEMBER = "项目成员";
    
    /** 项目成员 */
    public static final String MESSAGE_RECEIVER_PRODUCT_MEMBER = "产品团队成员";

    /** 计划负责人 */
    public static final String MESSAGE_RECEIVER_PLAN_OWNER = "计划负责人";

    /** 父计划负责人 */
    public static final String MESSAGE_RECEIVER_PARENT_OWNER = "父计划负责人";

    /** 子计划负责人 */
    public static final String MESSAGE_RECEIVER_SON_OWNER = "子计划负责人";

    /** 后置计划负责人 */
    public static final String MESSAGE_RECEIVER_BACK_OWNER = "后置计划负责人";
}

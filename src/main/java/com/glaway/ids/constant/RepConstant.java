/*
 * 文件名：RepConstant.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：lzhang
 * 修改时间：2015年4月8日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.constant;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * 
 * @author lzhang
 * @version 2015年4月8日
 * @see RepConstant
 * @since
 */

public interface RepConstant {

    /**
     * 启用状态
     */
    String LIB_STATUS_WORKING = "启用";

    /**
     * 拟制中状态
     */
    String LIB_STATUS_DRAFTING = "拟制中";

    /**
     * 禁用状态
     */
    String LIB_STATUS_DELETEING = "禁用";

    /**
     * 库管理员
     */
    String LID_ADMIN = "libAdmin";

    /**
     * 库管理员
     */
    String LID_ADMIN_NAME = "库管理员";

    /**
     * 库管理员
     */
    String LID_MEMBER = "libMember";

    /**
     * 库管理员
     */
    String LID_MEMBER_NAME = "成员";

    /**
     * 操作状态:正常
     */
    String OPERSTATUS_NORMAL = "正常";

    /**
     * 操作状态:检出
     */
    String OPERSTATUS_CHECKOUT = "已检出";

    /**
     * 操作状态:检入
     */
    String OPERSTATUS_CHECKIN = "已检入";

    /**
     * 操作状态:修订中
     */
    String OPERSTATUS_REVISE = "修订中";

}

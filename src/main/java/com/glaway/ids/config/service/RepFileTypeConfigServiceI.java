

package com.glaway.ids.config.service;

/*
 * 文件名：RepFileTypeConfigServiceI.java
 * 版权：Copyright by www.glaway.com
 * 描述：文档类型设置接口类
 * 修改人：zhousuxia
 * 修改时间：2018年7月26日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */


import com.glaway.foundation.businessobject.attribute.dto.EntityAttributeAdditionalAttributeDto;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileTypeDto;
import com.glaway.foundation.system.serial.entity.SerialNumberGeneratorInfo;
import com.glaway.ids.common.pbmn.activity.entity.BpmnTask;
import com.glaway.ids.config.entity.RepFileTypeConfigDescLink;
import com.glaway.ids.config.vo.RepFileTypeConfigVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;

public interface RepFileTypeConfigServiceI extends CommonService {


    /**
     * 获取文档类型设置数据列表
     *
     * @author zhousuxia
     * @version 2018年7月26日
     * @see RepFileTypeConfigServiceI
     * @since
     */
    List<RepFileTypeDto> getRepFileTypeConfigList(String parentId, String fileTypeCode, String fileTypeName, String entrance, String docTypeId);

    /**
     * 判断编号是否重复
     *
     * @author zhousuxia
     * @version 2018年7月26日
     * @see RepFileTypeConfigServiceI
     * @since
     */
    boolean checkFileTypeCodeBeforeSave(String parentId,String repFileTypeId,String fileTypeCode);

    /**
     * 保存文档类型配置
     * @param repFileTypeId 文档类型配置id
     * @param fileTypeCode 文档类型配置编号
     * @param fileTypeName 文档类型配置名称
     * @param generatorInfoId 编号规则id
     * @param description 描述
     * @return
     */
    FeignJson saveRepFileTypeConfig(String repFileTypeId, String fileTypeCode, String fileTypeName, String generatorInfoId, String description,String userId);

    /**
     * 保存文档类型设置描述
     *
     * @author zhousuxia
     * @version 2018年7月26日
     * @see RepFileTypeConfigServiceI
     * @since
     */
    void saveRepFileTypeConfigDescLink(RepFileTypeConfigDescLink link);

    /**
     * 根据文档类型id查找文档类型关联的描述信息
     *
     * @author zhousuxia
     * @version 2018年7月26日
     * @see RepFileTypeConfigServiceI
     * @since
     */
    List<RepFileTypeConfigDescLink> queryDescLinkByRepFileTypeId(String repFileTypeId);

    /**
     * 删除文档类型设置
     *
     * @author zhousuxia
     * @version 2018年7月27日
     * @see RepFileTypeConfigServiceI
     * @since
     */
    void deleteRepFileTypeConfig(String ids);

    /**
     * 启用/禁用文档类型设置
     *
     * @author zhousuxia
     * @version 2018年7月27日
     * @see RepFileTypeConfigServiceI
     * @since
     */
    void changeRepFileTypeStatus(String type,String ids);

    /**
     * 获取map<文档类型,自定义属性名称>
     * @param map
     */
    Map<String, String> getCustomAttrMap(Map<String, String> map, String entityUri);

    /**
     * 获取文档类型规则说明和备注
     * @param idRuleDescMap 编号规则id-描述的集合
     * @param idRemarkMap 文档类型配置id-备注的集合
     */
    void getRuleAndRemarkMap(Map<String, SerialNumberGeneratorInfo> idRuleDescMap, Map<String, String> idRemarkMap);

    /**
     * 导出
     * @param list 文档类型配置列表
     * @return
     */
    HSSFWorkbook export(List<RepFileTypeConfigVo> list, int max);

    /**
     * 软属性保存
     *
     * @author zhousuxia
     * @version 2018年7月27日
     * @see RepFileTypeConfigServiceI
     * @since
     */
    FeignJson editEntityAttributeAdditionalAttribute(String oldId, List<EntityAttributeAdditionalAttributeDto> entityAttributeAdditionalAttributeList);

    /**
     * 获取流程节点信息
     * @param originId 流程来源id
     * @return
     */
    List<BpmnTask> getBpmnTaskList(String originId);

}

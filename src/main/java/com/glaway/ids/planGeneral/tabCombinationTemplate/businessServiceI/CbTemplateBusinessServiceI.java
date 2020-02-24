package com.glaway.ids.planGeneral.tabCombinationTemplate.businessServiceI;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.CommonService;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.planGeneral.tabCombinationTemplate.vo.CombinationTemplateVo;

import java.util.List;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: CbTemplateBusinessServiceI
 * @Date: 2019/9/2-15:35
 * @since
 */
public interface CbTemplateBusinessServiceI extends CommonService {

    /**
     *移除绑定的组合模板信息
     * @param tabCombinationTemplateId 页签组合模板id
     */
    void removeCbTemplateInfos(String tabCombinationTemplateId);

    /**
     * 更新页签组合模板时保存组合模板信息
     * @param voList 页签组合模板集合
     * @param tabCombinationTemplate 页签组合模板
     * @param curUser 用户对象
     * @param orgId 组织id
     */
    void updateCbTemplateInfos(List<CombinationTemplateVo> voList, TabCombinationTemplate tabCombinationTemplate, TSUserDto curUser, String orgId);

}

package com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI;

import com.glaway.foundation.common.service.CommonService;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.CombinationTemplateInfo;

import java.util.List;

/**
 * @Description: 组合模板service
 * @author: sunmeng
 * @ClassName: CombinationTemplateEntityServiceI
 * @Date: 2019/8/30-13:47
 * @since
 */
public interface CombinationTemplateEntityServiceI extends CommonService {

    /**
     * 保存组合模板信息集合
     * @param combInfos 页签组合模板信息集合
     */
    void saveCbTemplateInfos(List<CombinationTemplateInfo> combInfos);

    /**
     * 通过页签组合模板信息获取组合模板信息
     * @param tabCbTemplateId 页签组合模板id
     * @return
     */
    List<CombinationTemplateInfo> getInfosBytabCbTemplateId(String tabCbTemplateId);
}

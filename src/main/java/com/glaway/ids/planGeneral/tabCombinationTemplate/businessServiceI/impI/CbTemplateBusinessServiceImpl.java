package com.glaway.ids.planGeneral.tabCombinationTemplate.businessServiceI.impI;

import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.ids.planGeneral.tabCombinationTemplate.businessServiceI.CbTemplateBusinessServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.CombinationTemplateInfo;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.CombinationTemplateEntityServiceI;
import com.glaway.ids.planGeneral.tabCombinationTemplate.vo.CombinationTemplateVo;
import com.glaway.ids.util.CommonInitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: CbTemplateBusinessServiceImpl
 * @Date: 2019/9/2-15:36
 * @since
 */
@Service("cbTemplateBusinessService")
@Transactional
public class CbTemplateBusinessServiceImpl extends CommonServiceImpl implements CbTemplateBusinessServiceI {

    @Autowired
    private CombinationTemplateEntityServiceI combinationTemplateEntityService;

    @Override
    public void removeCbTemplateInfos(String tabCombinationTemplateId) {
        //通过页签组合模板id获取绑定的组合模板信息
        List<CombinationTemplateInfo> infos = combinationTemplateEntityService.getInfosBytabCbTemplateId(tabCombinationTemplateId);
        batchDelete(infos);
    }

    @Override
    public void updateCbTemplateInfos(List<CombinationTemplateVo> voList, TabCombinationTemplate tabCombinationTemplate, TSUserDto curUser, String orgId) {
        //根据保存的页签组合模板信息id再保存组合模板信息
        List<CombinationTemplateInfo> combInfos = new ArrayList<>();
        for(int i = 0; i < voList.size(); i++) {
            CombinationTemplateInfo combInfo = new CombinationTemplateInfo();
            CommonInitUtil.initGLObjectForCreate(combInfo,curUser,orgId);
            combInfo.setOrderNum(i);
            combInfo.setName(voList.get(i).getName());
            combInfo.setTypeId(voList.get(i).getTypeId());
            combInfo.setTabCombinationTemplateId(tabCombinationTemplate.getId());
            combInfo.setDisplayAccess(voList.get(i).getDisplayAccess());
            combInfos.add(combInfo);
        }
        combinationTemplateEntityService.saveCbTemplateInfos(combInfos);
    }
}

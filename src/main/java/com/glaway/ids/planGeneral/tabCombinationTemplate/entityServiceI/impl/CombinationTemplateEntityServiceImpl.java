package com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.impl;

import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entity.CombinationTemplateInfo;
import com.glaway.ids.planGeneral.tabCombinationTemplate.entityServiceI.CombinationTemplateEntityServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 组合模板实现类
 * @author: sunmeng
 * @ClassName: CombinationTemplateEntityServiceImpl
 * @Date: 2019/8/30-13:48
 * @since
 */
@Service("combinationTemplateEntityService")
@Transactional
public class CombinationTemplateEntityServiceImpl extends CommonServiceImpl implements CombinationTemplateEntityServiceI {

    @Autowired
    private SessionFacade sessionFacade;

    @Override
    public void saveCbTemplateInfos(List<CombinationTemplateInfo> combInfos) {
        sessionFacade.batchSave(combInfos);
    }

    @Override
    public List<CombinationTemplateInfo> getInfosBytabCbTemplateId(String tabCbTemplateId) {
        String hql = "from CombinationTemplateInfo t where 1=1 and t.tabCombinationTemplateId='"+ tabCbTemplateId +"' order by t.orderNum asc";
        List<CombinationTemplateInfo> infos = findByQueryString(hql);
        return infos;
    }
}

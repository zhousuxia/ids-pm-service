package com.glaway.ids.project.plan.dto;

import com.glaway.foundation.common.vdata.GLVData;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileTypeDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 〈一句话功能简述〉
 * @author: sunmeng
 * @ClassName: DeliveryStandardDocTypeDto
 * @Date: 2019/8/7-10:56
 * @since
 */
@Getter
@Setter
public class DeliveryStandardDocTypeDto extends GLVData {

    /**
     * <!-- begin-user-doc -->交付项名称库ID
     * <!-- end-user-doc -->
     *
     * @generated
     */
    private String deliveryStandardId = null;

    private DeliveryStandardDto deliveryStandard = null;

    /**
     * <!-- begin-user-doc -->文档类型ID
     * <!-- end-user-doc -->
     *
     * @generated
     *
     */
    private String repFileTypeId = null;

    private RepFileTypeDto repFileType = null;
}

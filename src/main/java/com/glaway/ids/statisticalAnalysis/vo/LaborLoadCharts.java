package com.glaway.ids.statisticalAnalysis.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 人员负载分析报表对象
 * @author: sunmeng
 * @ClassName: LaborLoadCharts
 * @Date: 2019/12/5-19:58
 * @since
 */
@Getter
@Setter
public class LaborLoadCharts {
    /**
     * 名称
     */
    private String name;

    /**
     * 数据
     */
    private List<Integer> data = new ArrayList<>();

    /**
     * 颜色
     */
    private String color;
}

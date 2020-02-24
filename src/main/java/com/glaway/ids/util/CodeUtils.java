package com.glaway.ids.util;

import com.alibaba.fastjson.JSON;
import com.glaway.foundation.common.util.JsonUtil;
import com.glaway.ids.project.plan.entity.BasicLine;

import java.util.List;

/**
 * Created by LHR on 2019/6/12.
 */
public class CodeUtils {

    public static String replaceQuotes(String s) {
        return s.substring(1, s.length() - 1);
    }

    /**
     * List转List
     *
     * @param list
     * @param s
     * @param <T>
     * @return
     */
    public static <T> List JsonListToList(List<T> list, Class s) {
        return JSON.parseArray(JsonUtil.getListJsonWithoutQuote(list), s);
    }

    /**
     * bean转bean
     *
     * @param t
     * @param s
     * @return
     */
    public static <T> Object JsonBeanToBean(T t, Class s) {
        return JSON.parseObject(JSON.toJSONString(t), s);
    }

}

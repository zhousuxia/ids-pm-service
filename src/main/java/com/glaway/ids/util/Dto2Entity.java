package com.glaway.ids.util;

import net.sf.cglib.beans.BeanCopier;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: Dto与Entity相互转换工具类
 * @author: sunmeng
 * @ClassName: Dto2Entity
 * @Date: 2019/6/21-10:22
 * @since
 */
public class Dto2Entity {

    public static ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<String, BeanCopier>();

    /**
     * 通过cplib BeanCopier形式
     * @param source
     * @param target
     */
    public static void copyProperties(Object source,Object target) {
        String beanKey = generateKey(source.getClass(),target.getClass());
        BeanCopier copier = null;
        copier = BeanCopier.create(source.getClass(),target.getClass(),false);
        beanCopierMap.putIfAbsent(beanKey,copier);
        copier = beanCopierMap.get(beanKey);
        copier.copy(source,target,null);
    }

    private static String generateKey (Class<?> class1,Class<?> class2) {
        return class1.toString() + class2.toString();
    }
}

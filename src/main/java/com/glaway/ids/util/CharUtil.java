/*
 * 文件名：CharUtil.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：shitian
 * 修改时间：2018年8月19日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.util;


import java.util.Random;


/**
 * 随机字符生成器
 * 
 * @author shitian
 * @version 2018年8月19日
 * @see CharUtil
 * @since
 */
public class CharUtil {

    /**
     * 把整数转成小写字母（a-z）
     * 
     * @param n
     *            被转换的整数
     * @return
     * @see
     */
    public static char convertIntToChar(int n) {
        int t = (n - 1) % 26 + 97;
        return (char)t;
    }

    /**
     * 随机产生一个小写字母的字符
     * 
     * @return
     * @see
     */
    public static char randomChar() {
        Random r = new Random();
        int t = r.nextInt(26) + 97;
        return (char)t;
    }

    /**
     * 产生指定长度的的随机字符串（小写字母）
     * 
     * @param len
     * @return
     * @see
     */
    public static String randomStringByLength(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++ ) {
            sb.append(randomChar());
        }
        return sb.toString();
    }

    /**
     * 根据num产生指定长度的（小写字母）的字符串<br>
     * e.g: 1:a,2:b,...,27:aa
     * 
     * @param num
     * @return
     * @see
     */
    public static String generatorStringByNumber(int num) {
        StringBuilder sb = new StringBuilder();
        if ((num - 1) / 26 == 0) {
            return sb.append(convertIntToChar(num)).toString();
        }
        else {
            int tem = (num - 1) / 26;
            sb.append(convertIntToChar(num));
            return sb.insert(0, generatorStringByNumber(tem)).toString();
        }
    }

    public static void main(String[] args) {
        System.out.println((int)'a');
        System.out.println(randomStringByLength(4));
    }
}

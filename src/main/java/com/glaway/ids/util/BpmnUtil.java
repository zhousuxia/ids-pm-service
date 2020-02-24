/*
 * 文件名：BpmnUtil.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：shitian
 * 修改时间：2018年9月7日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.ids.common.pbmn.activity.entity.FlowElementModifyNode;

/**
 * 
 * 〈一句话功能简述>动态生成工作流工具类
 * 〈功能详细描述〉
 * @author shitian
 * @version 2018年9月7日
 * @see BpmnUtil
 * @since
 */
public class BpmnUtil {
    
    /**
     * 返回工作流文件的io流
     * @param type 
     * @return 
     * @see
     */
    public static InputStream getProcessXMLByType(String type){
        
        InputStream is = null;
        is = BpmnUtil.class.getClassLoader().getResourceAsStream("process-sample/singleProcess.bpmn");
        try {
            byte[] bs =new byte[2048];
            int len = 0;
            while((len=is.read(bs))>=0){
                System.out.print(new String(bs, 0, len));
            }
            FileUtils.copyInputStreamToFile(is, new File("process-sample","copy.xml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
    
    /**
     * 
     * 把工作流文件转换成工作流模型并返回模型
     * @param fileFullName
     * @return 
     * @see
     */
    @SuppressWarnings("finally")
    public static BpmnModel getBpmnModelFromFile(String fileFullName){
        BpmnModel model = null;
        try {
            InputStream in = BpmnUtil.class.getClassLoader().getResourceAsStream(fileFullName);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(in);
            BpmnXMLConverter converter = new BpmnXMLConverter();
            model = converter.convertToBpmnModel(reader);
        }
        catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
        catch (XMLStreamException e) {
            e.printStackTrace();
        }
        finally {
            return model;
        }
    }
    
    public static void modifyBpmnModel(BpmnModel model){}
    
    /**
     * 
     * 
     * @param obj
     * @param methodName
     * @param methodValue 
     * @see
     */
    public static void invokeFlowSetMethod(Object obj, String methodName, Object methodValue) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            String name = m.getName();
            if (name.startsWith("set")
                && StringUtils.equalsIgnoreCase(name.substring(3), methodName)) {
                Class<?>[] parameterTypes = m.getParameterTypes();
                if (!CommonUtil.isEmpty(parameterTypes) && parameterTypes.length == 1) {
                    if (parameterTypes[0] == String.class
                        || parameterTypes[0] == org.activiti.bpmn.model.MultiInstanceLoopCharacteristics.class) {
                        try {
                            m.invoke(obj, methodValue);
                        }
                        catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    /**
     * 
     * 返回流程模型所有流程元素
     * @param model
     * @return 
     * @see
     */
    public static Collection<FlowElement> getFlowElementsByModel(BpmnModel model){
        Collection<FlowElement> flowElements = model.getProcesses().get(0).getFlowElements();
        return flowElements;
    }
    
    /**
     * 
     * 返回流程模型所有流程元素
     * @param model
     * @return 
     * @see
     */
    public static Collection<FlowElement> getFlowElementsByFullPath(String fullPath){
        BpmnModel model = null;
        try {
            InputStream in = BpmnUtil.class.getClassLoader().getResourceAsStream(fullPath);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(in);
            BpmnXMLConverter converter = new BpmnXMLConverter();
            model = converter.convertToBpmnModel(reader);
        }
        catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
        catch (XMLStreamException e) {
            e.printStackTrace();
        }
        Collection<FlowElement> flowElements = model.getProcesses().get(0).getFlowElements();
        return flowElements;
    }
    public static void main(String[] args) {
        FlowElementModifyNode node = new FlowElementModifyNode();
        BpmnUtil.invokeFlowSetMethod(node,"FlowId","123");
    }
}

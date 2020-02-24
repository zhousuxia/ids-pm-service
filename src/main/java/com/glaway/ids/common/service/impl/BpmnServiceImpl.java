/*
 * 文件名：BpmnServiceImpl.java
 * 版权：Copyright by www.glaway.com
 * 描述：
 * 修改人：shitian
 * 修改时间：2018年8月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.common.service.impl;


import com.glaway.foundation.activiti.core.dto.FlowDeployEntityDto;
import com.glaway.foundation.activiti.core.dto.ObjectBusinessBPMNLinkDto;
import com.glaway.foundation.activiti.facade.WorkFlowFacade;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.exception.GWException;
import com.glaway.foundation.common.exception.NullParameterException;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.SystemLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.DBUtil;
import com.glaway.foundation.common.util.FileUtil;
import com.glaway.foundation.common.util.StringUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.service.activiti.FeignActivitiCommonService;
import com.glaway.foundation.jackrabbit.util.JackrabbitUtil;
import com.glaway.ids.common.pbmn.activity.entity.BpmnTask;
import com.glaway.ids.common.pbmn.activity.entity.FlowElementModifyNode;
import com.glaway.ids.common.service.BpmnServiceI;
import com.glaway.ids.config.vo.BpmnTaskVo;
import com.glaway.ids.constant.BpmnConstants;
import com.glaway.ids.constant.JackrabbitConstants;
import com.glaway.ids.dynamicform.constants.DynamicFormConstants;
import com.glaway.ids.dynamicform.service.DynamicFormCommonServiceI;
import com.glaway.ids.dynamicform.vo.DynamicFormCommonVo;
import com.glaway.ids.dynamicform.vo.DynamicFormFieldCommonVo;
import com.glaway.ids.util.BpmnUtil;
import com.glaway.ids.util.CharUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 〈bpmnService〉
 * 〈功能详细描述〉
 * 
 * @author shitian
 * @version 2018年8月10日
 * @see BpmnServiceImpl
 * @since
 */
@Service("bpmnService")
@Transactional
public class BpmnServiceImpl extends CommonServiceImpl implements BpmnServiceI {

    /**
     * 
     */
    private static final SystemLog log = BaseLogFactory.getSystemLog(BpmnServiceImpl.class);

    /**
     * redis缓存服务
     */
    @Autowired
    private RedisService redisService;

    /**
     * Jdbc服务接口
     */
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;


    /*@Autowired
    @Qualifier("repositoryService")
    private RepositoryService repositoryService;*/

    /**
     * 参数WorkFlowFacade
     */
    @Autowired
    private WorkFlowFacade workFlowFacade;


    @Autowired
    private DynamicFormCommonServiceI dynamicFormCommonService;

    @Autowired
    private FeignActivitiCommonService activitiCommonService;


    @Override
    public void clearBpmnTaskVoList(String type, String typeId) {
        redisService.deleteFromRedis(type, typeId);
    }

    @Override
    public List<BpmnTaskVo> getFromRedis(String type, String typeId) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(typeId)) {
            throw new NullParameterException();
        }
        return (List<BpmnTaskVo>)redisService.getFromRedis(type, typeId);
    }

    @Override
    public List<BpmnTaskVo> addListToRedis(String type, String typeId, List<BpmnTaskVo> taskVoList) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(typeId)) {
            throw new NullParameterException();
        }

        Collections.sort(taskVoList, new Comparator<BpmnTaskVo>() {
            @Override
            public int compare(BpmnTaskVo o1, BpmnTaskVo o2) {
                return o1.getOrderNum().compareTo(o2.getOrderNum());
            }
        });

        redisService.setToRedis(type, typeId, taskVoList);
        return getFromRedis(type, typeId);
    }


    @Override
    public List<BpmnTaskVo> moveTaskVoById(String type, String typeId, String ids, String moveType) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(typeId) || StringUtil.isEmpty(ids)) {
            throw new NullParameterException();
        }
        List<BpmnTaskVo> taskList = getFromRedis(type, typeId);
        if (CommonUtil.isEmpty(taskList)) {
            throw new GWException("不能移动任务节点为空的工作流程");
        }
        if (StringUtil.equals(moveType, BpmnConstants.DIRECTION_UP)
                || StringUtil.isEmpty(moveType)) {
            upTask(taskList, ids);

        }
        else if (StringUtil.equals(moveType, BpmnConstants.DIRECTION_DOWN)) {
            downTask(taskList, ids);
        }
        int num = 1;
        for (BpmnTaskVo vo : taskList) {
            vo.setOrderNum(Integer.toString(num++ ));
        }
        Collections.sort(taskList, new Comparator<BpmnTaskVo>() {
            @Override
            public int compare(BpmnTaskVo o1, BpmnTaskVo o2) {
                return o1.getOrderNum().compareTo(o2.getOrderNum());
            }
        });

        redisService.setToRedis(type, typeId, taskList);
        return getFromRedis(type, typeId);
    }

    @Override
    public List<BpmnTaskVo> addTaskVoToRedis(String type, String typeId, BpmnTaskVo task) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(typeId)) {
            throw new NullParameterException();
        }
        List<BpmnTaskVo> taskList = getFromRedis(type, typeId);
        if (CommonUtil.isEmpty(taskList)) {
            taskList = new ArrayList<BpmnTaskVo>();
        }

        if (!CommonUtil.isEmpty(task)) {
            int tail = taskList.size();
            task.setOrderNum(Integer.toString(++tail));
            if (CommonUtil.isEmpty(task.getId())) {
                task.setId(UUID.randomUUID().toString());
            }
            taskList.add(task);
        }
        Collections.sort(taskList, new Comparator<BpmnTaskVo>() {
            @Override
            public int compare(BpmnTaskVo o1, BpmnTaskVo o2) {
                return o1.getOrderNum().compareTo(o2.getOrderNum());
            }
        });

        redisService.setToRedis(type, typeId, taskList);
        return getFromRedis(type, typeId);
    }

    @Override
    public boolean isTaskNameRepeat(String type, String typeId, String name) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(typeId)) {
            throw new NullParameterException();
        }
        List<BpmnTaskVo> taskList = getFromRedis(type, typeId);

        if (CommonUtil.isEmpty(taskList)) {
            return false;
        }
        else {
            for (BpmnTaskVo vo : taskList) {
                if (StringUtil.equals(vo.getName(), name)) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * 向上移动流程节点
     *
     * @param list
     * @param ids
     * @see
     */
    private void upTask(List<BpmnTaskVo> list, String ids) {
        String[] split = ids.split(",");
        if (StringUtil.equals(list.get(0).getId(), split[0])) {
            throw new GWException("不能向上移动第一个流程节点");
        }
        try {
            for (int i = 1; i < list.size(); i++ ) {
                for (String id : split) {
                    if (StringUtil.equals(id, list.get(i).getId())) {
                        BpmnTaskVo temp1 = new BpmnTaskVo();
                        temp1 = (BpmnTaskVo)list.get(i - 1).clone();
                        BpmnTaskVo temp2 = new BpmnTaskVo();
                        temp2 = (BpmnTaskVo)list.get(i).clone();
                        list.set(i - 1, temp2);
                        list.set(i, temp1);
                    }
                }
            }
        }
        catch (CloneNotSupportedException e) {
            GWException ge = new GWException("克隆流程任务节点失败");
            ge.initCause(e);
            ge.printStackTrace();
        }
    }

    @Override
    public void dynamicDeployBpmnModel(String typeId, String processId, String processName, List<BpmnTaskVo> list, TSUserDto currentUser,String basePath) {
       System.out.println(".........start...");
      /*  String basePath = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath(
                "/") + "bpmn\\";*/
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        processId = null;
        // 根据任务节点获取选人表单
        String voName = null;
        ObjectBusinessBPMNLinkDto link = activitiCommonService.getObjectBusinessBPMNLinkInfo(
                BpmnConstants.BPMN_ENTITIY_NAME_PROJECT_FILE,
                BpmnConstants.OBJECT_BUSINESS_BPMN_LINK_BUSINESSTYPE + list.get(0).getOriginId());
        if (CommonUtil.isEmpty(list.get(0).getFormId())) {
            voName = BpmnConstants.EDIT_FORM_ID + Math.random();
            processId = BpmnConstants.BPMN_PROCESS_ID + CharUtil.randomStringByLength(5);
            if(!CommonUtil.isEmpty(link)){
                link.setBpmnName(processId);
                activitiCommonService.saveOrupdateObjectBusinessBPMNLink(link);
            }
        }
        else {
            voName = list.get(0).getFormId().split("_")[0];

            if (!CommonUtil.isEmpty(link) && !CommonUtil.isEmpty(link.getBpmnName())) {
                processId = link.getBpmnName();
            }
            else {
                throw new GWException("业务对象流程关联信息不能为空");
            }
        }

        DynamicFormCommonVo vo = new DynamicFormCommonVo();
        vo.setFormName(BpmnConstants.EDIT_FORM_NAME);
        vo.setFormCode(voName);
        vo.setBizVersion(CommonUtil.isEmpty(list.get(0).getVersion()) ? "0" : list.get(0).getVersion());
        List<DynamicFormFieldCommonVo> fieldList = new ArrayList<DynamicFormFieldCommonVo>();
        int count = 0;
        for (BpmnTaskVo v : list) {
            DynamicFormFieldCommonVo field = new DynamicFormFieldCommonVo();
            field.setFieldName(BpmnConstants.TASK_ROLE_VARIBALES_PREFIX
                    + (CharUtil.generatorStringByNumber(++count)));
            field.setFieldTitle(v.getName());
            field.setFieldType(DynamicFormConstants.SELECT_USER);
            field.setFieldFlow("1");
            field.setOrgrequired("true");
            field.setMultiple(v.getApproveType().equalsIgnoreCase("singleSign") ? "false" : "true");
            field.setFieldCode("");
            if (v.getApproveType().equalsIgnoreCase("singleSign")) {
                field.setFieldPlugins("dynccommonuser");
            }
            else {
                field.setFieldPlugins("dynccommonusers");
            }
            fieldList.add(field);
        }
        vo.setFieldList(fieldList);
        String returnVersion = null;
        {
            DynamicFormCommonVo formVo = dynamicFormCommonService.generateDynamicForm(vo,currentUser);
            if (!CommonUtil.isEmpty(formVo) && !CommonUtil.isEmpty(formVo.getBizVersion())) {
                returnVersion = formVo.getBizVersion();
            }
            else {
                throw new GWException("获取动态表单发生异常");
            }
        }

        // 1. Build up the model from scratch
        BpmnModel model = new BpmnModel();
        org.activiti.bpmn.model.Process process = new org.activiti.bpmn.model.Process();
        model.addProcess(process);
        // process.setId(processId+":"+typeId+":process");
        process.setId(processId);
        process.setName(processName);
        // 设置开始节点
        process.addFlowElement(createStartEvent());
        // 设置任务发起人初始编辑节点1
        process.addFlowElement(createEditTask(BpmnConstants.EDIT_TASK_ID,
                BpmnConstants.EDIT_TASK_TITLE, "${user}", voName + "_" + returnVersion));
        process.addFlowElement(createSequenceFlow("flow0", "start", BpmnConstants.EDIT_TASK_ID,
                "", ""));



        // 从流程图模板处修改任务节点，使流程图有坐标，在页面可以显示
        BpmnModel xmlModel = null;
        String sourceProcessName="";
        int len = list.size();
        if (len == 1) {
            sourceProcessName = "process-sample/oneUserTask.bpmn";
        }
        else if (len == 2) {
            sourceProcessName = "process-sample/twoUserTask.bpmn";
        }
        else if (len == 3) {
            sourceProcessName = "process-sample/threeUserTask.bpmn";
        }
        if(!CommonUtil.isEmpty(sourceProcessName)){
            Map<String, FlowElementModifyNode> map = new HashMap<>();

            FlowElementModifyNode editNode = new FlowElementModifyNode();
            editNode.setFlowId(BpmnConstants.EDIT_TASK_ID);
            editNode.setFlowName(BpmnConstants.EDIT_TASK_TITLE);
            editNode.setFlowType(BpmnConstants.FLOWTYPE_USERTASK);
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(BpmnConstants.FLOWPROPERTY_ASSIGNEE, "${user}");
            properties.put(BpmnConstants.FLOWPROPERTY_FORMKEY, voName + "_" + returnVersion);
            editNode.setProperties(properties);
            map.put(editNode.getFlowId(), editNode);

            getModifyNode(map, list);
            xmlModel = BpmnUtil.getBpmnModelFromFile(sourceProcessName);
            Collection<FlowElement> flowElements = BpmnUtil.getFlowElementsByModel(xmlModel);
            Set<String> ids = map.keySet();
            List<String> hasDeal = new ArrayList<String>();

            Process process2 = xmlModel.getProcesses().get(0);
            process2.setId(processId);
            process2.setName(processName);
            for (FlowElement e : flowElements) {
                for (String id : ids) {
                    if (!hasDeal.contains(id)) {
                        if (StringUtils.equals(e.getId(), id)) {
                            FlowElementModifyNode node = map.get(id);
                            e.setName(node.getFlowName());
                            e.setId(node.getFlowId());
                            Map<String, Object> properties2 = node.getProperties();
                            if (!CommonUtil.isEmpty(properties2)) {
                                Set<Map.Entry<String, Object>> entrySet = properties2.entrySet();
                                for (Map.Entry<String, Object> entry : entrySet) {
                                    BpmnUtil.invokeFlowSetMethod(e, entry.getKey(), entry.getValue());
                                }
                            }
                            hasDeal.add(id);
                        }
                    }
                }
            }
        }

        if (!CommonUtil.isEmpty(list)) {
            int curIndex = 0;
            int size = list.size();
            Map<String, String> flowMap = new HashMap<String, String>();
            Map<String, String> rejectFlowMap = new HashMap<String, String>();
            for (int i = 0; i < size; i++ ) {
                if (0 == i) {
                    flowMap.put(BpmnConstants.USER_TASK_PREFIX + (i + 1),
                            BpmnConstants.EDIT_TASK_ID);
                    flowMap.put(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i + 1),
                            BpmnConstants.USER_TASK_PREFIX + (i + 1));
                    rejectFlowMap.put(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i + 1),
                            BpmnConstants.EDIT_TASK_ID);
                }
                else {
                    flowMap.put(BpmnConstants.USER_TASK_PREFIX + (i + 1),
                            BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i));
                    flowMap.put(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i + 1),
                            BpmnConstants.USER_TASK_PREFIX + (i + 1));
                    rejectFlowMap.put(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i + 1),
                            BpmnConstants.EDIT_TASK_ID);
                }
            }
            for (BpmnTaskVo taskVo : list) {
                curIndex++ ;
                String userId = BpmnConstants.USER_TASK_PREFIX + curIndex;
                String gateWayId = BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + curIndex;

                String taskName = taskVo.getName();
                String type = taskVo.getApproveType();
                process.addFlowElement(createUserTask(
                        userId,
                        taskName,
                        BpmnConstants.TASK_ROLE_VARIBALES_PREFIX
                                + CharUtil.generatorStringByNumber(curIndex), type,
                        taskVo.getApprovePercent() == null ? null : taskVo.getApprovePercent()));
                if (curIndex == 1) {
                    process.addFlowElement(createSequenceFlowAndListener(
                            BpmnConstants.BPMN_LISTENER_SEQUENCEFLOW + curIndex,
                            flowMap.get(userId),
                            userId,
                            BpmnConstants.BPMN_LISTENER_SEQUENCEFLOW + curIndex,"${repFileTypeEditUrl}",
                            "",
                            Arrays.asList("take:class:com.glaway.foundation.activiti.task.common.CommonExecutionListener")));
                }
                else {
                    process.addFlowElement(createSequenceFlow(flowMap.get(userId), userId));
                }

                process.addFlowElement(createExclusiveGateway(gateWayId));
                process.addFlowElement(createSequenceFlow("flow" + curIndex,
                        flowMap.get(gateWayId), gateWayId, BpmnConstants.FLOW_AGREE_NAME,
                        "${approve=='true'}"));
                process.addFlowElement(createSequenceFlowAndListener(
                        "rejectFlow" + curIndex,
                        gateWayId,
                        rejectFlowMap.get(gateWayId),
                        BpmnConstants.FLOW_DISAGREE_NAME,"${refuseUrl}",
                        "${approve=='false'}",
                        Arrays.asList("take:class:com.glaway.foundation.activiti.task.common.CommonExecutionListener")));
            }
        }
        // 设置结束节点
        process.addFlowElement(createEndEventAndListener(Arrays.asList("end:class:com.glaway.foundation.activiti.task.common.CommonExecutionListener"),"${stopUrl}"));
        process.addFlowElement(createSequenceFlow("flowEnd",
                BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + list.size(), "end",
                BpmnConstants.FLOW_AGREE_NAME, "${approve=='true'}"));
        if(!CommonUtil.isEmpty(xmlModel)){
            model = xmlModel;
        }
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] convertToXML = bpmnXMLConverter.convertToXML(model);
        String bytes = null;
        try {
            bytes = new String(convertToXML, BpmnConstants.BPMN_CONVERT_ENCODING);


        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(bytes);


      /*  SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String time = df.format(new Date());
        String fileName = "activityBpmn_"+time+".bpmn";*/
        String fileName = processId+".bpmn";


        //测试用，勿删
       /* String path = "C:\\activity";
        File myFile = new File(path);
        if(!myFile.exists()){
            myFile.mkdir();
        }

        String filePath = "C:\\activity\\"+fileName;
        try {
            FileOutputStream os = new FileOutputStream(filePath);
            try {
                os.write(convertToXML);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        InputStream in_bytes = null;
        in_bytes = new ByteArrayInputStream(convertToXML);
        String actiFilePath = JackrabbitUtil.handleFileUpload(in_bytes, fileName, JackrabbitConstants.REPFILECONFIGTYPE_FILE_PATH);

        //测试用，勿删
       /* InputStream fis = JackrabbitUtil.downLoadFile(actiFilePath);
        try {
            System.out.println(FileUtil.downFile("111.bpmn", fis, "D:"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        FeignJson fj = activitiCommonService.deployNewFlowByUrl(actiFilePath,"bpmn",fileName);
        if(fj.isSuccess()){
            // 发布成功，保存业务对象流程信息关联
            Map<String, String> columns = new HashMap<String, String>();
            columns.put("bpmnName", processId);
            columns.put("businessType", BpmnConstants.OBJECT_BUSINESS_BPMN_LINK_BUSINESSTYPE + typeId);
            columns.put("remark", "文档类型审核流程");
            columns.put("entityName", BpmnConstants.BPMN_ENTITIY_NAME_PROJECT_FILE);
            addObjectBusinessBpmnLink(columns);

            // 发布成功，保存节点信息
            for (BpmnTaskVo v2 : list) {
                v2.setVersion(returnVersion);
                v2.setFormId(voName + "_" + returnVersion);
            }
            addTaskList(list, String.valueOf(fj.getObj()));
        }

        System.out.println(".........end...");
    }


    private byte[] InputStream2ByteArray(String filePath) throws IOException{
        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();
        return data;
    }


    private byte[] toByteArray(InputStream in) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*4];
        int n = 0;
        while((n = in.read(buffer)) != -1){
            out.write(buffer,0,n);
        }
        return out.toByteArray();
    }


    @Override
    public void deployNewFlowDeployEntity(FlowDeployEntityDto entity) {
        activitiCommonService.updateStatusByFlowDeployEntityKey("历史中","已发布",entity.getKey());
        activitiCommonService.saveOrupdateByFlowDeployEntity(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addTaskList(List<BpmnTaskVo> taskVoList, String processId) {
        if (!CommonUtil.isEmpty(taskVoList)) {
            List<BpmnTask> taskList = convertVoToPojo(taskVoList, processId);
            String formId = taskVoList.get(0).getFormId();
            sessionFacade.executeSql2("update CM_BPMN_TASK set avaliable = '0 ' where formId like '%"+formId.substring(0,formId.lastIndexOf("_"))+"%'");
            saveTaskBySql(taskList);
        }
    }


    /**
     *
     * 保存节点任务到数据库
     * @param list
     * @see
     */
    private void saveTaskBySql(List<BpmnTask> list){

        Connection conn = null;
        // 表单相关信息
        PreparedStatement insertForForm = null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            conn.setAutoCommit(false);
            if(!CommonUtil.isEmpty(list)) {
                String sql = "insert into cm_bpmn_task (ID, CREATEBY, CREATENAME, CREATETIME, CREATEFULLNAME, ORIGINID, ORDERNUM, ROLES, "
                        + "APPROVETYPE, NUMBERS, NAME, REMARK, FORMID, VERSION, APPROVEPERCENT, PROCESSID, AVALIABLE)"
                        + " values (?, ? , ?, ?, ?, ?, ?, ?, ? ,? , ?, ?, ?, ?, ?, ?, ?)";
                insertForForm = conn.prepareStatement(sql);
                for(BpmnTask task: list){
                    insertForForm.setObject(1, task.getId());
                    insertForForm.setObject(2, task.getCreateBy());
                    insertForForm.setObject(3, task.getCreateName());
                    insertForForm.setObject(4, new Timestamp(new Date().getTime()));
                    insertForForm.setObject(5, task.getCreateFullName());
                    insertForForm.setObject(6, task.getOriginId());
                    insertForForm.setObject(7, task.getOrderNum());
                    insertForForm.setObject(8, task.getRoles());
                    insertForForm.setObject(9, task.getApproveType());
                    insertForForm.setObject(10, task.getNumbers());
                    insertForForm.setObject(11, task.getName());
                    insertForForm.setObject(12, task.getRemark());
                    insertForForm.setObject(13, task.getFormId());
                    insertForForm.setObject(14, task.getVersion());
                    insertForForm.setObject(15, task.getApprovePercent());
                    insertForForm.setObject(16, task.getProcessId());
                    insertForForm.setObject(17, "1");

                    insertForForm.addBatch();
                }
                insertForForm.executeBatch();
                conn.commit();
                conn.setAutoCommit(true);
            }
        }
        catch (Exception ex) {
            try {
                conn.rollback();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
        finally {
            DBUtil.closeConnection(null, insertForForm, conn);
        }


    }



    /**
     * vo转成pojo
     *
     * @param taskList
     * @return
     * @see
     */
    private List<BpmnTask> convertVoToPojo(List<BpmnTaskVo> taskList, String processId) {
        List<BpmnTask> list = new ArrayList<BpmnTask>();
        for (BpmnTaskVo vo : taskList) {
            BpmnTask task = new BpmnTask();
            CommonUtil.glObjectSet(task);
            task.setName(vo.getName());
            task.setNumbers(vo.getNumbers());
            task.setApproveType(vo.getApproveType());
            task.setOrderNum(vo.getOrderNum());
            task.setOriginId(vo.getOriginId());
            task.setRoles(vo.getRoles());
            task.setVersion(vo.getVersion());
            task.setRemark(vo.getRemark());
            task.setFormId(vo.getFormId());
            task.setApprovePercent(vo.getApprovePercent());
            task.setProcessId(processId);
            task.setId(UUID.randomUUID().toString());
            list.add(task);
        }
        return list;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void addObjectBusinessBpmnLink(Map<String, String> columns) {

        /*long count = sessionFacade.getCountByHql(
                "select count(*) from ObjectBusinessBPMNLink t where t.businessType=?",
                columns.get("businessType"));*/
        long count = 0;
        List<ObjectBusinessBPMNLinkDto> list = activitiCommonService.getObjectBusinessBPMNLinkBybusinessType(columns.get("businessType"));
        count = list.size();
        if (count > 0) {
            System.out.println("ObjectBusinessBPMNLink already exists!");
        }
        else {
            ObjectBusinessBPMNLinkDto link = new ObjectBusinessBPMNLinkDto();
            link.setBpmnName(columns.get("bpmnName"));
            link.setBusinessType(columns.get("businessType"));
            link.setRemark(columns.get("remark"));
            link.setEntityName(columns.get("entityName"));
            activitiCommonService.saveOrupdateObjectBusinessBPMNLink(link);
        }

    }


    /**
     * 创建节点任务 个人任务
     *
     * @param id
     *            任务id标识
     * @param name
     *            任务名称
     * @return
     */
    private UserTask createUserTask(String id, String name, String roleVar, String assignType,
                                    String approvePercent) {

        UserTask userTask = new UserTask();
        String userVar = "${" + roleVar + "}";
        MultiInstanceLoopCharacteristics multiElement = new MultiInstanceLoopCharacteristics();
        if (StringUtil.equals(BpmnConstants.ASSIGN_MODE_SIGNLE, assignType)) {
            userTask.setAssignee(userVar);
        }
        else if (StringUtil.equals(BpmnConstants.ASSIGN_MODE_VIE, assignType)) {
            multiElement.setSequential(false);
            multiElement.setElementVariable(roleVar);
            multiElement.setInputDataItem(userVar);
            multiElement.setCompletionCondition("${signOnePassAndOneVetoService.signTaskRule(execution)}");
            userTask.setLoopCharacteristics(multiElement);
            userTask.setAssignee(userVar);
        }
        else if (StringUtil.equals(BpmnConstants.ASSIGN_MODE_COUNTER, assignType)) {
            multiElement.setSequential(false);
            multiElement.setElementVariable(roleVar);
            multiElement.setInputDataItem(userVar);
            if (!CommonUtil.isBlank(approvePercent)) {

                multiElement.setCompletionCondition("${signMorePassService.signTaskRule(execution,"
                        + approvePercent + ")}");
            }
            else {
                multiElement.setCompletionCondition("${nrOfInstances == nrOfCompletedInstances}");
            }
            userTask.setLoopCharacteristics(multiElement);
            userTask.setAssignee(userVar);
        }
        userTask.setName(name);
        userTask.setId(id);

        return userTask;
    }


    /**
     * 获取需要修改的节点对象
     */
    private  Map<String, FlowElementModifyNode> getModifyNode(Map<String, FlowElementModifyNode> map , List<BpmnTaskVo> list){

        if (!CommonUtil.isEmpty(list)) {
            int curIndex = 0;
            int size = list.size();
            Map<String, String> flowMap = new HashMap<String, String>();
            Map<String, String> rejectFlowMap = new HashMap<String, String>();
            for (int i = 0; i < size; i++ ) {
                if (0 == i) {
                    flowMap.put(BpmnConstants.USER_TASK_PREFIX + (i + 1),
                            BpmnConstants.EDIT_TASK_ID);
                    flowMap.put(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i + 1),
                            BpmnConstants.USER_TASK_PREFIX + (i + 1));
                    rejectFlowMap.put(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i + 1),
                            BpmnConstants.EDIT_TASK_ID);
                }
                else {
                    flowMap.put(BpmnConstants.USER_TASK_PREFIX + (i + 1),
                            BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i));
                    flowMap.put(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i + 1),
                            BpmnConstants.USER_TASK_PREFIX + (i + 1));
                    rejectFlowMap.put(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + (i + 1),
                            BpmnConstants.EDIT_TASK_ID);
                }
            }
            for (BpmnTaskVo taskVo : list) {
                curIndex++ ;
                String userId = BpmnConstants.USER_TASK_PREFIX + curIndex;
                String gateWayId = BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + curIndex;

                String taskName = taskVo.getName();
                String type = taskVo.getApproveType();

                FlowElementModifyNode userNode = createUserNode(
                        userId,
                        taskName,
                        BpmnConstants.TASK_ROLE_VARIBALES_PREFIX
                                + CharUtil.generatorStringByNumber(curIndex), type,
                        taskVo.getApprovePercent() == null ? null : taskVo.getApprovePercent());
                map.put(userNode.getFlowId(), userNode);
                if (curIndex == 1) {
                    createSequenceFlowAndListener(
                            BpmnConstants.BPMN_LISTENER_SEQUENCEFLOW + curIndex,
                            flowMap.get(userId),
                            userId,
                            BpmnConstants.BPMN_LISTENER_SEQUENCEFLOW + curIndex,"${repFileTypeEditUrl}",
                            "",
                            Arrays.asList("take:class:com.glaway.foundation.activiti.task.common.CommonExecutionListener"));
                }
                else {
                    createSequenceFlow(flowMap.get(userId), userId);
                }

                createExclusiveGateway(gateWayId);
                createSequenceFlow("flow" + curIndex, flowMap.get(gateWayId), gateWayId,
                        BpmnConstants.FLOW_AGREE_NAME, "${approve=='true'}");
                createSequenceFlowAndListener(
                        "rejectFlow" + curIndex,
                        gateWayId,
                        rejectFlowMap.get(gateWayId),
                        BpmnConstants.FLOW_DISAGREE_NAME,"${refuseUrl}",
                        "${approve=='false'}",
                        Arrays.asList("take:class:com.glaway.foundation.activiti.task.common.CommonExecutionListener"));
            }
        }
        // 设置结束节点
        createEndEventAndListener(Arrays.asList("end:class:com.glaway.foundation.activiti.task.common.CommonExecutionListener"),"${stopUrl}");
        createSequenceFlow("flowEnd", BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX + list.size(), "end",
                BpmnConstants.FLOW_AGREE_NAME, "${approve=='true'}");
        return map;
    }


    /**
     * 设置结束节点
     *
     * @return
     */
    private EndEvent createEndEventAndListener(List<String> listeners,String listenerName) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        endEvent.setName("end");
        if (!CommonUtil.isEmpty(listeners)) {
            List<ActivitiListener> list = new ArrayList<ActivitiListener>();
            for (String sub : listeners) {
                String[] ss = sub.split(":");
                ActivitiListener listener = new ActivitiListener();
                List<FieldExtension> fieldExtensions = new ArrayList();
                FieldExtension field = new FieldExtension();
                field.setFieldName("listenerFeign");
                field.setExpression(listenerName);
                fieldExtensions.add(field);
                listener.setFieldExtensions(fieldExtensions) ;
                listener.setEvent(ss[0]);
                // Spring配置以变量形式调用无法写入，只能通过继承TaskListener方法，
                listener.setImplementationType(ss[1]);
                listener.setImplementation(ss[2]);
                list.add(listener);
            }
            endEvent.setExecutionListeners(list);
        }
        return endEvent;
    }


    /**
     * 排他网关节点
     *
     * @param id
     * @return
     */
    private ExclusiveGateway createExclusiveGateway(String id) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(id);
        exclusiveGateway.setName(id);
        return exclusiveGateway;
    }


    /**
     * 设置有监听的连线
     *
     * @param from
     *            从哪里出发
     * @param to
     *            连接到哪里
     * @param name
     *            连线名称
     * @param conditionExpression
     *            判断条件${arg>2}
     * @param listeners
     *            event:type:fullClassName
     * @return
     */
    private SequenceFlow createSequenceFlowAndListener(String sequenceFlowId, String from,
                                                       String to, String name,String listenerName,
                                                       String conditionExpression,
                                                       List<String> listeners) {
        SequenceFlow flow = new SequenceFlow();
        flow.setId(sequenceFlowId);
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        flow.setName(name);
        if (from.contains(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX)) {
            if (null != conditionExpression && !"".equals(conditionExpression)) {
                flow.setConditionExpression(conditionExpression);
            }
        }
        if (!CommonUtil.isEmpty(listeners)) {
            List<ActivitiListener> list = new ArrayList<ActivitiListener>();
            for (String sub : listeners) {
                String[] ss = sub.split(":");
                ActivitiListener listener = new ActivitiListener();
                List<FieldExtension> fieldExtensions = new ArrayList();
                FieldExtension field = new FieldExtension();
                field.setFieldName("listenerFeign");
                field.setExpression(listenerName);
                fieldExtensions.add(field);
                listener.setFieldExtensions(fieldExtensions) ;
                listener.setEvent(ss[0]);
                // Spring配置以变量形式调用无法写入，只能通过继承TaskListener方法，
                listener.setImplementationType(ss[1]);
                listener.setImplementation(ss[2]);
                list.add(listener);
            }
            flow.setExecutionListeners(list);
        }
        return flow;
    }



    /**
     * 用户任务节点
     */
    private FlowElementModifyNode createUserNode(String id, String name, String roleVar, String assignType,
                                                 String approvePercent) {

        FlowElementModifyNode user = new FlowElementModifyNode();
        user.setFlowId(id);
        user.setFlowName(name);
        String userVar = "${" + roleVar + "}";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(BpmnConstants.FLOWPROPERTY_ASSIGNEE, userVar);
        MultiInstanceLoopCharacteristics multiElement = new MultiInstanceLoopCharacteristics();
        if (StringUtil.equals(BpmnConstants.ASSIGN_MODE_SIGNLE, assignType)) {

        }
        else if (StringUtil.equals(BpmnConstants.ASSIGN_MODE_VIE, assignType)) {
            multiElement.setSequential(false);
            multiElement.setElementVariable(roleVar);
            multiElement.setInputDataItem(userVar);
            multiElement.setCompletionCondition("${signOnePassAndOneVetoService.signTaskRule(execution)}");
            map.put(BpmnConstants.FLOWPROPERTY_SUBELEMENT_LOOPCHARACTERISTICS, multiElement);
        }
        else if (StringUtil.equals(BpmnConstants.ASSIGN_MODE_COUNTER, assignType)) {
            multiElement.setSequential(false);
            multiElement.setElementVariable(roleVar);
            multiElement.setInputDataItem(userVar);
            if (!CommonUtil.isBlank(approvePercent)) {

                multiElement.setCompletionCondition("${signMorePassService.signTaskRule(execution,"
                        + approvePercent + ")}");
            }
            else {
                multiElement.setCompletionCondition("${nrOfInstances == nrOfCompletedInstances}");
            }
            map.put(BpmnConstants.FLOWPROPERTY_SUBELEMENT_LOOPCHARACTERISTICS, multiElement);
        }
        user.setProperties(map);
        return user;
    }

    /**
     * 设置连线
     *
     * @param from
     *            从哪里出发
     * @param to
     *            连接到哪里
     * @return
     */
    private SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }


    /**
     * 设置连线
     *
     * @param sequenceFlowId
     *            从哪里出发
     * @param from
     *            从哪里出发
     * @param to
     *            连接到哪里
     * @param name
     *            连线名称
     * @param conditionExpression
     *            判断条件${arg>2}
     * @return
     */
    private SequenceFlow createSequenceFlow(String sequenceFlowId, String from, String to,
                                            String name, String conditionExpression) {
        SequenceFlow flow = new SequenceFlow();
        flow.setId(sequenceFlowId);
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        if (from.contains(BpmnConstants.EXCLUSIVE_GATEWAY_PREFIX)) {
            flow.setName(name);
            if (null != conditionExpression && !"".equals(conditionExpression)) {
                flow.setConditionExpression(conditionExpression);
            }
        }
        return flow;
    }



    /**
     * Description: <br>
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param id
     * @param name
     * @param assignee
     * @param formKey
     * @return
     * @see
     */
    private UserTask createEditTask(String id, String name, String assignee, String formKey) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        if (StringUtil.isEmpty(formKey)) {
            userTask.setFormKey(formKey);
        }
        else {
            userTask.setFormKey(formKey);
        }
        return userTask;
    }


    /**
     * 设置起始节点
     *
     * @return
     */
    private StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        startEvent.setName("start");
        return startEvent;
    }


    @Override
    public List<BpmnTaskVo> deployTaskFlow(String type, String typeId, String ids, String processName, String basePath,TSUserDto currentUser) {
        List<BpmnTaskVo> taskList = getFromRedis(type, typeId);
        if (CommonUtil.isEmpty(taskList)) {
            List<BpmnTask> list = sessionFacade.executeQuery(
                    "from BpmnTask where originId=? and avaliable = '1'", new Object[] {typeId});
            if(CommonUtil.isEmpty(list)) {  // 第一次部署流程， 节点不能为空
                throw new GWException("部署流程时，任务节点不能为空");
            } else { // 已经存在流程，删除
                String processId = list.get(0).getProcessId();
                String businessType = "fileType:"+typeId;
                // projFileProcess_rzfjk:7:17572
  //              workFlowFacade.getWorkFlowCommonService().deleteDeployedProcessByDefinitionId(processId);   //执行此方法控制台报### SQL: select * from ACT_PROCDEF_INFO where PROC_DEF_ID_ = ? Cause: java.sql.SQLSyntaxErrorException: ORA-00942: 表或视图不存在
                sessionFacade.executeSql2("update CM_BPMN_TASK set avaliable = '0 ' where processId = '"+processId+"'");
                //TODO
                activitiCommonService.updateStatusByFlowDeployEntityKey("历史中","已发布",processId.substring(0, processId.indexOf(":")));
 //               sessionFacade.executeSql2("update act_fd_deployment set status ='历史中', createTime =sysdate where 1=1 and status ='已发布' and key = '"+processId.substring(0, processId.indexOf(":"))+"'");
                activitiCommonService.updateBpmnNameBybusinessTypeAndentityName("",businessType,"ProjectFile");
//                sessionFacade.executeSql2("update OBJECT_BUSINESS_BPMN_LINK set bpmnName ='' where 1=1 and businessType ='"+businessType+"' and entityName = 'ProjectFile'");
//                this.executeSql(
//                    "update  act_fd_deployment set status ='历史中', createTime =sysdate where 1=1 and status ='已发布' and key =:key",
//                    processId.substring(0, processId.indexOf(":")));
            }
            // 如果ids是空， 且CM_BPMN_TASK中存在流程，删除流程
        } else {
            try {

                dynamicDeployBpmnModel(typeId, processName, processName
                        + BpmnConstants.FLOW_NAME_SUFFIX, taskList,currentUser,basePath);
            }
            catch (Exception e) {
                System.out.println("depoly error");
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<BpmnTaskVo> batchDeleteFromRedis(String type, String typeId, String ids) {
        if (StringUtil.isEmpty(type) || StringUtil.isEmpty(typeId) || StringUtil.isEmpty(ids)) {
            throw new NullParameterException();
        }
        List<BpmnTaskVo> taskList = getFromRedis(type, typeId);
        if (!CommonUtil.isEmpty(taskList)) {
            Iterator<BpmnTaskVo> it = taskList.iterator();
            while (it.hasNext()) {
                BpmnTaskVo next = it.next();
                for (String id : ids.split(",")) {
                    if (StringUtil.equals(id, next.getId())) {
                        it.remove();
                    }
                }
            }
        }
        Collections.sort(taskList, new Comparator<BpmnTaskVo>() {
            @Override
            public int compare(BpmnTaskVo o1, BpmnTaskVo o2) {
                return o1.getOrderNum().compareTo(o2.getOrderNum());
            }
        });
        int order = 1;
        for (BpmnTaskVo vo : taskList) {
            vo.setOrderNum(Integer.toString(order++ ));
        }
        redisService.setToRedis(type, typeId, taskList);
        return getFromRedis(type, typeId);
    }

    /**
     * 向下移动流程节点
     *
     * @param list
     * @param ids
     * @see
     */
    private void downTask(List<BpmnTaskVo> list, String ids) {
        String[] split = ids.split(",");
        int size = list.size();
        if (StringUtil.equals(list.get(size - 1).getId(), split[0])) {
            throw new GWException("不能向下移动最后一个流程节点");
        }
        try {
            for (int i = size - 2; i >= 0; i-- ) {
                for (String id : split) {
                    if (StringUtil.equals(id, list.get(i).getId())) {
                        BpmnTaskVo temp1 = new BpmnTaskVo();
                        temp1 = (BpmnTaskVo)list.get(i).clone();
                        BpmnTaskVo temp2 = new BpmnTaskVo();
                        temp2 = (BpmnTaskVo)list.get(i + 1).clone();
                        list.set(i, temp2);
                        list.set(i + 1, temp1);
                    }
                }
            }
        }
        catch (CloneNotSupportedException e) {
            GWException ge = new GWException("克隆流程任务节点失败");
            ge.initCause(e);
            ge.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BpmnTaskVo> queryBpmnTaskList(String originId) {
        List<BpmnTask> tasks = new ArrayList<BpmnTask>();
        String lastVersion = queryBpmnTaskLastVersion(originId);
        if (StringUtil.equals(lastVersion, "0")) {
            tasks = (List<BpmnTask>)sessionFacade.findHql(
                    "from BpmnTask t where t.originId=? and avaliable='1' order by t.orderNum", originId);
        }
        else {
            tasks = (List<BpmnTask>)sessionFacade.findHql(
                    "from BpmnTask t where t.originId=? and t.version=? and avaliable='1' order by t.orderNum",
                    originId, lastVersion);
        }
        List<BpmnTaskVo> vos = new ArrayList<BpmnTaskVo>();
        if (!CommonUtil.isEmpty(tasks)) {
            for (BpmnTask task : tasks) {
                BpmnTaskVo vo = new BpmnTaskVo();
                vo.setName(task.getName());
                vo.setNumbers(task.getNumbers());
                vo.setApproveType(task.getApproveType());
                vo.setRoles(task.getRoles());
                vo.setOrderNum(task.getOrderNum());
                vo.setOriginId(task.getOriginId());
                vo.setRemark(task.getRemark());
                vo.setVersion(task.getVersion());
                vo.setId(task.getId());
                vo.setFormId(task.getFormId());
                if (!CommonUtil.isEmpty(task.getApprovePercent())) {
                    vo.setApprovePercent(task.getApprovePercent());
                }
                vos.add(vo);
            }
        }
        return vos;
    }

    @Override
    public String queryBpmnTaskLastVersion(String originId) {

        @SuppressWarnings("unchecked")
        List<Object> list = sessionFacade.executeQuery(
                "select max(version) from BpmnTask where originId=? and avaliable='1'", new Object[] {originId});
        if (!CommonUtil.isEmpty(list)) {
            String result = null;
            if (list.get(0) != null) {
                result = (String)list.get(0);
            }
            return result;
        }
        else {
            return "0";
        }
    }
}

--基本信息--
delete from pm_data_source_Object where id='4028f0066d48f35e016d48f8d5ab0011';
delete from pm_data_source_Object where id='4028f0066d48f35e016d48fec1fc0012';
delete from pm_object_property_info where DATASOURCEID='4028f0066d48f35e016d48f8d5ab0011';
delete from pm_object_property_info where DATASOURCEID='4028f0066d48f35e016d48fec1fc0012';

insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('4028f0066d48f35e016d48f8d5ab0011', null, 'com.glaway.ids.project.plan.entity.Plan', 'ids-pm-service', '', '4028f0066d48f35e016d48f864450001', '', '1', 'planNumber,formId,planOrder,isCreateByPmo,planName,projectId,project,projectName,currentUser,parentPlanId,parentPlan,parentPlanName,parentPlanNo,owner,ownerInfo,ownerDept,ownerRealName,implementation,assigner,assignerInfo,assignTime,assignTimeStart,assignTimeEnd,launcher,launcherInfo,launchTime,creator,planLevel,planLevelInfo,planLevelName,progressRate,planStartTime,planStartTimeStart,planStartTimeEnd,planEndTime,planEndTimeStart,planEndTimeEnd,workTime,workTimeReference,actualStartTime,actualStartTimeStart,actualStartTimeEnd,actualEndTime,actualEndTimeStart,actualEndTimeEnd,remark,status,projectStatus,flowStatus,milestone,isAssignBack,isAssignSingleBack,isChangeSingleBack,milestoneName,preposeIds,preposeNos,preposePlans,documents,parentStorey,storeyNo,beforePlanId,feedbackProcInstId,feedbackRateBefore,planSource,fromTemplate,flowResolveXml,opContent,planType,taskType,taskNameType,cellId,required,order,result,flowFlag,_parentId,rescLinkInfoList,deliInfoList,inputList,preposeList,invalidTime,deliverablesName,planTemplateId,isNecessary,planViewInfoId,planReceivedProcInstId,serialVersionUID,bizId,bizVersion,policy,bizCurrent,avaliable,firstBy,firstName,firstFullName,firstOrgId,firstTime,branchIterationId,lastIterationId,latestIteration,serialVersionUID,id,createBy,createName,createFullName,createTime,updateBy,updateName,updateFullName,updateTime,deleteFlag,ext1,ext2,ext3,createOrgId,updateOrgId,securityLevel,glObjParentId,parentSysTableId,parentObjectName,parentObjectBizId,containerPath,instanceEntity');
insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('4028f0066d48f35e016d48fec1fc0012', null, 'com.glaway.ids.project.menu.entity.Project', 'ids-pm-service', '', '4028f0066d48f35e016d48f864450001', '', '1', 'name,projectNumber,status,startProjectTime,endProjectTime,projectTimeType,projectTemplate,remark,queryBefStartProjTime,queryAftStartProjTime,queryBefEndProjTime,queryAftEndProjTime,prevLifeStatus,epsInfo,eps,epsName,phase,phaseInfo,process,isRefuse,projectManagers,projectManagerNames,flowFlag,creator,procInstId,closeTime,pauseTime,serialVersionUID,bizId,bizVersion,policy,bizCurrent,avaliable,firstBy,firstName,firstFullName,firstOrgId,firstTime,branchIterationId,lastIterationId,latestIteration,serialVersionUID,id,createBy,createName,createFullName,createTime,updateBy,updateName,updateFullName,updateTime,deleteFlag,ext1,ext2,ext3,createOrgId,updateOrgId,securityLevel,glObjParentId,parentSysTableId,parentObjectName,parentObjectBizId,containerPath,instanceEntity');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f00b6dec4df3016decee50b70010', null, '9', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'TC001.selectPreposePlan()', '15', '前置计划', 'preposeIds', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096001b', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '0', '计划名称', 'planName', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096011b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.plan.entity.Plan', '', '1', '项目', 'projectName', 'TC001.projectName()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096022b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '20', '项目经理', 'projectManagerNames', 'TC001.projectManagerNames()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096033b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '2', '项目开始时间', 'startProjectTime', 'TC001.startProjectTime()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096044b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '3', '项目结束时间', 'endProjectTime', 'TC001.endProjectTime()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096055b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '4', '项目分类', 'eps', 'TC001.eps()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096066b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '5', '项目进度', 'process', 'TC001.process()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d384b5001c', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.ownerChange()"', '6', '负责人', 'owner', '', '0', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d459ee001d', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '7', '计划等级', 'planLevel', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d62bce001e', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '8', '责任部门', 'ownerDept', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afc0115', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '23', '计划进度', 'progressRate', 'TC001.progressRate()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d9493e001f', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.workTimeLinkage(''workTime'')"', '9', '工期（天）', 'workTime', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19deb7580020', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.milestoneChange()"', '10', '里程碑', 'milestone', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19dff0a30021', null, '4', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.workTimeLinkage(''planStartTime'')"', '11', '开始时间', 'planStartTime', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19dff0a60022', null, '4', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.workTimeLinkage(''planEndTime'')"', '12', '结束时间', 'planEndTime', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36af80023', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '13', '计划类型', 'taskNameType', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afa0024', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '14', '计划类别', 'taskType', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afc0025', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '17', '状态', 'bizCurrent', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afa0224', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '16', '上级计划', 'parentPlanId', 'TC001.parentPlanId()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afe0026', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '18', '创建者', 'createFullName', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b010027', null, '4', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '19', '创建时间', 'createTime', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b010117', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '21', '下达人', 'assigner', 'TC001.assigner()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b010227', null, '4', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '22', '下达时间', 'assignTime', 'TC001.assignTime()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b030028', null, '1', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '24', '备注说明', 'remark', '', '0', 0, '1');



--输入--

delete from pm_data_source_Object where id='297e02af6d18ed7d016d18f1fd220113';
delete from pm_object_property_info where DATASOURCEID='297e02af6d18ed7d016d18f1fd220113';

insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('297e02af6d18ed7d016d18f1fd220113', null, 'com.glaway.ids.project.plan.entity.Inputs', 'ids-pm-service', '', '297e02af6d18ed7d016d18f191460112', '', '1', 'name,useObjectType,useObjectId,fileId,origin,required,docId,docName,originObjectId,originObjectName,originObjectNameShow,originDeliverablesInfoId,fileType,versionCode,originDeliverablesInfoName,checked,document,formId,originType,originTypeExt,tempId,havePower,download,detail,securityLeve,docNameShow,originPath,docIdShow,matchFlag,serialVersionUID,id,createBy,createName,createFullName,createTime,updateBy,updateName,updateFullName,updateTime,deleteFlag,ext1,ext2,ext3,createOrgId,updateOrgId,securityLevel,glObjParentId,parentSysTableId,parentObjectName,parentObjectBizId,containerPath,instanceEntity');

insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('297e02af6dce09e3016dd2a38f670004', null, 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', 'ids-rdflow-service', '', '297e02af6d18ed7d016d18f191460112', '', '1', 'cellId,userObjectId,cellName,deliverId,deliverName,deliverType,originType,inputOrOutputName,fromDeliverId,isNameStandardDefault,isOutputDeliverRequired,fileName,filePath,checked,originObjectId,originObjectName,serialVersionUID,bizId,bizVersion,policy,bizCurrent,avaliable,firstBy,firstName,firstFullName,firstOrgId,firstTime,branchIterationId,lastIterationId,latestIteration,serialVersionUID,id,createBy,createName,createFullName,createTime,updateBy,updateName,updateFullName,updateTime,deleteFlag,ext1,ext2,ext3,createOrgId,updateOrgId,securityLevel,glObjParentId,parentSysTableId,parentObjectName,parentObjectBizId,containerPath,instanceEntity');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f00d6db8e701016dbdf5e859000a', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.addPLM()', '0', 'PLM系统', '/', 'TC002.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b71231', null, '7', '297e02af6d18ed7d016d18f1fd220113', 'TC002.showFileType', 'com.glaway.ids.project.plan.entity.Inputs', '', '11', '类型', 'fileType', 'TC002.isShowColumn()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b70019', null, '7', '297e02af6d18ed7d016d18f1fd220113', '', 'com.glaway.ids.project.plan.entity.Inputs', '', '13', '版本', 'versionCode', 'TC002.isShowColumn()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd29396940002', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.openAddInnerInput()', '2', '新增内部输入', '/', 'TC002.isShowForTemplate()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd29396f40003', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.openAddOutInput()', '3', '新增外部输入', '/', 'TC002.isShowForTemplate()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341510004', null, '6', '297e02af6d18ed7d016d18f1fd220113', '', 'com.glaway.ids.project.plan.entity.Inputs', '', '8', '', '', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b20005', null, '7', '297e02af6d18ed7d016d18f1fd220113', 'TC002.addNameLink', 'com.glaway.ids.project.plan.entity.Inputs', '', '9', '名称', 'name', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b50006', null, '7', '297e02af6d18ed7d016d18f1fd220113', 'TC002.addLink', 'com.glaway.ids.project.plan.entity.Inputs', '', '10', '文档', 'docNameShow', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b70007', null, '7', '297e02af6d18ed7d016d18f1fd220113', 'TC002.showOrigin', 'com.glaway.ids.project.plan.entity.Inputs', '', '12', '来源', 'originPath', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a97bd860007', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.addLocalFile()', '4', '新增本地文档', '/', 'TC002.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a97bd880008', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-minus', '/', 'TC002.deleteSelectionsInputs(''inputsList'', ''inputsController.do?doBatchDelInputs'')', '5', '删除', '/', 'TC002.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a97bd8b0009', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-pddata', '/', 'TC002.goProjLibLink()', '6', '项目库关联', '/', 'TC002.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a97bd8d000a', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-search', '/', 'TC002.goPlanLink()', '7', '计划关联', '/', 'TC002.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a8ce42e00a1', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.addInputsNew()', '1', '新增输入项', '/', 'TC002.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd30af1720006', null, '6', '297e02af6dce09e3016dd2a38f670004', '', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '14', '', '', '', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd30af19c0007', null, '7', '297e02af6dce09e3016dd2a38f670004', '', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '15', '输入项名称', 'deliverName', 'TC002.isShowColumnForTemplate()', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd30af19e0008', null, '7', '297e02af6dce09e3016dd2a38f670004', 'TC002.originObjectName', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '16', '来源', 'ext1', 'TC002.isShowColumnForTemplate()', '3', 1, '1');

--输出--
delete from pm_data_source_Object where id ='297e02af6d4e03d1016d4e1aa4510224';
delete from pm_object_property_info where DATASOURCEID='297e02af6d4e03d1016d4e1aa4510224';
insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('297e02af6d4e03d1016d4e1aa4510224', null, 'com.glaway.ids.project.plan.entity.DeliverablesInfo', 'ids-common-service', '', '297e02af6d4e03d1016d4e1aa44e0223', '', '1', 'name,useObjectType,useObjectId,useObjectCellId,useObjectName,useObjectTempId,document,fileId,origin,required,result,checked,docId,docName,formId,havePower,download,detail,serialVersionUID,bizId,bizVersion,policy,bizCurrent,avaliable,firstBy,firstName,firstFullName,firstOrgId,firstTime,branchIterationId,lastIterationId,latestIteration,serialVersionUID,id,createBy,createName,createFullName,createTime,updateBy,updateName,updateFullName,updateTime,deleteFlag,ext1,ext2,ext3,createOrgId,updateOrgId,securityLevel,glObjParentId,parentSysTableId,parentObjectName,parentObjectBizId,containerPath,instanceEntity');

insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('297e02af6dce09e3016dd30bd4160009', null, 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', 'ids-rdflow-service', '', '297e02af6d4e03d1016d4e1aa44e0223', '', '1', 'cellId,userObjectId,cellName,deliverId,deliverName,deliverType,originType,inputOrOutputName,fromDeliverId,isNameStandardDefault,isOutputDeliverRequired,fileName,filePath,checked,originObjectId,originObjectName,serialVersionUID,bizId,bizVersion,policy,bizCurrent,avaliable,firstBy,firstName,firstFullName,firstOrgId,firstTime,branchIterationId,lastIterationId,latestIteration,serialVersionUID,id,createBy,createName,createFullName,createTime,updateBy,updateName,updateFullName,updateTime,deleteFlag,ext1,ext2,ext3,createOrgId,updateOrgId,securityLevel,glObjParentId,parentSysTableId,parentObjectName,parentObjectBizId,containerPath,instanceEntity');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f00d6db8e701016dbddc7dac0007', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-object_relation', '/', 'TC003.addPLM()', '0', 'PLM系统关联', '/', 'TC003.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1a16d4e1aa4860027', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.addFlagLink', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '10', '挂载物', 'deleteFlag', 'TC003.isShowColumn()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1ef39c016d1efa723500011', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-copy', '/', 'TC003.inheritParent()', '1', '继承父项', '/', 'TC003.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa4860027', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.getStatus', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '12', '状态', 'ext3', 'TC003.isShowColumn()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa48a0028', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-search', '/', 'TC003.checkLine()', '6', '从项目库查找挂接', '/', 'TC003.isShowButton()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa48e0029', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-minus', '/', 'TC003.deleteSelections(''deliverablesInfoList'', ''deliverablesInfoController.do?doBatchDel'')', '3', '删除', '/', 'TC003.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa492002a', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-send', '/', 'TC003.uploadLine()', '5', '本地上传挂接', '/', 'TC003.isShowButton()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa497002b', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-submitted_approval', '/', 'TC003.submitLine()', '4', '提交', '/', 'TC003.isShowButton()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa49a002c', null, '6', '297e02af6d4e03d1016d4e1aa4510224', '', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '7', '', '', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa49e002d', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.getDeliverableName', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '8', '交付项名称', 'ext1', '', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa4a1002e', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.addLink', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '9', '文档 ', 'docName', 'TC003.isShowColumn()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa4a4002f', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.getVersion', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '11', '版本', 'ext2', 'TC003.isShowColumn()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1ef39c016d1efa72350003', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-plus', '/', 'TC003.addDeliverable()', '2', '新增', '/', 'TC003.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd310ee5d000a', null, '6', '297e02af6dce09e3016dd30bd4160009', '', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '13', '', '', '', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd310ee5f000b', null, '7', '297e02af6dce09e3016dd30bd4160009', '', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '14', '交付项名称', 'deliverName', '', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd310ee61000c', null, '7', '297e02af6dce09e3016dd30bd4160009', 'TC003.isNecessary', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '15', '必要', 'isOutputDeliverRequired', '', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd310ee63000d', null, '7', '297e02af6dce09e3016dd30bd4160009', 'TC003.inputOrOutputName', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '16', '去向 ', 'ext2', '', '3', 1, '1');

--资源--
delete from pm_data_source_Object where id='297e02af6d3cb69c016d3e4f328d0312';
delete from pm_object_property_info where DATASOURCEID='297e02af6d3cb69c016d3e4f328d0312';

insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('297e02af6d3cb69c016d3e4f328d0312', null, 'com.glaway.ids.common.entity.ResourceLinkInfo', 'ids-common-service', '', '297e02af6d1e0823016d1ec0a223038d', '', '1', 'resourceId,resourceInfo,useObjectType,useObjectId,useObjectTempId,startTime,endTime,paramTime,useRate,resourceName,resourceType,serialVersionUID,bizId,bizVersion,policy,bizCurrent,avaliable,firstBy,firstName,firstFullName,firstOrgId,firstTime,branchIterationId,lastIterationId,latestIteration,serialVersionUID,id,createBy,createName,createFullName,createTime,updateBy,updateName,updateFullName,updateTime,deleteFlag,ext1,ext2,ext3,createOrgId,updateOrgId,securityLevel,glObjParentId,parentSysTableId,parentObjectName,parentObjectBizId,containerPath,instanceEntity');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf41008f', null, '8', '297e02af6d3cb69c016d3e4f328d0312', 'basis ui-icon-plus', '/', 'TC004.addResource()', '0', '新增', '/', 'TC004.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf430090', null, '8', '297e02af6d3cb69c016d3e4f328d0312', 'basis ui-icon-minus', '/', 'TC004.deleteSelections2(''resourceList'', ''resourceLinkInfoController.do?doBatchDel'')', '1', '删除', '/', 'TC004.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf450091', null, '6', '297e02af6d3cb69c016d3e4f328d0312', '', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '2', '', '', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf480092', null, '7', '297e02af6d3cb69c016d3e4f328d0312', 'TC004.resourceNameLink', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '3', '资源名称', 'resourceName', '', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf4a0093', null, '7', '297e02af6d3cb69c016d3e4f328d0312', '', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '4', '分类', 'resourceType', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf4d0094', null, '7', '297e02af6d3cb69c016d3e4f328d0312', '', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '5', '开始时间', 'startTime', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf4f0095', null, '7', '297e02af6d3cb69c016d3e4f328d0312', '', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '6', '结束时间', 'endTime', '', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf500096', null, '7', '297e02af6d3cb69c016d3e4f328d0312', 'TC004.viewUseRate2', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '7', '使用百分比', 'useRate', '', '', 0, '1');


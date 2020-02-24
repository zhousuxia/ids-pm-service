
--基本信息 PM_TAB_TEMPLATE
insert into PM_TAB_TEMPLATE (ID, SECURITYLEVEL, CODE, DISPLAYUSAGE, EXTERNALURL, NAME, REMAKE, SOURCE, STOPFLAG, TABTYPE, AVALIABLE)
values ('4028f0066d48f35e016d48f864450001', null, 'TC001', '1', '', '基本信息', '基本信息', '0', '1', '1', '1');
--输入 PM_TAB_TEMPLATE
insert into PM_TAB_TEMPLATE (ID, SECURITYLEVEL, CODE, DISPLAYUSAGE, EXTERNALURL, NAME, REMAKE, SOURCE, STOPFLAG, TABTYPE, AVALIABLE)
values ('297e02af6d18ed7d016d18f191460112', null, 'TC002', '1', '', '输入', '输入', '0', '1', '1', '1');

--输出 PM_TAB_TEMPLATE
insert into PM_TAB_TEMPLATE (ID, SECURITYLEVEL, CODE, DISPLAYUSAGE, EXTERNALURL, NAME, REMAKE, SOURCE, STOPFLAG, TABTYPE, AVALIABLE)
values ('297e02af6d4e03d1016d4e1aa44e0223', null, 'TC003', '1', '', '输出', '输出', '0', '1', '1', '1');

--资源 PM_TAB_TEMPLATE
insert into PM_TAB_TEMPLATE (ID, SECURITYLEVEL, CODE, DISPLAYUSAGE, EXTERNALURL, NAME, REMAKE, SOURCE, STOPFLAG, TABTYPE, AVALIABLE)
values ('297e02af6d1e0823016d1ec0a223038d', null, 'TC004', '1', '', '资源', '资源', '0', '1', '1', '1');

--基本信息pm_data_source_Object
insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('4028f0066d48f35e016d48f8d5ab0011', null, 'com.glaway.ids.project.plan.entity.Plan', 'ids-pm-service', '', '4028f0066d48f35e016d48f864450001', '', '1', '');

insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('4028f0066d48f35e016d48fec1fc0012', null, 'com.glaway.ids.project.menu.entity.Project', 'ids-pm-service', '', '4028f0066d48f35e016d48f864450001', '', '1', '');

--输入pm_data_source_Object
insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('297e02af6d18ed7d016d18f1fd220113', null, 'com.glaway.ids.project.plan.entity.Inputs', 'ids-pm-service', '', '297e02af6d18ed7d016d18f191460112', '', '1', '');

--输出pm_data_source_Object
insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('297e02af6d4e03d1016d4e1aa4510224', null, 'com.glaway.ids.project.plan.entity.DeliverablesInfo', 'ids-common-service', '', '297e02af6d4e03d1016d4e1aa44e0223', '', '1', '');

--资源pm_data_source_Object
insert into pm_data_source_Object (ID, SECURITYLEVEL, OBJECTPATH, PROJECTMODEL, RESULTSQL, TABID, TABLENAME, AVALIABLE, OBJECTMODELPROPERTY)
values ('297e02af6d3cb69c016d3e4f328d0312', null, 'com.glaway.ids.common.entity.ResourceLinkInfo', 'ids-common-service', '', '297e02af6d1e0823016d1ec0a223038d', '', '1', '');


--基本信息pm_object_property_info
insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096001b', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '0', '计划名称', 'planName', '1', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096011b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.plan.entity.Plan', '', '1', '项目', 'projectName', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096022b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '20', '项目经理', 'projectManagerNames', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096033b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '2', '项目开始时间', 'startProjectTime', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096044b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '3', '项目结束时间', 'endProjectTime', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096055b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '4', '项目分类', 'eps', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d1e096066b', null, '0', '4028f0066d48f35e016d48fec1fc0012', '', 'com.glaway.ids.project.menu.entity.Project', '', '5', '项目进度', 'process', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d384b5001c', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.ownerChange()"', '6', '负责人', 'owner', '1', '0', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d459ee001d', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '7', '计划等级', 'planLevel', '1', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d62bce001e', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '8', '责任部门', 'ownerDept', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afc0115', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '23', '计划进度', 'progressRate', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19d9493e001f', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.workTimeLinkage(''workTime'')"', '9', '工期（天）', 'workTime', '1', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19deb7580020', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.milestoneChange()"', '10', '里程碑', 'milestone', '1', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19dff0a30021', null, '4', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.workTimeLinkage(''planStartTime'')"', '11', '开始时间', 'planStartTime', '1', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19dff0a60022', null, '4', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', 'onChange="TC001.workTimeLinkage(''planEndTime'')"', '12', '结束时间', 'planEndTime', '1', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36af80023', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '13', '计划类型', 'taskNameType', '1', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afa0024', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '14', '计划类别', 'taskType', '1', '0', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afc0025', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '17', '状态', 'bizCurrent', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afa0114', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '15', '前置计划', 'preposeIds', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afa0224', null, '5', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '16', '上级计划', 'parentPlanId', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36afe0026', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '18', '创建者', 'createFullName', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b010027', null, '4', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '19', '创建时间', 'createTime', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b010117', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '21', '下达人', 'assigner', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b010227', null, '4', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '22', '下达时间', 'assignTime', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b030028', null, '1', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '24', '备注说明', 'remark', '1', '0', 0, '1');

--输入pm_object_property_info
insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341510004', null, '6', '297e02af6d18ed7d016d18f1fd220113', '', 'com.glaway.ids.project.plan.entity.Inputs', '', '7', '', '', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b20005', null, '7', '297e02af6d18ed7d016d18f1fd220113', '', 'com.glaway.ids.project.plan.entity.Inputs', '', '8', '名称', 'name', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b50006', null, '7', '297e02af6d18ed7d016d18f1fd220113', 'TC002.addLink', 'com.glaway.ids.project.plan.entity.Inputs', '', '9', '文档', 'docNameShow', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b70007', null, '7', '297e02af6d18ed7d016d18f1fd220113', 'TC002.showOrigin', 'com.glaway.ids.project.plan.entity.Inputs', '', '10', '来源', 'originPath', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a97bd860007', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.addLocalFile()', '3', '新增本地文档', '/', '0', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a97bd880008', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-minus', '/', 'TC002.deleteSelectionsInputs(''inputsList'', ''inputsController.do?doBatchDelInputs'')', '4', '删除', '/', '0', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a97bd8b0009', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-pddata', '/', 'TC002.goProjLibLink()', '5', '项目库关联', '/', '0', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a97bd8d000a', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-search', '/', 'TC002.goPlanLink()', '6', '计划关联', '/', '0', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1a664d016d1a8ce42e00a1', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.addInputsNew()', '0', '新增输入项', '/', '0', '3', 0, '1');

--输出pm_object_property_info
insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1ef39c016d1efa723500011', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-copy', '/', 'TC003.inheritParent()', '0', '继承父项', '/', '0', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa4860027', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.getStatus', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '9', '状态', 'ext3', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa48a0028', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-search', '/', 'TC003.checkLine()', '4', '从项目库查找挂接', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa48e0029', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-minus', '/', 'TC003.deleteSelections(''deliverablesInfoList'', ''deliverablesInfoController.do?doBatchDel'')', '2', '删除', '/', '0', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa492002a', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-send', '/', 'TC003.uploadLine()', '3', '本地上传挂接', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa497002b', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-submitted_approval', '/', 'TC003.submitLine()', '2', '提交', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa49a002c', null, '6', '297e02af6d4e03d1016d4e1aa4510224', '', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '5', '', '', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa49e002d', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.getDeliverableName', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '6', '交付项名称', 'ext1', '1', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa4a1002e', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.addLink', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '7', '文档 ', 'docName', '1', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d4e03d1016d4e1aa4a4002f', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.getVersion', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '8', '版本', 'ext2', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1ef39c016d1efa72350003', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-plus', '/', 'TC003.addDeliverable()', '1', '新增', '/', '0', '3', 0, '1');

--资源pm_object_property_info
insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf41008f', null, '8', '297e02af6d3cb69c016d3e4f328d0312', 'basis ui-icon-plus', '/', 'TC004.addResource()', '0', '新增', '/', '0', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf430090', null, '8', '297e02af6d3cb69c016d3e4f328d0312', 'basis ui-icon-minus', '/', 'TC004.deleteSelections2(''resourceList'', ''resourceLinkInfoController.do?doBatchDel'')', '1', '删除', '/', '0', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf450091', null, '6', '297e02af6d3cb69c016d3e4f328d0312', '', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '2', '', '', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf480092', null, '7', '297e02af6d3cb69c016d3e4f328d0312', 'TC004.resourceNameLink', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '3', '资源名称', 'resourceName', '1', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf4a0093', null, '7', '297e02af6d3cb69c016d3e4f328d0312', '', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '4', '分类', 'resourceType', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf4d0094', null, '7', '297e02af6d3cb69c016d3e4f328d0312', '', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '5', '开始时间', 'startTime', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf4f0095', null, '7', '297e02af6d3cb69c016d3e4f328d0312', '', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '6', '结束时间', 'endTime', '1', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1e0823016d1eebaf500096', null, '7', '297e02af6d3cb69c016d3e4f328d0312', 'TC004.viewUseRate2', 'com.glaway.ids.common.entity.ResourceLinkInfo', '', '7', '使用百分比', 'useRate', '1', '', 0, '1');

--- 问题；
delete from pm_object_property_info where datasourceid = '4028f0066d1de316016d1f0233bd0005';    

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f0688220006', null, '8', '4028f0066d1de316016d1f0233bd0005', 'basis ui-icon-plus', '/', 'createAddTaskDialogForPlan()', '0', '新增', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f0688260007', null, '6', '4028f0066d1de316016d1f0233bd0005', '', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '5', '问题列表', '', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f0688280008', null, '7', '4028f0066d1de316016d1f0233bd0005', 'formatProblemName', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '6', '问题', 'problemName', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f06882c0009', null, '7', '4028f0066d1de316016d1f0233bd0005', '', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '7', '风险', 'riskName', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f06882e000a', null, '7', '4028f0066d1de316016d1f0233bd0005', '', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '8', '描述', 'description', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f068831000b', null, '7', '4028f0066d1de316016d1f0233bd0005', 'formatcreateName', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '9', '登记人', 'createName', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f068833000c', null, '7', '4028f0066d1de316016d1f0233bd0005', 'dateFormatterStr', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '10', '登记时间', 'createTime', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f068836000d', null, '7', '4028f0066d1de316016d1f0233bd0005', 'formatProblemStatus', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '2', '状态', 'bizCurrent', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3e2837790058', null, '8', '4028f0066d1de316016d1f0233bd0005', 'basis ui-icon-pencil', '/', 'updateProblemLineForPlan()', '1', '修改', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3e28377c0059', null, '8', '4028f0066d1de316016d1f0233bd0005', 'basis ui-icon-minus', '/', 'deleteProblemLineForPlan()', '3', '删除', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3e28377d005a', null, '8', '4028f0066d1de316016d1f0233bd0005', 'basis ui-icon-submitted_approval', '/', 'assignProblemLineForPlan()', '4', '提交', '/', '2', '3', 0, '1');

--风险清单
delete from pm_object_property_info where datasourceid = '4028f0066d3cc740016d3f43c5ff0094';    

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a18650097', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-minus', '/', 'deleteRiskMassForPlan()', '8', '删除', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a18670098', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-import', '/', 'importRiskForPlan()', '9', '导入', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a18690099', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-export', '/', 'exportRiskForPlan()', '10', '导出', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a186b009a', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-discussion', '/', 'acccessRiskForPlan()', '11', '评估', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a186c009b', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-recovery', '/', 'openRiskForPlan()', '12', '开启', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a186e009c', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-close', '/', 'closeRiskForPlan()', '13', '关闭', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a186f009d', null, '6', '4028f0066d3cc740016d3f43c5ff0094', '', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '14', '风险清单列表', '', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a1871009e', null, '7', '4028f0066d3cc740016d3f43c5ff0094', '', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '15', '风险名称', 'name', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a1872009f', null, '7', '4028f0066d3cc740016d3f43c5ff0094', 'formatownerName', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '2', '负责人', 'createName', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187400a0', null, '7', '4028f0066d3cc740016d3f43c5ff0094', 'formatAssess', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '3', '评估', 'ext1', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187500a1', null, '7', '4028f0066d3cc740016d3f43c5ff0094', 'formatMeasure', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '4', '减缓措施', 'ext2', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187700a2', null, '7', '4028f0066d3cc740016d3f43c5ff0094', 'formatUnCloseProblem', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '5', '未关闭问题', 'ext3', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187800a3', null, '7', '4028f0066d3cc740016d3f43c5ff0094', '', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '6', '状态', 'status', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187a00a4', null, '7', '4028f0066d3cc740016d3f43c5ff0094', '', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '7', '备注', 'remark', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a185a0095', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-plus', '/', 'addRiskForPlan()', '0', '新增', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a18640096', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-pencil', '/', 'modifyRiskForPlan()', '1', '修改', '/', '2', '3', 0, '1');

------
--组合模版修改：
--（研发）
delete from pm_combination_template_info where tabcombinationtemplateid = '4028f00b6d1df353016d1e58fcc40002';   

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('4028f00b6d7199a0016d71ccf80c0012', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.18.41.292000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '基本信息', 0, '4028f00b6d1df353016d1e58fcc40002', '4028f0066d48f35e016d48f864450001');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('4028f00b6d7199a0016d71ccf80e0013', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.18.41.292000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '输入', 1, '4028f00b6d1df353016d1e58fcc40002', '297e02af6d18ed7d016d18f191460112');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('4028f00b6d7199a0016d71ccf80e0014', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.18.41.292000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '输出', 2, '4028f00b6d1df353016d1e58fcc40002', '297e02af6d4e03d1016d4e1aa44e0223');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('4028f00b6d7199a0016d71ccf80e0015', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.18.41.292000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '资源', 3, '4028f00b6d1df353016d1e58fcc40002', '297e02af6d1e0823016d1ec0a223038d');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('4028f00b6d7199a0016d71ccf80f0016', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.18.41.292000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '2', '参考', 4, '4028f00b6d1df353016d1e58fcc40002', '297e02af6d435655016d47610af8000d');

--（风险）
delete from pm_combination_template_info where tabcombinationtemplateid = '4028f0066d1faaa3016d1fafe7220000'; 

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d6d5c9a016d709d20f30069', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 10.46.48.819000 上午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '基本信息', 0, '4028f0066d1faaa3016d1fafe7220000', '4028f0066d48f35e016d48f864450001');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d6d5c9a016d709d20f6006a', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 10.46.48.819000 上午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '输入', 1, '4028f0066d1faaa3016d1fafe7220000', '297e02af6d18ed7d016d18f191460112');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d6d5c9a016d709d20f6006b', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 10.46.48.819000 上午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '输出', 2, '4028f0066d1faaa3016d1fafe7220000', '297e02af6d4e03d1016d4e1aa44e0223');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d6d5c9a016d709d20f7006c', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 10.46.48.819000 上午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '资源', 3, '4028f0066d1faaa3016d1fafe7220000', '297e02af6d1e0823016d1ec0a223038d');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d6d5c9a016d709d20f7006d', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 10.46.48.819000 上午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '2', '参考', 4, '4028f0066d1faaa3016d1fafe7220000', '297e02af6d435655016d47610af8000d');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d6d5c9a016d709d20f7006e', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 10.46.48.819000 上午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '2', '问题', 5, '4028f0066d1faaa3016d1fafe7220000', '4028f0066d1de316016d1f0215b60004');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d6d5c9a016d709d20f7006f', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 10.46.48.819000 上午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '2', '风险清单', 6, '4028f0066d1faaa3016d1fafe7220000', '4028f0066d3cc740016d3f43adbf0093');

--（评审）
delete from pm_combination_template_info where tabcombinationtemplateid = '4028f00d6d42f2ea016d431634eb0000'; 

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d71acc5016d71e9b0b00026', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.50.03.565000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '输入', 1, '4028f00d6d42f2ea016d431634eb0000', '297e02af6d18ed7d016d18f191460112');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d71acc5016d71e9b0b00027', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.50.03.565000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '输出', 2, '4028f00d6d42f2ea016d431634eb0000', '297e02af6d4e03d1016d4e1aa44e0223');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d71acc5016d71e9b0b10028', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.50.03.565000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '1', '资源', 3, '4028f00d6d42f2ea016d431634eb0000', '297e02af6d1e0823016d1ec0a223038d');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d71acc5016d71e9b0b10029', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.50.03.565000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '2', '评审活动', 4, '4028f00d6d42f2ea016d431634eb0000', '4028f00d6d3cc51a016d3ddef9ba0000');

insert into pm_combination_template_info (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, DISPLAYACCESS, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6d71acc5016d71e9b0b1002a', '', '4028ef506b08a241016b25649af70082', '卢浩然', 'lhr', '4028ef506af7fb98016b06b40a95004d', '27-9月 -19 04.50.03.565000 下午', '', '', '', '', '', '', '', '', '', null, '', '', '', '', '', '2', '基本信息', 5, '4028f00d6d42f2ea016d431634eb0000', '4028f0066d48f35e016d48f864450000');






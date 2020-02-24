--基本信息pm_object_property_info
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
values ('sd7e02af6d18ed7d016d19e36af80023', null, '10', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '13', '计划类型', 'taskNameType', '', '3', 0, '1');

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
values ('sd7e02af6d18ed7d016d19e36b010117', null, '0', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '21', '发布人', 'assigner', 'TC001.assigner()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b010227', null, '4', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '22', '发布时间', 'assignTime', 'TC001.assignTime()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d19e36b030028', null, '1', '4028f0066d48f35e016d48f8d5ab0011', '', 'com.glaway.ids.project.plan.entity.Plan', '', '24', '备注说明', 'remark', '', '0', 0, '1');

--输入pm_object_property_info

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f00d6db8e701016dbdf5e859000a', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.addPLM()', '2', '关联PLM系统', '/', 'TC002.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b71231', null, '7', '297e02af6d18ed7d016d18f1fd220113', 'TC002.showFileType', 'com.glaway.ids.project.plan.entity.Inputs', '', '11', '类型', 'fileType', 'TC002.isShowColumn()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d18ed7d016d18f341b70019', null, '7', '297e02af6d18ed7d016d18f1fd220113', '', 'com.glaway.ids.project.plan.entity.Inputs', '', '13', '版本', 'versionCode', 'TC002.isShowColumn()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd29396940002', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.openAddInnerInput()', '1', '新增内部输入', '/', 'TC002.isShowForTemplate()', '3', 0, '1');

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
values ('sd7e02af6d1a664d016d1a8ce42e00a1', null, '8', '297e02af6d18ed7d016d18f1fd220113', 'basis ui-icon-plus', '/', 'TC002.addInputsNew()', '0', '新增输入项', '/', 'TC002.isEditor()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd30af1720006', null, '6', '297e02af6dce09e3016dd2a38f670004', '', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '14', '', '', '', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd30af19c0007', null, '7', '297e02af6dce09e3016dd2a38f670004', '', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '15', '输入项名称', 'deliverName', 'TC002.isShowColumnForTemplate()', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd30af19e0008', null, '7', '297e02af6dce09e3016dd2a38f670004', 'TC002.originObjectName', 'com.glaway.ids.rdflow.task.entity.TaskCellDeliverItem', '', '16', '来源', 'ext1', 'TC002.isShowColumnForTemplate()', '3', 1, '1');


--输出pm_object_property_info
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

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd310ee61aaac', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.isNecessary', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '17', '必要', 'required', 'TC003.isShowColumn()', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('297e02af6dce09e3016dd310ee63aaad', null, '7', '297e02af6d4e03d1016d4e1aa4510224', 'TC003.inputOrOutputName', 'com.glaway.ids.project.plan.entity.DeliverablesInfo', '', '18', '去向 ', 'orginType', 'TC003.isShowColumn()', '3', 0, '1');



--资源pm_object_property_info
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

--- 问题
insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f0688220006', null, '8', '4028f0066d1de316016d1f0233bd0005', 'basis ui-icon-plus', '/', 'createAddTaskDialogForPlan()', '0', '新增', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f0688260007', null, '6', '4028f0066d1de316016d1f0233bd0005', '', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '4', '问题列表', '', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f0688280008', null, '7', '4028f0066d1de316016d1f0233bd0005', 'formatProblemName', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '5', '问题', 'problemName', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f06882c0009', null, '7', '4028f0066d1de316016d1f0233bd0005', '', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '6', '风险', 'riskName', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f06882e000a', null, '7', '4028f0066d1de316016d1f0233bd0005', '', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '7', '描述', 'description', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f068831000b', null, '7', '4028f0066d1de316016d1f0233bd0005', 'formatcreateName', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '8', '登记人', 'createName', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f068833000c', null, '7', '4028f0066d1de316016d1f0233bd0005', 'dateFormatterStr', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '9', '登记时间', 'createTime', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d1de316016d1f068836000d', null, '7', '4028f0066d1de316016d1f0233bd0005', 'formatProblemStatus', 'com.glaway.ids.riskproblems.problems.entity.RiskProblemInfo', '', '10', '状态', 'bizCurrent', '2', '', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3e2837790058', null, '8', '4028f0066d1de316016d1f0233bd0005', 'basis ui-icon-pencil', '/', 'updateProblemLineForPlan()', '1', '修改', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3e28377c0059', null, '8', '4028f0066d1de316016d1f0233bd0005', 'basis ui-icon-minus', '/', 'deleteProblemLineForPlan()', '2', '删除', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3e28377d005a', null, '8', '4028f0066d1de316016d1f0233bd0005', 'basis ui-icon-submitted_approval', '/', 'assignProblemLineForPlan()', '3', '提交', '/', '2', '3', 0, '1');

--风险清单 
insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a185a0095', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-plus', '/', 'addRiskForPlan()', '0', '新增', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a18640096', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-pencil', '/', 'modifyRiskForPlan()', '1', '修改', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a18650097', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-minus', '/', 'deleteRiskMassForPlan()', '2', '删除', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a18670098', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-import', '/', 'importRiskForPlan()', '3', '导入', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a18690099', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-export', '/', 'exportRiskForPlan()', '4', '导出', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a186b009a', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-discussion', '/', 'acccessRiskForPlan()', '5', '评估', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a186c009b', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-enable', '/', 'openRiskForPlan()', '6', '开启', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a186e009c', null, '8', '4028f0066d3cc740016d3f43c5ff0094', 'basis ui-icon-close', '/', 'closeRiskForPlan()', '7', '关闭', '/', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a186f009d', null, '6', '4028f0066d3cc740016d3f43c5ff0094', '', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '8', '风险清单列表', '', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a1871009e', null, '7', '4028f0066d3cc740016d3f43c5ff0094', '', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '9', '风险名称', 'name', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a1872009f', null, '7', '4028f0066d3cc740016d3f43c5ff0094', 'formatownerName', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '10', '负责人', 'createName', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187400a0', null, '7', '4028f0066d3cc740016d3f43c5ff0094', 'formatAssess', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '11', '评估', 'ext1', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187500a1', null, '7', '4028f0066d3cc740016d3f43c5ff0094', 'formatMeasure', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '12', '减缓措施', 'ext2', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187700a2', null, '7', '4028f0066d3cc740016d3f43c5ff0094', 'formatUnCloseProblem', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '13', '未关闭问题', 'ext3', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187800a3', null, '7', '4028f0066d3cc740016d3f43c5ff0094', '', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '14', '状态', 'status', '2', '3', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066d3cc740016d3f4a187a00a4', null, '7', '4028f0066d3cc740016d3f43c5ff0094', '', 'com.glaway.ids.riskproblems.config.entity.RiskTarget', '', '15', '备注', 'remark', '2', '3', 0, '1');

--质量检查单
insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e162569016e169365700006', null, '0', '4028f0066e162569016e16844b390005', '', 'com.glaway.ids.qualityTest.CofigFormTest', '', '1', '检查单名', 'name', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e162569016e169365a40007', null, '3', '4028f0066e162569016e16844b390005', '', 'com.glaway.ids.qualityTest.CofigFormTest', '', '2', '检查人', 'checkPerson', '', '0', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e162569016e169365a70008', null, '5', '4028f0066e162569016e16844b390005', '', 'com.glaway.ids.qualityTest.CofigFormTest', '', '3', '检查周期', 'period', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e162569016e169365a90009', null, '0', '4028f0066e162569016e16844b390005', '', 'com.glaway.ids.qualityTest.CofigFormTest', '', '4', '结论', 'approve', 'TC009.approveShow()', '2', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e162569016e169365ab000a', null, '1', '4028f0066e162569016e16844b390005', '', 'com.glaway.ids.qualityTest.CofigFormTest', '', '5', '备注', 'remark', '', '0', 0, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e1fa73b016e20bee4d1002e', null, '8', '4028f0066e162569016e16844b390005', 'basis ui-icon-save', '/', 'TC009.savaData()', '0', '保存', '/', 'TC009.btnShow()', '3', 0, '1');

--质量检查项
insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e1a43b3016e1a6686740003', null, '6', '4028f0066e1a43b3016e1a4ed90a0001', '', 'com.glaway.ids.qualityTest.CofigDataGridTest', '', '1', '检查项列表', '', '', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e1a43b3016e1a6686770004', null, '7', '4028f0066e1a43b3016e1a4ed90a0001', '', 'com.glaway.ids.qualityTest.CofigDataGridTest', '', '2', '名称', 'name', '', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e1a43b3016e1a66867a0005', null, '7', '4028f0066e1a43b3016e1a4ed90a0001', '', 'com.glaway.ids.qualityTest.CofigDataGridTest', '', '3', '规则', 'rule', '', '3', 1, '1');

insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('4028f0066e1a43b3016e1a6685fe0002', null, '8', '4028f0066e1a43b3016e1a4ed90a0001', 'l-btn-icon basis ui-icon-plus', '/', 'TC010.addAction()', '0', '新增', '/', 'TC010.btnShow()', '3', 0, '1');


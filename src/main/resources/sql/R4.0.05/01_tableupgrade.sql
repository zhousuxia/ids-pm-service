--菜单创建
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506dbda131016dc83777090144', null, 2, '计划反馈引擎', '9', 'planFeedBackController.do?goPlanFeedBack'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506dbda131016dc8d920e001e3', null, 2, '研发流程模板', '8', 'procTemplateController.do?goList'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

--父子菜单关系
insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028ef506dbda131016dc8d80ddd01e1', '4028ef506dbda131016dc83777090144', '4028efec4cdf892b014cdf8e37b10005');

insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028ef506dbda131016dc8d920ff01e5', '4028ef506dbda131016dc8d920e001e3', '4028efec4cdf892b014cdf8e37b10005');

--研发流程模板&计划反馈引擎权限
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8d9c0b601e8', 'procTemplateAddCode', '', '新增', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8da3ecb01ee', 'procTemplateEnableCode', '', '启用', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8db25bf01f6', 'ProcTemplateCopyProcLineCode', '', '复制', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8db6a3d01f8', 'ProcTemplateSubmitProcLineCode', '', '提交', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dc1e0101fe', 'ProcTemplateBackMinorLineCode', '', '回退', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8da842401f0', 'procTemplateDisableCode', '', '禁用', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dab32f01f2', 'ProcTemplateUpdateProcLineCode', '', '修改', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8daed2301f4', 'ProcTemplateReviseProcLineCode', '', '修订', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dbbd6b01fa', 'procTemplateImport', '', '导入', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dbee5a01fc', 'ProcTemplateBackMajorLineCode', '', '撤消', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8da03ed01ec', 'procTemplateDeleteCode', '', '删除', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dc50280200', 'procCategoryAdd', '', '研发流程分类新增', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dc8b060202', 'procCategoryUpdate', '', '研发流程分类修改', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dcb89b0204', 'procCategoryDel', '', '研发流程分类删除', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc83835a60148', 'planFeedBackSave', '', '保存', null, '4028ef506dbda131016dc83777090144', '');

insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dcaea30016dceae4e1b0226', 'planConcern', null, '关注计划', null, '4028efed4e9a0a47014e9aad0dee000a', '');

insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dcaea30016dceae91a0022a', 'planUnconcern', null, '取消关注计划', null, '4028efed4e9a0a47014e9aad0dee000a', '');

--计划视图信息--
insert into PLANVIEW_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, ISDEFAULT, NAME, PUBLISHPERSON, PUBLISHTIME, STATUS)
values ('4028f00763ba3b2001a3ba63fd26000c', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'true', '关注计划', '4028ef354b5d7bb1014b5d7cffa10043', to_date('27-08-2017 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'PUBLIC');


--视图设置条件--
insert into PLANVIEW_SET_CONDITION (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, PLANVIEWINFOID)
values ('4028f00763b8df390163b9ad75441008', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028f00763ba3b2001a3ba63fd26000c');

--计划视图展示列信息--
insert into PLANVIEW_COLUMN_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, COLUMNID, PLANVIEWINFOID)
values ('4028f00763cd7cb70a63cea6fdb90018', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'planNo,planLevel,planType,planTaskType,status,owner,planStartTime,planEndTime,assigner,assignTime,workTime,preposePlan,mileStone,creator,createTime', '4028f00763ba3b2001a3ba63fd26000c');

--页签模板
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506cdadfef016cdff92cef017e', null, 2, '页签模板', '4', 'tabTemplateController.do?tabTemplateInfoManagePage'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', null);

insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028ef506cdadfef016cdffe74780196', '4028ef506cdadfef016cdff92cef017e', '4028efec4cdf892b014cdf8e37b10005');

--页签模板权限按钮
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdff9d7600182', 'addPlanTabTemplate', '', '新增', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffa04870184', 'deletePlanTabTemplate', '', '删除', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffa5b100186', 'starPlanTabTemplate', '', '启用', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffa987c0188', 'stopPlanTabTemplate', '', '禁用', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffb21b8018a', 'updatePlanTabTemplate', '', '更新', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffb6472018c', 'copyPlanTabTemplate', '', '复制', null, '4028ef506cdadfef016cdff92cef017e', '');


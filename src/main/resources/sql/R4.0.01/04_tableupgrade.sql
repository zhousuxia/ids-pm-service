--ҳǩģ��
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506cdadfef016cdff92cef017e', null, 2, 'ҳǩģ��', '4', 'tabTemplateController.do?tabTemplateInfoManagePage'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', null);

insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028ef506cdadfef016cdffe74780196', '4028ef506cdadfef016cdff92cef017e', '4028efec4cdf892b014cdf8e37b10005');

--ҳǩģ��Ȩ�ް�ť
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdff9d7600182', 'addPlanTabTemplate', '', '����', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffa04870184', 'deletePlanTabTemplate', '', 'ɾ��', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffa5b100186', 'starPlanTabTemplate', '', '����', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffa987c0188', 'stopPlanTabTemplate', '', '����', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffb21b8018a', 'updatePlanTabTemplate', '', '����', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cdadfef016cdffb6472018c', 'copyPlanTabTemplate', '', '����', null, '4028ef506cdadfef016cdff92cef017e', '');


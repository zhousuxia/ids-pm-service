--ҳǩ���ģ��
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506cd2741b016cd2ffef9800e4', null, 2, 'ҳǩ���ģ��', '6', 'tabCombinationTemplateController.do?goTabCombinationTemplate'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028ef506cd2741b016cd2ffefa400e6', '4028ef506cd2741b016cd2ffef9800e4', '4028efec4cdf892b014cdf8e37b10005');

--ҳǩ���ģ��Ȩ�ް�ť 
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506ce011be016cef9bc3b400b1', 'tabCbTemplateUpdateCode', '', '�޸�', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506ce011be016cef9bf0fb00b3', 'tabCbTemplateCopyCode', '', '����', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506ce011be016cef9c191400b5', 'tabCbTemplateViewCode', '', 'Ԥ��', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cd2741b016cd5c9e663010a', 'tabCbTemplateAddCode', '', '����', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cd2741b016cd5ca305d010c', 'tabCbTemplateDeleteCode', '', 'ɾ��', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cd2741b016cd5ca9dde010e', 'tabCbTemplateEnableCode', '', '����', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cd2741b016cd5cae50b0110', 'tabCbTemplateDisableCode', '', '����', null, '4028ef506cd2741b016cd2ffef9800e4', '');
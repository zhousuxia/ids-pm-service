--页签组合模板
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506cd2741b016cd2ffef9800e4', null, 2, '页签组合模板', '6', 'tabCombinationTemplateController.do?goTabCombinationTemplate'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028ef506cd2741b016cd2ffefa400e6', '4028ef506cd2741b016cd2ffef9800e4', '4028efec4cdf892b014cdf8e37b10005');

--页签组合模板权限按钮 
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506ce011be016cef9bc3b400b1', 'tabCbTemplateUpdateCode', '', '修改', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506ce011be016cef9bf0fb00b3', 'tabCbTemplateCopyCode', '', '复制', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506ce011be016cef9c191400b5', 'tabCbTemplateViewCode', '', '预览', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cd2741b016cd5c9e663010a', 'tabCbTemplateAddCode', '', '新增', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cd2741b016cd5ca305d010c', 'tabCbTemplateDeleteCode', '', '删除', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cd2741b016cd5ca9dde010e', 'tabCbTemplateEnableCode', '', '启用', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506cd2741b016cd5cae50b0110', 'tabCbTemplateDisableCode', '', '禁用', null, '4028ef506cd2741b016cd2ffef9800e4', '');
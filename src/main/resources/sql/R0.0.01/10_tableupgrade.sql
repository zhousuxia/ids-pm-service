--��Ŀ��Ȩ��ģ��˵�--
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE, FUNCTIONCLS)
values ('4028f0075599c31d015599e7e9d80000', null, 2, '��Ŀ��Ȩ��ģ��', '41', 'projectLibTemplateController.do?goProjectLibTemplate', '4028efec4cdf892b014cdf8fe7fe000b', '4028ef354b5d7bb1014b5d7cfd8c0002', null, '');

--��Ŀ��Ȩ��ģ��˵��㼶--
insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028f0075599fcf001559a027ad30001', '4028f0075599c31d015599e7e9d80000', '4028efec4cdf892b014cdf8fe7fe000b');

--��Ŀ��Ȩ��ģ��˵���ťȨ��--
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00755b37e7c0155b3815a930000', 'projectLibTemplateAdd', '', '����', null, '4028f0075599c31d015599e7e9d80000', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00755b37e7c0155b38199d50001', 'projectLibTemplateBatchDel', '', '����ɾ��', null, '4028f0075599c31d015599e7e9d80000', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00755b37e7c0155b381e7960002', 'projectLibTemplateBatchStart', '', '��������', null, '4028f0075599c31d015599e7e9d80000', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00755b37e7c0155b38220140003', 'projectLibTemplateBatchStop', '', '��������', null, '4028f0075599c31d015599e7e9d80000', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00755b37e7c0155b382761f0004', 'projectLibTemplateUpdate', '', '�޸�', null, '4028f0075599c31d015599e7e9d80000', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00755b37e7c0155b382d7be0005', 'projectLibTemplateDelete', '', 'ɾ��', null, '4028f0075599c31d015599e7e9d80000', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00755b37e7c0155b3832ffb0006', 'projectLibTemplateStart', '', '����', null, '4028f0075599c31d015599e7e9d80000', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00755b37e7c0155b383683a0007', 'projectLibTemplateStop', '', '����', null, '4028f0075599c31d015599e7e9d80000', '');
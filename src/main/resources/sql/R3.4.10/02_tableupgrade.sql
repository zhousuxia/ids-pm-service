--页签组合模板生命周期--
insert into LIFE_CYCLE_POLICY (ID, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('4028ef506e8b72c8016eabfda7f10527', null, 1, '页签组合模板生命周期', '页签组合模板生命周期', 'A.1,A.2,B.1,...');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda86e0528', '', 'nizhi', 0, '拟制中', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8ac0529', '', 'shenhe', 1, '审批中', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8ac052a', '', 'qiyong', 2, '启用', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8bc052b', '', 'jinyong', 3, '禁用', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8bc052c', '', 'xiuding', 4, '修订中', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into BUSINESS_OBJECT_INIT_RULE (ID, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY, SECURITYLEVEL, GENERATOR_INFO_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8cb052d', 'com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate', '页签组合模板生命周期', '', '', '', '', '', '');

--页签组合模板添加按钮权限--
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eb06be8016eb083a0f90003', 'tabCbTemplateminBackCode', '', '回退', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eb06be8016eb08486280008', 'tabCbTemplatemajBackCode', '', '撤消', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eb06be8016eb084c1d0000a', 'tabCbTemplateReviseCode', '', '修订', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eb06be8016eb0850e89000c', 'tabCbTemplateApproveCode', '', '审批', null, '4028ef506cd2741b016cd2ffef9800e4', '');

--ҳǩ���ģ����������--
insert into LIFE_CYCLE_POLICY (ID, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('4028ef506e8b72c8016eabfda7f10527', null, 1, 'ҳǩ���ģ����������', 'ҳǩ���ģ����������', 'A.1,A.2,B.1,...');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda86e0528', '', 'nizhi', 0, '������', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8ac0529', '', 'shenhe', 1, '������', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8ac052a', '', 'qiyong', 2, '����', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8bc052b', '', 'jinyong', 3, '����', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8bc052c', '', 'xiuding', 4, '�޶���', '4028ef506e8b72c8016eabfda7f10527', '', '', '', '');

insert into BUSINESS_OBJECT_INIT_RULE (ID, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY, SECURITYLEVEL, GENERATOR_INFO_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506e8b72c8016eabfda8cb052d', 'com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate', 'ҳǩ���ģ����������', '', '', '', '', '', '');

--ҳǩ���ģ����Ӱ�ťȨ��--
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eb06be8016eb083a0f90003', 'tabCbTemplateminBackCode', '', '����', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eb06be8016eb08486280008', 'tabCbTemplatemajBackCode', '', '����', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eb06be8016eb084c1d0000a', 'tabCbTemplateReviseCode', '', '�޶�', null, '4028ef506cd2741b016cd2ffef9800e4', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eb06be8016eb0850e89000c', 'tabCbTemplateApproveCode', '', '����', null, '4028ef506cd2741b016cd2ffef9800e4', '');

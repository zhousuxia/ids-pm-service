--ҳǩģ����������--
insert into LIFE_CYCLE_POLICY (ID, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('4028ef506eb06be8016eb6a47ff31105', null, 1, 'ҳǩģ����������', 'ҳǩģ����������', 'A.1,A.2,B.1,...');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506eb06be8016eb6a47ff71106', '', 'nizhi', 0, '������', '4028ef506eb06be8016eb6a47ff31105', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506eb06be8016eb6a47ffb1107', '', 'shenhe', 1, '������', '4028ef506eb06be8016eb6a47ff31105', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506eb06be8016eb6a47ffc1108', '', 'qiyong', 2, '����', '4028ef506eb06be8016eb6a47ff31105', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506eb06be8016eb6a47ffd1109', '', 'jinyong', 3, '����', '4028ef506eb06be8016eb6a47ff31105', '', '', '', '');

insert into life_cycle_status (ID, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506eb06be8016eb6a47ffe110a', '', 'xiuding', 4, '�޶���', '4028ef506eb06be8016eb6a47ff31105', '', '', '', '');

insert into BUSINESS_OBJECT_INIT_RULE (ID, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY, SECURITYLEVEL, GENERATOR_INFO_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028ef506eb06be8016eb6a48011110b', 'com.glaway.ids.planGeneral.plantabtemplate.entity.TabTemplate', 'ҳǩģ����������', '', '', '', '', '', '');

--ҳǩģ����Ӱ�ťȨ��--
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eca27e8016ece8ab6780166', 'approvePlanTabTemplate', '', '����', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eca27e8016ece8b370c0168', 'revisePlanTabTemplate', '', '�޶�', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eca27e8016ece8bc8ef016a', 'revokePlanTabTemplate', '', '����', null, '4028ef506cdadfef016cdff92cef017e', '');

insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506eca27e8016ece8c1d74016c', 'backPlanTabTemplate', '', '����', null, '4028ef506cdadfef016cdff92cef017e', '');
--��Ŀ�ƻ�ģ��
--��������
insert into life_Cycle_Policy (ID, CREATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE, UPDATEBY, UPDATENAME, UPDATETIME, CREATENAME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME)
values ('1002', to_date('11-07-2018 09:43:51', 'dd-mm-yyyy hh24:mi:ss'), '', 1, '��Ŀ�ƻ�ģ��Ĳ���', 'PLAN_TEMPLATE', 'A.1,A.2,B.1,...', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:51', 'dd-mm-yyyy hh24:mi:ss'), 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա');
--����״̬
insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d190010', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'nizhi', 0, '������', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d360011', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'shenhe', 1, '������', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d390012', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'qiyong', 2, '����', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d3b0013', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'jinyong', 3, '����', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d3d0014', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'xiuding', 4, '�޶���', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

--������ʼ������
insert into Business_Object_Init_Rule (ID, CREATETIME,  CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY)
values ('4028f00162851baa01628522c3a23578', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 'com.glaway.ids.project.plantemplate.entity.PlanTemplate', 'PLAN_TEMPLATE');


--��Ŀά��
--��������
insert into life_cycle_policy (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('1004', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 1, '��Ŀά���Ĳ���', 'PROJECT_MANAGER', '1');
--����״̬
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865a92aa20100', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'EDITING', 0, '������', '1004');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865a92aee0101', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'STARTING', 1, 'ִ����', '1004');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865a92af20102', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'PAUSED', 2, '����ͣ', '1004');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865a92af50103', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'CLOSED', 3, '�ѹر�', '1004');
--������ʼ������
insert into Business_Object_Init_Rule (ID, CREATETIME,  CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY)
values ('4028f00568641f1f016865a92b450104', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 'com.glaway.ids.project.menu.entity.Project', 'PROJECT_MANAGER');


--��Ŀ�ĵ�
--��������
insert into life_cycle_policy (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('1005', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 1, '��Ŀ�ĵ��Ĳ���', 'PROJECT_DOCUMENT', 'A.1,A.2,B.1,...');
--����״̬
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865ad4fd00108', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'nizhi', 0, '������', '1005');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865ad4fd30109', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'shenpi', 1, '������', '1005');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865ad4fd4010a', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'guidang', 2, '�ѹ鵵', '1005');
--������ʼ������
insert into Business_Object_Init_Rule (ID, CREATETIME,  CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY)
values ('4028f00568641f1f016865ad4fcc0107', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 'com.glaway.foundation.rep.entity.RepFile', 'PROJECT_DOCUMENT');


--��Ŀ�ƻ�
--��������
insert into life_cycle_policy (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('1003', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 1, '��Ŀ�ƻ��Ĳ���', 'PLAN', '1');
--����״̬
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c96106000d', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'EDITING', 0, '������', '1003');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028ef506dcaea30016dd36e07340482', to_date('16-10-2019 15:17:46', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('16-10-2019 15:17:46', 'dd-mm-yyyy hh24:mi:ss'), '', 'LAUNCHED', 1, '�ѷ���', '1003');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028ef506dcaea30016dd36e07440483', to_date('16-10-2019 15:17:46', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('16-10-2019 15:17:46', 'dd-mm-yyyy hh24:mi:ss'), '', 'TOBERECEIVED', 2, '������', '1003');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c96122000e', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'ORDERED', 3, 'ִ����', '1003');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c9612a0011', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'FEEDBACKING', 4, '�깤ȷ��', '1003');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c961270010', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'FINISH', 5, '���깤', '1003');
insert into LIFE_CYCLE_STATUS (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c96125000f', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'INVALID', 6, '�ѷ���', '1003');
--������ʼ������
insert into Business_Object_Init_Rule (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY)
values ('4028f0065a87b6ea015a87c95f0f000c', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 'com.glaway.ids.project.plan.entity.Plan', 'PLAN');

--�ƻ�����
--��������
insert into life_cycle_policy (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('1006', to_date('02-06-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 1, '��Ŀ���ߵĲ���', 'BASICLINE', '1');
--����״̬
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865b0b8a20118', to_date('02-06-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'EDITING', 0, '������', '1006');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865b0b89e0116', to_date('02-06-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'APPROVING', 1, '������', '1006');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865b0b8a10117', to_date('02-06-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'ENABLE', 2, '����', '1006');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f00568641f1f016865b0b8a30119', to_date('02-06-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'FREEZING', 3, '����', '1006');
--������ʼ������
insert into Business_Object_Init_Rule (ID, CREATETIME,  CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY)
values ('4028f00568641f1f016865b0b89a0115', to_date('02-06-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-03-2015', 'dd-mm-yyyy'), 'com.glaway.ids.project.plan.entity.BasicLine', 'BASICLINE');

--��Ŀģ��--
insert into LIFE_CYCLE_POLICY (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('4028f0056469b640016469dabe730188', to_date('05-07-2018', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '', '', '', null, '0', 1, '��Ŀģ����������', 'PM_PROJ_TEMPLATE', 'A.1,A.2,B.1,...');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac1250189', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'nizhi', 0, '������', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac151018a', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'shenhe', 1, '������', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac155018b', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'qiyong', 2, '����', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac157018c', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'jinyong', 3, '����', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac157018d', to_date('16-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'xiuding', 4, '�޶���', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '');

insert into BUSINESS_OBJECT_INIT_RULE (ID, CREATETIME, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY, SECURITYLEVEL, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, GENERATOR_INFO_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f00162851baa01628522c3a23579', to_date('05-07-2018', 'dd-mm-yyyy'), 'com.glaway.ids.project.projecttemplate.entity.ProjTemplate', 'PM_PROJ_TEMPLATE', '', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', '', '', '', '', '');

--ҳǩ���ģ����������--
insert into LIFE_CYCLE_POLICY (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('4028ef506e8b72c8016eabfda7f10527',  to_date('26-09-2019', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-09-2019', 'dd-mm-yyyy'), null, 1, 'ҳǩ���ģ����������', 'ҳǩ���ģ����������', 'A.1,A.2,B.1,...');

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

--ҳǩģ����������--
insert into LIFE_CYCLE_POLICY (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('4028ef506eb06be8016eb6a47ff31105', to_date('26-09-2019', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '��������Ա', to_date('26-09-2019', 'dd-mm-yyyy'), null, 1, 'ҳǩģ����������', 'ҳǩģ����������', 'A.1,A.2,B.1,...');

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

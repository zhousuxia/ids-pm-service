--��Ŀ�����̹���
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE, FUNCTIONCLS)
values ('4028efee4c3617ec014c362eed6c0012', null, 0, '��Ŀ�ƻ�', '100', 'projectMenuController.do?projectMenu'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '', '4028ef354b5d7bb1014b5d7cfd8c0002', 1, 'fa-leftmenu_project-plan');


delete from t_s_role_operation where functionid = '4028f00162892911016289348292000a';
delete from t_s_role_function where functionid = '4028f00162892911016289348292000a';
delete from t_s_operation where functionid = '4028f00162892911016289348292000a';
delete from t_s_function_relate where functionid = '4028f00162892911016289348292000a';
delete from t_s_function where id='4028f00162892911016289348292000a';

delete from t_s_role_operation where functionid = '4028f00162703bdd01627040f58b0006';
delete from t_s_role_function where functionid = '4028f00162703bdd01627040f58b0006';
delete from t_s_operation where functionid = '4028f00162703bdd01627040f58b0006';
delete from t_s_function_relate where functionid = '4028f00162703bdd01627040f58b0006';
delete from t_s_function where id='4028f00162703bdd01627040f58b0006';

delete from t_s_role_operation where functionid = '4028f02162d116870162d21bd1060063';
delete from t_s_role_function where functionid = '4028f02162d116870162d21bd1060063';
delete from t_s_operation where functionid = '4028f02162d116870162d21bd1060063';
delete from t_s_function_relate where functionid = '4028f02162d116870162d21bd1060063';
delete from t_s_function where id='4028f02162d116870162d21bd1060063';

--��Ŀ���Թ���
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efec4cdf892b014cdf8ca9aa0001', null, 1, '��Ŀ���Թ���', '00', null, '4028ef314c49a7fa014c49aa8a3a0001', '4028ef354b5d7bb1014b5d7cfd9c0007', 1);

--��Ŀ���Թ�����ҳ��
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef314c5a6054014c5a6b49210004', null, 2, '��Ŀ����', '01', 'epsConfigController.do?epsConfig'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efec4cdf892b014cdf8ca9aa0001', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef314c501dd7014c5049ffad0012', null, 2, '��Ŀ�׶�', '02', 'planBusinessConfigController.do?businessConfig'||chr(38)||'type=PHARSE'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efec4cdf892b014cdf8ca9aa0001', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef5b55b3a2640155b54a3b1a0364', null, 2, '�ĵ���������Ȩ��', '05', 'repFileLifeCycleAuthConfigController.do?repLifeCycleStatusAuthConfig'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efec4cdf892b014cdf8ca9aa0001', '4028ef354b5d7bb1014b5d7cfd8c0002', null);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efec4cdfb80f014cdfbd89c30007', null, 2, '��Ŀģ��', '8', 'projTemplateController.do?projTemplateList'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efec4cdf892b014cdf8ca9aa0001', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

--��Ŀ��Ȩ��ģ��˵�--
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028f0075599c31d015599e7e9d80000', null, 2, '��Ŀ��Ȩ��ģ��', '6', 'projectLibTemplateController.do?goProjectLibTemplate'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efec4cdf892b014cdf8ca9aa0001', '4028ef354b5d7bb1014b5d7cfd8c0002', null);


insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efec4cdf892b014cdf8f93ed0009', null, 1, 'ҵ�����', '02', null, '4028ef314c49a7fa014c49aa8a3a0001', '4028ef354b5d7bb1014b5d7cfd9c0007', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef314cbc2fd1014cbc326a670001', null, 2, '��Ŀ����', '01', 'paramSwitchController.do?paramSwitch'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8f93ed0009', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

--ͳ�Ʒ����˵�--
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE, FUNCTIONCLS)
values ('4028f00753bc56a40153bc58ffea0000', null, 0, 'ͳ�Ʒ���', '150', '', '', '4028ef354b5d7bb1014b5d7cfd8c0002', 1, 'fa-leftmenu_statistics');

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028f00753ea50080153ea71ed790009', null, 1, '��Ŀ����', '01', 'statisticalAnalysisController.do?goProjectAnalysis'||chr(38)||'isIframe=true', '4028f00753bc56a40153bc58ffea0000', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028f00753bc56a40153bc5bd8fc0001', null, 1, '��Ա���ط���', '02', 'statisticalAnalysisController.do?goLaborAnalysis'||chr(38)||'isIframe=true', '4028f00753bc56a40153bc58ffea0000', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028f00753e47aaf0153e47dbd840000', null, 1, '�ƻ��������', '03', 'statisticalAnalysisController.do?goPlanChangeAnalysis'||chr(38)||'isIframe=true', '4028f00753bc56a40153bc58ffea0000', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);


--����ʾ�Ĳ˵�����Ŀ��أ�
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('297e26e64eae5953014eae5dde7b0000', null, 1, '��Ŀ�б�', '01', 'projectController.do?project'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efee4c3617ec014c362eed6c0012', '4028ef354b5d7bb1014b5d7cfd8c0002', 3);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('297e26e64eae5953014eae62f3a10002', null, 1, '��ϸ��Ϣ', '02', 'projectController.do?viewDetailBaseInfo'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efee4c3617ec014c362eed6c0012', '4028ef354b5d7bb1014b5d7cfd8c0002', 3);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efed4e9a0a47014e9aad0dee000a', null, 1, '�ƻ�', '03', 'planController.do?plan'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efee4c3617ec014c362eed6c0012', '4028ef354b5d7bb1014b5d7cfd8c0002', 3);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efed4e9a0a47014e9aaea4df000b', null, 2, '���߹���', '01', 'basicLineController.do?basicLine'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efed4e9a0a47014e9aad0dee000a', '4028ef354b5d7bb1014b5d7cfd8c0002', 3);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('297e26e64eae5953014eae6f59690003', null, 1, '�Ŷ�', '04', 'projRolesController.do?getProjRolesAndUsers'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efee4c3617ec014c362eed6c0012', '4028ef354b5d7bb1014b5d7cfd8c0002', 3);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('297e26e64eae5953014eae7543de0005', null, 1, '��Ŀ��', '05', 'projLibController.do?goProjLibLayout'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efee4c3617ec014c362eed6c0012', '4028ef354b5d7bb1014b5d7cfd8c0002', 3);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('297e26e64eae5953014eaf38ad030026', null, 2, '��Ŀ�ĵ�', '01', 'projLibController.do?goProjDocList'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '297e26e64eae5953014eae7543de0005', '4028ef354b5d7bb1014b5d7cfd8c0002', 3);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('297e26e64eae5953014eae76d42b0006', null, 1, 'ͳ�Ʒ���', '06', 'projStatisticsController.do?goStatistics'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efee4c3617ec014c362eed6c0012', '4028ef354b5d7bb1014b5d7cfd8c0002', 3);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028f00b643629e1016436337625000a', null, 2, '�ƻ�������', '2', 'planController.do?goDatagridPage'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efed4e9a0a47014e9aad0dee000a', '4028ef354b5d7bb1014b5d7cfd8c0002', 3);

--��Ϣ֪ͨ���ò˵�--
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE, FUNCTIONCLS)
values ('4028f0055552027c0155520be41b0000', null, 1, '��Ϣ֪ͨ', '80', 'messageReceiverConfigController.do?messageReceiverConfig'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028ef314c49a7fa014c49aa8a3a0001', '4028ef354b5d7bb1014b5d7cfd8c0002', null, '');


--�ĵ��������ò˵�
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028f00b64d426bb0164d52f6a96000e', null, 2, '�ĵ���������', '7', 'repFileTypeConfigController.do?repFileTypeConfigIndex'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', '4028efec4cdf892b014cdf8ca9aa0001', '4028ef354b5d7bb1014b5d7cfd8c0002', null);



--���������嵥���������⡿�˵����ڣ�����¡���Ŀ�ƻ���Ϊ�丸�ڵ�--
update t_s_function set PARENTFUNCTIONID = '4028efee4c3617ec014c362eed6c0012',FUNCTIONTYPE = '3' where exists (select id from t_s_function where id in ('4028f006549dc2ca01549dd6f911001f','4028f02162f570fd0162f66bb5fe00fb')) and ID in ('4028f006549dc2ca01549dd6f911001f','4028f02162f570fd0162f66bb5fe00fb');

--�ƻ�����
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efec4cdf892b014cdf8e37b10005', null, 1, '�ƻ�����', '01', null, '4028ef314c49a7fa014c49aa8a3a0001', '4028ef354b5d7bb1014b5d7cfd9c0007', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef314c5483ec014c54f5f3f30039', null, 2, '���������ƿ�', '01', 'deliveryStandardController.do?deliveryStandard'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef314c698760014c699db8ca0015', null, 2, '����ƿ�', '02', 'nameStandardController.do?nameStandard'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE, FUNCTIONCLS)
values ('4028efee4c3617ec014c3630156b0016', null, 1, '�ƻ�ģ��', '7', 'planTemplateController.do?planTemplate'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1, '');

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef314c5483ec014c54f2e2350037', null, 2, '�ƻ��ȼ�', '06', 'planBusinessConfigController.do?planLevelIndex'||chr(38)||'type=PLANLEVEL'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

--ҳǩ���ģ��
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506cd2741b016cd2ffef9800e4', null, 2, 'ҳǩ���ģ��', '5', 'tabCombinationTemplateController.do?goTabCombinationTemplate'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506cdadfef016cdff92cef017e', null, 2, 'ҳǩģ��', '4', 'tabTemplateController.do?tabTemplateInfoManagePage'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', null);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506dbda131016dc83777090144', null, 2, '�ƻ���������', '9', 'planFeedBackController.do?goPlanFeedBack'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506dbda131016dc8d920e001e3', null, 2, '�з�����ģ��', '8', 'procTemplateController.do?goList'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

--�˵�����
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506dbda131016dc83777090144', null, 2, '�ƻ���������', '9', 'planFeedBackController.do?goPlanFeedBack'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028ef506dbda131016dc8d920e001e3', null, 2, '�з�����ģ��', '8', 'procTemplateController.do?goList'||chr(38)||'isIframe=true', '4028efec4cdf892b014cdf8e37b10005', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

--���Ӳ˵���ϵ
insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028ef506dbda131016dc8d80ddd01e1', '4028ef506dbda131016dc83777090144', '4028efec4cdf892b014cdf8e37b10005');

insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028ef506dbda131016dc8d920ff01e5', '4028ef506dbda131016dc8d920e001e3', '4028efec4cdf892b014cdf8e37b10005');

--�з�����ģ��&�ƻ���������Ȩ��
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8d9c0b601e8', 'procTemplateAddCode', '', '����', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8da3ecb01ee', 'procTemplateEnableCode', '', '����', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8db25bf01f6', 'ProcTemplateCopyProcLineCode', '', '����', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8db6a3d01f8', 'ProcTemplateSubmitProcLineCode', '', '�ύ', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dc1e0101fe', 'ProcTemplateBackMinorLineCode', '', '����', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8da842401f0', 'procTemplateDisableCode', '', '����', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dab32f01f2', 'ProcTemplateUpdateProcLineCode', '', '�޸�', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8daed2301f4', 'ProcTemplateReviseProcLineCode', '', '�޶�', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dbbd6b01fa', 'procTemplateImport', '', '����', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dbee5a01fc', 'ProcTemplateBackMajorLineCode', '', '����', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8da03ed01ec', 'procTemplateDeleteCode', '', 'ɾ��', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dc50280200', 'procCategoryAdd', '', '�з����̷�������', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dc8b060202', 'procCategoryUpdate', '', '�з����̷����޸�', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc8dcb89b0204', 'procCategoryDel', '', '�з����̷���ɾ��', null, '4028ef506dbda131016dc8d920e001e3', '');

insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dbda131016dc83835a60148', 'planFeedBackSave', '', '����', null, '4028ef506dbda131016dc83777090144', '');

insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dcaea30016dceae4e1b0226', 'planConcern', null, '��ע�ƻ�', null, '4028efed4e9a0a47014e9aad0dee000a', '');

insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef506dcaea30016dceae91a0022a', 'planUnconcern', null, 'ȡ����ע�ƻ�', null, '4028efed4e9a0a47014e9aad0dee000a', '');

--�ƻ���ͼ��Ϣ--
insert into PLANVIEW_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, ISDEFAULT, NAME, PUBLISHPERSON, PUBLISHTIME, STATUS)
values ('4028f00763ba3b2001a3ba63fd26000c', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'true', '��ע�ƻ�', '4028ef354b5d7bb1014b5d7cffa10043', to_date('27-08-2017 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'PUBLIC');


--��ͼ��������--
insert into PLANVIEW_SET_CONDITION (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, PLANVIEWINFOID)
values ('4028f00763b8df390163b9ad75441008', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028f00763ba3b2001a3ba63fd26000c');

--�ƻ���ͼչʾ����Ϣ--
insert into PLANVIEW_COLUMN_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, COLUMNID, PLANVIEWINFOID)
values ('4028f00763cd7cb70a63cea6fdb90018', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'planNo,planLevel,planType,planTaskType,status,owner,planStartTime,planEndTime,assigner,assignTime,workTime,preposePlan,mileStone,creator,createTime', '4028f00763ba3b2001a3ba63fd26000c');

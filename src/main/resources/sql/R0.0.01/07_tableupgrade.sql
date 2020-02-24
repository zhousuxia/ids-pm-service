insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efec4cdf892b014cdf8fe7fe000b', null, 1, '模板管理', '03', null, '4028ef314c49a7fa014c49aa8a3a0001', '4028ef354b5d7bb1014b5d7cfd9c0007', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efec4cdfb80f014cdfbd89c30007', null, 2, '项目模板', '01', 'projTemplateController.do?projTemplateList', '4028efec4cdf892b014cdf8fe7fe000b', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);

insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efee4c3617ec014c3630156b0016', null, 2, '计划模板', '02', 'planTemplateController.do?planTemplate', '4028efec4cdf892b014cdf8fe7fe000b', '4028ef354b5d7bb1014b5d7cfd8c0002', 1);


insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028efec4eb48f9d014eb495b42800bd', '4028efec4cdf892b014cdf8fe7fe000b', '4028ef314c49a7fa014c49aa8a3a0001');

insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028efec4eb48f9d014eb495b43c00f3', '4028efec4cdfb80f014cdfbd89c30007', '4028efec4cdf892b014cdf8fe7fe000b');
insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028efec4eb48f9d014eb495b43c00f9', '4028efee4c3617ec014c3630156b0016', '4028efec4cdf892b014cdf8fe7fe000b');


--项目模板
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297e26e64eb04edf014eb06369840009', 'enableProjTemplate', null, '启用', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297e26e64eb04edf014eb0645b0f000a', 'forbiddenProjTemplate', null, '禁用', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297e26e64eb04edf014eb0613e0c0007', 'deleteProjTemplate', null, '删除', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297e26e64eb04edf014eb0645b0f012a', 'addProjTemplate', '', '新增', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297e26e64eb04edf014eb0645b0f023a', 'updateProjTemplate', '', '修改', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297c36e64eb04edf014eb0645b0f012a', 'approveProjTemplate', '', '审批', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297d46e64eb04edf014eb0645b0f023a', 'copyProjTemplate', '', '复制', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297c36e64eb04edf014eb0645b0f0191', 'reviseProjTemplate', '', '修订', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297c36e64eb04edf014eb0645b0f0193', 'minBackProjTemplate', '', '回退', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('297c36e64eb04edf014eb0645b0f0195', 'majBackProjTemplate', '', '撤消', null, '4028efec4cdfb80f014cdfbd89c30007', '4028ef354b5d7bb1014b5d7cfd5a0000');

--计划模板
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef324eae835a014eaf1d497f0004', 'planTemplateImportCode', null, '导入', null, '4028efee4c3617ec014c3630156b0016', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef324eae835a014eaf2e35510005', 'planTemplateDeleteCode', null, '删除', null, '4028efee4c3617ec014c3630156b0016', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef324eae835a014eaf32750e0008', 'planTemplateExportCode', null, '导出', null, '4028efee4c3617ec014c3630156b0016', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef324eae835a014eaf32ccff0009', 'planTemplateEnableCode', null, '启用', null, '4028efee4c3617ec014c3630156b0016', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef324eae835a014eaf3402d5000a', 'planTemplateDisableCode', null, '禁用', null, '4028efee4c3617ec014c3630156b0016', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028ef324eae835a014eaf435a54000f', 'planTemplateSubmitLineCode', null, '提交', null, '4028efee4c3617ec014c3630156b0016', '4028ef354b5d7bb1014b5d7cfd5a0000');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00b643ed29301643f1501ee0009', 'planTemplateAddCode', '', '新增', null, '4028efee4c3617ec014c3630156b0016', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f00b6458a924016458eee686001b', 'planTemplateUpdateCode', '', '修改', null, '4028efee4c3617ec014c3630156b0016', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f007648835e2016488831a0d000e', 'planTemplateCopyCode', '', '复制', null, '4028efee4c3617ec014c3630156b0016', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f007648835e20164888418490010', 'planTemplateRevokeCode', '', '撤消', null, '4028efee4c3617ec014c3630156b0016', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f007648835e2016488847ee90012', 'planTemplateReviseCode', '', '修订', null, '4028efee4c3617ec014c3630156b0016', '');
insert into t_s_operation (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
values ('4028f007648c029e01648c2bb072001e', 'planTemplateBackCode', '', '回退', null, '4028efee4c3617ec014c3630156b0016', '');

--项目计划模板
--创建策略
insert into life_Cycle_Policy (ID, CREATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE, UPDATEBY, UPDATENAME, UPDATETIME, CREATENAME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME)
values ('1002', to_date('11-07-2018 09:43:51', 'dd-mm-yyyy hh24:mi:ss'), '', 1, '项目计划模板的策略', 'PLAN_TEMPLATE', 'A.1,A.2,B.1,...', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:51', 'dd-mm-yyyy hh24:mi:ss'), 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员');
--创建状态
insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d190010', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'nizhi', 0, '拟制中', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d360011', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'shenhe', 1, '审批中', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d390012', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'qiyong', 2, '启用', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d3b0013', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'jinyong', 3, '禁用', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0076486d763016487024d3d0014', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '', 'xiuding', 4, '修订中', '1002', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('11-07-2018 09:43:52', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

--创建初始化规则
insert into Business_Object_Init_Rule (ID, CREATETIME,  CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY)
values ('4028f00162851baa01628522c3a23578', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', to_date('26-03-2015', 'dd-mm-yyyy'), 'com.glaway.ids.project.plantemplate.entity.PlanTemplate', 'PLAN_TEMPLATE');



--项目模板--
insert into LIFE_CYCLE_POLICY (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('4028f0056469b640016469dabe730188', to_date('05-07-2018', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', '', '', '', null, '0', 1, '项目模板生命周期', 'PM_PROJ_TEMPLATE', 'A.1,A.2,B.1,...');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac1250189', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'nizhi', 0, '拟制中', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac151018a', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'shenhe', 1, '审批中', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac155018b', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'qiyong', 2, '启用', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac157018c', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'jinyong', 3, '禁用', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

insert into life_cycle_status (ID, CREATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f0056469b640016469dac157018d', to_date('16-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '', 'xiuding', 4, '修订中', '4028f0056469b640016469dabe730188', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018 17:51:41', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '');

insert into BUSINESS_OBJECT_INIT_RULE (ID, CREATETIME, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY, SECURITYLEVEL, CREATENAME, UPDATEBY, UPDATENAME, UPDATETIME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, GENERATOR_INFO_ID, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f00162851baa01628522c3a23579', to_date('05-07-2018', 'dd-mm-yyyy'), 'com.glaway.ids.project.projecttemplate.entity.ProjTemplate', 'PM_PROJ_TEMPLATE', '', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_date('05-07-2018', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', '', '', '', '', '');




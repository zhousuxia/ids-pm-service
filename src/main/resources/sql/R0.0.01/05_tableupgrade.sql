
--项目计划
--创建策略
insert into life_cycle_policy (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, AVALIABLE, DESCRIPTION, NAME, REVISESEQUENCE)
values ('1003', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 1, '项目计划的策略', 'PLAN', '1');
--创建状态
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c96106000d', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'EDITING', 0, '拟制中', '1003');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c96122000e', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'ORDERED', 1, '已下达', '1003');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c9612a0011', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'FEEDBACKING', 2, '完工反馈中', '1003');
insert into life_cycle_status (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c961270010', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'FINISH', 3, '已完工', '1003');
insert into LIFE_CYCLE_STATUS (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, SECURITYLEVEL, NAME, ORDERNUM, TITLE, POLICY_ID)
values ('4028f0065a87b6ea015a87c96125000f', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', to_date('26-03-2015', 'dd-mm-yyyy'), 0, 'INVALID', 4, '已废弃', '1003');
--创建初始化规则
insert into Business_Object_Init_Rule (ID, CREATETIME, CREATEBY, CREATENAME, CREATEFULLNAME, UPDATEBY, UPDATENAME, UPDATEFULLNAME, UPDATETIME, BUSINESSOBJECTURL, DEFAULTLIFECYCLEPOLICY)
values ('4028f0065a87b6ea015a87c95f0f000c', to_date('26-03-2015', 'dd-mm-yyyy'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '超级管理员', to_date('26-03-2015', 'dd-mm-yyyy'), 'com.glaway.ids.project.plan.entity.Plan', 'PLAN');


--文档生命周期权限初始化数据--
insert into LIFE_CYCLE_PERMISSION (ID, OBJECTCLASS, POLICYNAME, STATUSNAME, PERMISSIONCODE)
values ('4028f00555b9b1b60155b9b3c6220005', 'com.glaway.foundation.rep.entity.RepFile', 'PROJECT_DOCUMENT', 'nizhi', '1787');
insert into LIFE_CYCLE_PERMISSION (ID, OBJECTCLASS, POLICYNAME, STATUSNAME, PERMISSIONCODE)
values ('4028f00555b9b1b60155b9b3c6310006', 'com.glaway.foundation.rep.entity.RepFile', 'PROJECT_DOCUMENT', 'shenpi', '163');
insert into LIFE_CYCLE_PERMISSION (ID, OBJECTCLASS, POLICYNAME, STATUSNAME, PERMISSIONCODE)
values ('4028f00555b9b1b60155b9b3c6390007', 'com.glaway.foundation.rep.entity.RepFile', 'PROJECT_DOCUMENT', 'guidang', '483');

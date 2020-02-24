--文档编码生成规则
insert into SN_GENERATOR_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, CURVAL, DESCRIPTION, GENERATORCLASSNAME, NAME, NEXTVAL)
values ('4028ef2d506ff0c901506ff554f30000', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '-', null, 'com.glaway.ids.project.projectmanager.controller.ProjFileSerialNumberGenerator', 'projcetFile', '1');

--计划通用化 业签组合模板编号规则--
insert into SN_GENERATOR_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, CURVAL, DESCRIPTION, GENERATORCLASSNAME, NAME, NEXTVAL, GENERATERULEID, GENERATORTYPE, STATUS)
values ('4028ef506cd0cce3016cd0ee8ea60059', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '', '001 , 永不归零', '', '页签组合模板', '', '4028ef506cd0cce3016cd0ee8e680058', '1', '1');

--计划通用化 页签模板编号规则--
insert into SN_GENERATOR_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, CURVAL, DESCRIPTION, GENERATORCLASSNAME, NAME, NEXTVAL, GENERATERULEID, GENERATORTYPE, STATUS)
values ('4028ef506ce011be016cfb5c096a0c79', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '', 'TC001 , 永不归零', '', '页签模板编码', '', '4028ef506ce011be016cfb5c096a0c78', '1', '1');


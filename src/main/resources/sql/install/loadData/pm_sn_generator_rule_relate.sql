--增加自动编号sequence--
create sequence SEQ_GW20170110095218 minvalue 0 maxvalue 999 start with 1 increment by 1 nocache;
create sequence SEQ_GW20190827103812 minvalue 0 maxvalue 999 start with 1 increment by 1 nocache;
create sequence SEQ_GW20190904162044 minvalue 0 maxvalue 999 start with 1 increment by 1 nocache;

--编号生成规则与业务关系--
insert into SN_GENERATOR_RULE_RELATE (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, CLASSNAME, DESCRIPTION, PROPERTYNAME, SEQUENCENAME, GENERATORINFOID, INITVALUE)
values ('4028f007598603bc015986137397000d', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'epsConfig', '', '', 'SEQ_GW20170110095218', '4028f007598603bc0159860df32a0008', 1);

--计划通用化 业签组合模板编号规则--
insert into SN_GENERATOR_RULE_RELATE (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, CLASSNAME, DESCRIPTION, PROPERTYNAME, SEQUENCENAME, GENERATORINFOID, INITVALUE)
values ('4028ef506cd0cce3016cd0f01aed005e', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'tabCombinationTemplate', '', '', 'SEQ_GW20190827103812', '4028ef506cd0cce3016cd0ee8ea60059', 1);

--计划通用化 页签模板编号规则--
insert into SN_GENERATOR_RULE_RELATE (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, CLASSNAME, DESCRIPTION, PROPERTYNAME, SEQUENCENAME, GENERATORINFOID, INITVALUE)
values ('4028ef506ce011be016cfb5c95730c7c', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'tabTemplateCode', '', '', 'SEQ_GW20190904162044', '4028ef506ce011be016cfb5c096a0c79', 1);


--�ƻ�ͨ�û� ҵǩ���ģ���Ź���--
create sequence SEQ_GW20190827103812 minvalue 0 maxvalue 999 start with 1 increment by 1 nocache;

insert into SN_GENERATOR_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, CURVAL, DESCRIPTION, GENERATORCLASSNAME, NAME, NEXTVAL, GENERATERULEID, GENERATORTYPE, STATUS)
values ('4028ef506cd0cce3016cd0ee8ea60059', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '', '001 , ��������', '', 'ҳǩ���ģ��', '', '4028ef506cd0cce3016cd0ee8e680058', '1', '1');

insert into SN_GENERATOR_RULE (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, DATEFORMATSTR, GENERATORRULEVALUE, POWERVALUE, PREFIXSTR, STARTVALUE, STEP, SUFFIXSTR, ZEROTYPE)
values ('4028ef506cd0cce3016cd0ee8e680058', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', 'gladmin', to_date('04-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '', '', 3, '', 1, 1, '', '0');

insert into SN_GENERATOR_RULE_RELATE (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, CLASSNAME, DESCRIPTION, PROPERTYNAME, SEQUENCENAME, GENERATORINFOID, INITVALUE)
values ('4028ef506cd0cce3016cd0f01aed005e', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'tabCombinationTemplate', '', '', 'SEQ_GW20190827103812', '4028ef506cd0cce3016cd0ee8ea60059', 1);


--�ƻ�ͨ�û� ҳǩģ���Ź���--
create sequence SEQ_GW20190904162044 minvalue 0 maxvalue 999 start with 1 increment by 1 nocache;

insert into SN_GENERATOR_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, CURVAL, DESCRIPTION, GENERATORCLASSNAME, NAME, NEXTVAL, GENERATERULEID, GENERATORTYPE, STATUS)
values ('4028ef506ce011be016cfb5c096a0c79', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '', 'TC001 , ��������', '', 'ҳǩģ�����', '', '4028ef506ce011be016cfb5c096a0c78', '1', '1');

insert into SN_GENERATOR_RULE (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, DATEFORMATSTR, GENERATORRULEVALUE, POWERVALUE, PREFIXSTR, STARTVALUE, STEP, SUFFIXSTR, ZEROTYPE)
values ('4028ef506ce011be016cfb5c096a0c78', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '', '', 3, 'TC', 1, 1, '', '0');

insert into SN_GENERATOR_RULE_RELATE (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, CLASSNAME, DESCRIPTION, PROPERTYNAME, SEQUENCENAME, GENERATORINFOID, INITVALUE)
values ('4028ef506ce011be016cfb5c95730c7c', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'tabTemplateCode', '', '', 'SEQ_GW20190904162044', '4028ef506ce011be016cfb5c096a0c79', 1);


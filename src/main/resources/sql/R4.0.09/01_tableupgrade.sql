insert into PM_ACTIVITY_TYPE_MANAGE (ID, AVALIABLE, CODE, NAME, STATUS, SOURCE)
values ('4028f00d6db34426016db365b27c0000', '1', 'NZ004', 'PLM计划类', 'enable', '0');

update pm_object_property_info a set a.propertyname='关联PLM系统',a.ordernumber='2' where a.id='4028f00d6db8e701016dbdf5e859000a';
update pm_object_property_info a set a.ordernumber='0' where a.id='sd7e02af6d1a664d016d1a8ce42e00a1';
update pm_object_property_info a set a.ordernumber='1' where a.id='297e02af6dce09e3016dd29396940002';

--PLM系统
insert into pm_tab_combination_template (ID, ACTIVITYID, CODE, NAME, STATUS, TEMPLATENAME, AVALIABLE)
values ('4028f00d6dd22ee4016dd25de68d0001', '4028f00d6db34426016db365b27c0000', '192', 'PLM系统', '1', 'PLM计划类', '1');

--plm页签组合模板信息
insert into pm_combination_template_info (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6df6c71b016df74f1c330025', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '基本信息', 0, '4028f00d6dd22ee4016dd25de68d0001', '4028f0066d48f35e016d48f864450001');

insert into pm_combination_template_info (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6df6c71b016df74f1c380026', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '输入', 1, '4028f00d6dd22ee4016dd25de68d0001', '297e02af6d18ed7d016d18f191460112');

insert into pm_combination_template_info (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6df6c71b016df74f1c380027', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '输出', 2, '4028f00d6dd22ee4016dd25de68d0001', '297e02af6d4e03d1016d4e1aa44e0223');

insert into pm_combination_template_info (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, NAME, ORDERNUM, TABCOMBINATIONTEMPLATEID, TYPEID)
values ('297e02af6df6c71b016df74f1c380028', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('27-08-2019 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '资源', 3, '4028f00d6dd22ee4016dd25de68d0001', '297e02af6d1e0823016d1ec0a223038d');
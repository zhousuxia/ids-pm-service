--项目计划
delete from Business_Object_Init_Rule where BUSINESSOBJECTURL = 'com.glaway.ids.project.plan.entity.Plan' and DEFAULTLIFECYCLEPOLICY = 'PLAN';
delete from life_cycle_status where POLICY_ID = '1003';
delete from life_Cycle_Policy where id = '1003';

--文档生命周期权限初始化数据--
 delete from LIFE_CYCLE_PERMISSION where id = '4028f00555b9b1b60155b9b3c6220005';
 delete from LIFE_CYCLE_PERMISSION where id = '4028f00555b9b1b60155b9b3c6310006';
 delete from LIFE_CYCLE_PERMISSION where id = '4028f00555b9b1b60155b9b3c6390007';
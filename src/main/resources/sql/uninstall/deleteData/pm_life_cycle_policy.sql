--��Ŀģ��--
delete from BUSINESS_OBJECT_INIT_RULE where DEFAULTLIFECYCLEPOLICY = 'PM_PROJ_TEMPLATE';
delete from life_cycle_status where POLICY_ID = '4028f0056469b640016469dabe730188';
delete from LIFE_CYCLE_POLICY where id = '4028f0056469b640016469dabe730188';

--��Ŀ�ƻ�ģ��
delete from Business_Object_Init_Rule where BUSINESSOBJECTURL = 'com.glaway.ids.project.plantemplate.entity.PlanTemplate' and DEFAULTLIFECYCLEPOLICY = 'PLAN_TEMPLATE';
delete from life_cycle_status where POLICY_ID = '1002';
delete from life_Cycle_Policy where id = '1002';



--��Ŀ�ƻ�
delete from Business_Object_Init_Rule where BUSINESSOBJECTURL = 'com.glaway.ids.project.plan.entity.Plan' and DEFAULTLIFECYCLEPOLICY = 'PLAN';
delete from life_cycle_status where POLICY_ID = '1003';
delete from life_Cycle_Policy where id = '1003';


--��Ŀά��
delete from Business_Object_Init_Rule where BUSINESSOBJECTURL = 'com.glaway.ids.project.menu.entity.Project' and DEFAULTLIFECYCLEPOLICY = 'PROJECT_MANAGER';
delete from life_cycle_status where POLICY_ID = '1004';
delete from life_Cycle_Policy where id = '1004';

--��Ŀ�ĵ�
delete from Business_Object_Init_Rule where BUSINESSOBJECTURL = 'com.glaway.foundation.rep.entity.RepFile' and DEFAULTLIFECYCLEPOLICY = 'PROJECT_DOCUMENT';
delete from life_cycle_status where POLICY_ID = '1005';
delete from life_Cycle_Policy where id = '1005';

--�ƻ�����
delete from Business_Object_Init_Rule where BUSINESSOBJECTURL = 'com.glaway.ids.project.plan.entity.BasicLine' and DEFAULTLIFECYCLEPOLICY = 'BASICLINE';
delete from life_cycle_status where POLICY_ID = '1006';
delete from life_Cycle_Policy where id = '1006';

--ҳǩ���ģ����������
delete from Business_Object_Init_Rule where BUSINESSOBJECTURL = 'com.glaway.ids.planGeneral.tabCombinationTemplate.entity.TabCombinationTemplate' and DEFAULTLIFECYCLEPOLICY = 'ҳǩ���ģ����������';
delete from life_cycle_status where POLICY_ID = '4028ef506e8b72c8016eabfda7f10527';
delete from life_Cycle_Policy where id = '4028ef506e8b72c8016eabfda7f10527';

--ҳǩģ����������
delete from Business_Object_Init_Rule where BUSINESSOBJECTURL = 'com.glaway.ids.planGeneral.plantabtemplate.entity.TabTemplate' and DEFAULTLIFECYCLEPOLICY = 'ҳǩģ����������';
delete from life_cycle_status where POLICY_ID = '4028ef506eb06be8016eb6a47ff31105';
delete from life_Cycle_Policy where id = '4028ef506eb06be8016eb6a47ff31105';
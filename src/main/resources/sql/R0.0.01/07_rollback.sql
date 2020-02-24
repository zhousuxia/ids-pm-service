--项目模板--
delete from BUSINESS_OBJECT_INIT_RULE where DEFAULTLIFECYCLEPOLICY = 'PM_PROJ_TEMPLATE';
delete from life_cycle_status where POLICY_ID = '4028f0056469b640016469dabe730188';
delete from LIFE_CYCLE_POLICY where id = '4028f0056469b640016469dabe730188';

--项目计划模板
delete from Business_Object_Init_Rule where BUSINESSOBJECTURL = 'com.glaway.ids.project.plantemplate.entity.PlanTemplate' and DEFAULTLIFECYCLEPOLICY = 'PLAN_TEMPLATE';
delete from life_cycle_status where POLICY_ID = '1002';
delete from life_Cycle_Policy where id = '1002';

delete from t_s_role_operation where functionid = '4028efee4c3617ec014c3630156b0016';
delete from t_s_role_function where functionid = '4028efee4c3617ec014c3630156b0016';
delete from t_s_operation where functionid = '4028efee4c3617ec014c3630156b0016';
delete from t_s_function_relate where functionid = '4028efee4c3617ec014c3630156b0016';
delete from T_S_FUNCTION where ID = '4028efee4c3617ec014c3630156b0016';


delete from t_s_role_operation where functionid = '4028efec4cdfb80f014cdfbd89c30007';
delete from t_s_role_function where functionid = '4028efec4cdfb80f014cdfbd89c30007';
delete from t_s_operation where functionid = '4028efec4cdfb80f014cdfbd89c30007';
delete from t_s_function_relate where functionid = '4028efec4cdfb80f014cdfbd89c30007';
delete from T_S_FUNCTION where ID = '4028efec4cdfb80f014cdfbd89c30007';

delete from t_s_role_operation where functionid = '4028efec4cdf892b014cdf8fe7fe000b';
delete from t_s_role_function where functionid = '4028efec4cdf892b014cdf8fe7fe000b';
delete from t_s_operation where functionid = '4028efec4cdf892b014cdf8fe7fe000b';
delete from t_s_function_relate where functionid = '4028efec4cdf892b014cdf8fe7fe000b';
delete from T_S_FUNCTION where ID = '4028efec4cdf892b014cdf8fe7fe000b';
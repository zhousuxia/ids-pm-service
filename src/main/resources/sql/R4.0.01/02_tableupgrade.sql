--名称库管理改为计划管理
update T_S_FUNCTION set FUNCTIONNAME = '计划管理' where ID = '4028efec4cdf892b014cdf8e37b10005';

update T_S_FUNCTION set FUNCTIONURL = 'deliveryStandardController.do?deliveryStandard'||chr(38)||'isIframe=true' where ID = '4028ef314c5483ec014c54f5f3f30039';
update T_S_FUNCTION set FUNCTIONURL = 'nameStandardController.do?nameStandard'||chr(38)||'isIframe=true' where ID = '4028ef314c698760014c699db8ca0015';

--将【模板管理】中【计划模板】移至新增的【计划管理】中
update T_S_FUNCTION set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8e37b10005',FUNCTIONORDER = '03' where ID = '4028efee4c3617ec014c3630156b0016';
update T_S_FUNCTION_RELATE set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8e37b10005' where ID = '4028efec4eb48f9d014eb495b43c00f9';

--将【项目模板】【项目库权限模板】移动至【项目属性管理】中
update T_S_FUNCTION set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8ca9aa0001',FUNCTIONORDER = '07' where ID = '4028efec4cdfb80f014cdfbd89c30007';
update T_S_FUNCTION set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8ca9aa0001' where ID = '4028f0075599c31d015599e7e9d80000';
update T_S_FUNCTION_RELATE set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8ca9aa0001' where ID = '4028efec4eb48f9d014eb495b43c00f3';
update T_S_FUNCTION_RELATE set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8ca9aa0001' where ID = '4028f0075599fcf001559a027ad30001';

--移除【模板管理】
delete from T_S_FUNCTION_RELATE where ID = '4028efa96ca853b2016ca90719280007';
delete from t_s_role_function where functionid = '4028efec4cdf892b014cdf8fe7fe000b';
delete from T_S_FUNCTION_RELATE where ID = '4028efec4eb48f9d014eb495b42800bd';
delete from T_S_FUNCTION where ID = '4028efec4cdf892b014cdf8fe7fe000b';

--将【项目属性管理】中的【计划等级】移动至新增的模块【计划管理】中
update T_S_FUNCTION set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8e37b10005',FUNCTIONORDER = '04' where ID = '4028ef314c5483ec014c54f2e2350037';
update T_S_FUNCTION_RELATE set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8e37b10005' where ID = '4028efec4eb48f9d014eb495b43d00fa';
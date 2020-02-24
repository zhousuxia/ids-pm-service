--名称库管理改为计划管理[回滚]
update T_S_FUNCTION set FUNCTIONNAME = '名称库管理' where ID = '4028efec4cdf892b014cdf8e37b10005';

update T_S_FUNCTION set FUNCTIONURL = 'deliveryStandardController.do?deliveryStandard'||chr(38)||'isIframe=true' where ID = '4028ef314c5483ec014c54f5f3f30039';
update T_S_FUNCTION set FUNCTIONURL = 'nameStandardController.do?nameStandard'||chr(38)||'isIframe=true' where ID = '4028ef314c698760014c699db8ca0015';

--移除【模板管理】[回滚]
insert into T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL, PARENTFUNCTIONID, ICONID, FUNCTIONTYPE)
values ('4028efec4cdf892b014cdf8fe7fe000b', null, 1, '模板管理', '03', null, '4028ef314c49a7fa014c49aa8a3a0001', '4028ef354b5d7bb1014b5d7cfd9c0007', 1);
insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028efec4eb48f9d014eb495b42800bd', '4028efec4cdf892b014cdf8fe7fe000b', '4028ef314c49a7fa014c49aa8a3a0001');
insert into T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
values ('4028efa96ca853b2016ca90719280007', '4028efec4cdf892b014cdf8fe7fe000b', '4028ef314c49a7fa014c49aa8a3a0001');

--将【模板管理】中【计划模板】移至新增的【计划管理】中[回滚]
update T_S_FUNCTION set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8fe7fe000b',FUNCTIONORDER = '02' where ID = '4028efee4c3617ec014c3630156b0016';
update T_S_FUNCTION_RELATE set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8fe7fe000b' where ID = '4028efec4eb48f9d014eb495b43c00f9';

--将【项目模板】【项目库权限模板】移动至【项目属性管理】中[回滚]
update T_S_FUNCTION set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8fe7fe000b',FUNCTIONORDER = '01' where ID = '4028efec4cdfb80f014cdfbd89c30007';
update T_S_FUNCTION set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8fe7fe000b' where ID = '4028f0075599c31d015599e7e9d80000';
update T_S_FUNCTION_RELATE set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8fe7fe000b' where ID = '4028efec4eb48f9d014eb495b43c00f3';
update T_S_FUNCTION_RELATE set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8fe7fe000b' where ID = '4028f0075599fcf001559a027ad30001';

--将【项目属性管理】中的【计划等级】移动至新增的模块【计划管理】中[回滚]
update T_S_FUNCTION set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8ca9aa0001',FUNCTIONORDER = '03' where ID = '4028ef314c5483ec014c54f2e2350037';
update T_S_FUNCTION_RELATE set PARENTFUNCTIONID = '4028efec4cdf892b014cdf8ca9aa0001' where ID = '4028efec4eb48f9d014eb495b43d00fa';
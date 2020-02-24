update LIFE_CYCLE_POLICY set CREATETIME = to_date('26-09-2019', 'dd-mm-yyyy'),CREATEBY='4028ef354b5d7bb1014b5d7cffa10043',CREATENAME='gladmin',CREATEFULLNAME='超级管理员' where ID in('4028ef506eb06be8016eb6a47ff31105','4028ef506e8b72c8016eabfda7f10527');

--风险清单开启图标替换--
update pm_object_property_info set FORMAT='basis ui-icon-enable' where ID='4028f0066d3cc740016d3f4a186c009b';
delete from PM_TAB_TEMPLATE where CODE in('TC001','TC002','TC003','TC004','TC005','TC006');
--问题，风险清单code修改
update PM_TAB_TEMPLATE set CODE='TC005' where ID='4028f0066d1de316016d1f0215b60004';
update PM_TAB_TEMPLATE set CODE='TC006' where ID='4028f0066d3cc740016d3f43adbf0093';

update PM_TAB_TEMPLATE t set t.avaliable = '0' where t.avaliable = '1' and name like '%基本信息%';
update PM_TAB_TEMPLATE t set t.avaliable = '0' where t.avaliable = '1' and name like '%输入%';
update PM_TAB_TEMPLATE t set t.avaliable = '0' where t.avaliable = '1' and name like '%输出%';
update PM_TAB_TEMPLATE t set t.avaliable = '0' where t.avaliable = '1' and name like '%资源%';
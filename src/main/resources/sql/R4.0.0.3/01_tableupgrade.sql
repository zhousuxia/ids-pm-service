delete from PM_TAB_TEMPLATE where CODE in('TC001','TC002','TC003','TC004','TC005','TC006');
--���⣬�����嵥code�޸�
update PM_TAB_TEMPLATE set CODE='TC005' where ID='4028f0066d1de316016d1f0215b60004';
update PM_TAB_TEMPLATE set CODE='TC006' where ID='4028f0066d3cc740016d3f43adbf0093';

update PM_TAB_TEMPLATE t set t.avaliable = '0' where t.avaliable = '1' and name like '%������Ϣ%';
update PM_TAB_TEMPLATE t set t.avaliable = '0' where t.avaliable = '1' and name like '%����%';
update PM_TAB_TEMPLATE t set t.avaliable = '0' where t.avaliable = '1' and name like '%���%';
update PM_TAB_TEMPLATE t set t.avaliable = '0' where t.avaliable = '1' and name like '%��Դ%';
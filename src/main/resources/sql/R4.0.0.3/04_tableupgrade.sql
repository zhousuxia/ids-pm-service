insert into pm_object_property_info (ID, SECURITYLEVEL, CONTROL, DATASOURCEID, FORMAT, OBJECTPATH, OPERATIONEVENT, ORDERNUMBER, PROPERTYNAME, PROPERTYVALUE, DISPLAY, READWRITEACCESS, REQUIRED, AVALIABLE)
values ('sd7e02af6d1ef39c016d1efa723500011', null, '8', '297e02af6d4e03d1016d4e1aa4510224', 'basis ui-icon-copy', '/', 'TC003.inheritParent()', '0', '继承父项', '/', '0', '3', 0, '1');
update pm_object_property_info t set t.ordernumber = '1' where ID = 'sd7e02af6d1ef39c016d1efa72350003';
update pm_object_property_info t set t.ordernumber = '2' where ID = 'sd7e02af6d4e03d1016d4e1aa48e0029';
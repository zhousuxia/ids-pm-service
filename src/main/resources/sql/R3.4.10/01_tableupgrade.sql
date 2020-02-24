/*手动添加生命周期外键*/
alter table pm_tab_combination_template add POLICY_ID VARCHAR2(255 CHAR);
alter table pm_tab_combination_template add constraint FK_qwjc5kvqd9yayhpnk34j4pcr6 foreign key (policy_id) references life_Cycle_Policy(id);

update pm_tab_combination_template t
   set t.bizcurrent        = 'nizhi',
       t.bizversion        = 'A.1',
       t.bizid             = substr(sys_guid(), 1, 8) || '-' ||
                             substr(sys_guid(), 9, 4) || '-' ||
                             substr(sys_guid(), 13, 4) || '-' ||
                             substr(sys_guid(), 17, 4) || '-' ||
                             substr(sys_guid(), 20, 12),
       t.branchiterationid = substr(sys_guid(), 1, 8) || '-' ||
                             substr(sys_guid(), 9, 4) || '-' ||
                             substr(sys_guid(), 13, 4) || '-' ||
                             substr(sys_guid(), 17, 4) || '-' ||
                             substr(sys_guid(), 20, 12),
       t.lastiterationid   = '0',
       t.latestiteration   = '1',
       t.policy_id         = '4028ef506e8b72c8016eabfda7f10527' where t.bizid is null;

--将系统中启用的页签组合模板生命周期状态改为启用--
update pm_tab_combination_template t set t.bizcurrent = 'qiyong' where t.status = '1';

--for成都--
delete from pm_tab_combination_template t where t.status = '0';

--修改计划绑定的页签组合模板--
update pm_plan plan
   set plan.tabcbtemplateid = (select temp.id
                                 from pm_tab_combination_template temp
                                where temp.activityid = plan.tasknametype
                                  and temp.status = '1' and rownum =1);
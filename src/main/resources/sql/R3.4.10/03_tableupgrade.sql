/*手动添加页签模板生命周期外键*/
alter table pm_tab_template add POLICY_ID VARCHAR2(255 CHAR);
alter table pm_tab_template add constraint FK_qwjc5kvqd9yayhpnk34j4pcr6 foreign key (policy_id) references life_Cycle_Policy(id);

update pm_tab_template t
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
       t.policy_id         = '4028ef506eb06be8016eb6a47ff31105' where t.bizid is null;

       UPDATE pm_tab_template set bizcurrent = 'qiyong' where source='0';

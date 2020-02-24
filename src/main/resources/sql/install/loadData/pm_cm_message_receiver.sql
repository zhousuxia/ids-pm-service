--消息设置数据初始化--
insert into CM_MESSAGE_RECEIVER (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, NAME, RECEIVER, RECEIVERLIST)
values ('1', '', '', '', '', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '项目状态更新', '项目成员', '项目成员');
insert into CM_MESSAGE_RECEIVER (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, NAME, RECEIVER, RECEIVERLIST)
values ('2', '', '', '', '', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '计划变更', '计划负责人+父计划负责人+子计划负责人+后置计划负责人', '计划负责人,父计划负责人,子计划负责人,后置计划负责人');
insert into CM_MESSAGE_RECEIVER (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, NAME, RECEIVER, RECEIVERLIST)
values ('3', '', '', '', '', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '计划完工', '计划负责人+后置计划负责人', '计划负责人,后置计划负责人');
insert into CM_MESSAGE_RECEIVER (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, NAME, RECEIVER, RECEIVERLIST)
values ('4', '', '', '', '', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '计划废弃', '计划负责人+后置计划负责人', '计划负责人,后置计划负责人');
insert into CM_MESSAGE_RECEIVER (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, NAME, RECEIVER, RECEIVERLIST)
values ('5', '', '', '', '', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '计划逾期预警', '计划负责人', '计划负责人');

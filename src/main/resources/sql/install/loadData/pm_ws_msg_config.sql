--消息配置--
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f001555277a30155532c982d0007', 'planFinishMsgNotice', '计划完工', '${planName}', '项目【${projectName}】计划【${planName}】已完工，请查看', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '计划完工提醒');
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f00655519dd801555352e06f0145', 'planWarnAndOverNotice', '计划预警和逾期', '${planName}', '项目【${projectName}】计划【${planName}】的结束时间为${planEndTime}，请查看', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '计划预警和预期提醒');
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f0015556eae0015556edaa190000', 'planchangeMsgNotice', '计划变更', '${planName}', '项目【${projectName}】计划【${planName}】已变更，请查看', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '计划变更提醒');
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f001555277a301555286b7090000', 'planInvalidMsgNotice', '计划废弃', '${planName}', '项目【${projectName}】计划【${planName}】已废弃，请查看', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '计划废弃提醒');
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f005555733ad01555738a8320000', 'projectStatusChangeNotice', '项目状态变更', '项目计划', '项目【${projectName}】已【${status}】，请查看', '0', '/ids-pm-web/projectMenuController.do?projectMenu'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true'||chr(38)||'projectId=${projectId}', '项目状态变更提醒');

insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f0065c9f4f01015c9f57e0030009', 'planchangeBackMsgNotice', '计划批量变更驳回', '${planName}', '您提交的变更计划已被驳回，请在【我的任务】已发起中了解详情', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '计划批量变更驳回提醒');

insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f0065c9f4f01015c9f58ee67000a', 'planAssignBackMsgNotice', '计划批量下达驳回', '${planName}', '您提交的计划已被驳回，请在【我的任务】已发起中了解详情', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '计划批量下达驳回提醒');

insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f0056072966d016078207ed9002d', 'flowTaskAssignRejectNotice', '流程分解计划审批驳回通知', '流程分解计划审批驳回通知', '您提交的流程分解已被驳回，请在【我的任务】已发起中了解详情', '', '', '流程分解计划审批驳回通知');


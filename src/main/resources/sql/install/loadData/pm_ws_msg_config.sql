--��Ϣ����--
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f001555277a30155532c982d0007', 'planFinishMsgNotice', '�ƻ��깤', '${planName}', '��Ŀ��${projectName}���ƻ���${planName}�����깤����鿴', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '�ƻ��깤����');
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f00655519dd801555352e06f0145', 'planWarnAndOverNotice', '�ƻ�Ԥ��������', '${planName}', '��Ŀ��${projectName}���ƻ���${planName}���Ľ���ʱ��Ϊ${planEndTime}����鿴', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '�ƻ�Ԥ����Ԥ������');
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f0015556eae0015556edaa190000', 'planchangeMsgNotice', '�ƻ����', '${planName}', '��Ŀ��${projectName}���ƻ���${planName}���ѱ������鿴', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '�ƻ��������');
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f001555277a301555286b7090000', 'planInvalidMsgNotice', '�ƻ�����', '${planName}', '��Ŀ��${projectName}���ƻ���${planName}���ѷ�������鿴', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '�ƻ���������');
insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f005555733ad01555738a8320000', 'projectStatusChangeNotice', '��Ŀ״̬���', '��Ŀ�ƻ�', '��Ŀ��${projectName}���ѡ�${status}������鿴', '0', '/ids-pm-web/projectMenuController.do?projectMenu'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true'||chr(38)||'projectId=${projectId}', '��Ŀ״̬�������');

insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f0065c9f4f01015c9f57e0030009', 'planchangeBackMsgNotice', '�ƻ������������', '${planName}', '���ύ�ı���ƻ��ѱ����أ����ڡ��ҵ������ѷ������˽�����', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '�ƻ����������������');

insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f0065c9f4f01015c9f58ee67000a', 'planAssignBackMsgNotice', '�ƻ������´ﲵ��', '${planName}', '���ύ�ļƻ��ѱ����أ����ڡ��ҵ������ѷ������˽�����', '0', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${planId}'||chr(38)||'tabId=${planId}', '�ƻ������´ﲵ������');

insert into ws_msg_config (ID, CONFIGKEY, CONFIGNAME, TITLE, CONTENT, TYPE, URL, DESCRIPTION)
values ('4028f0056072966d016078207ed9002d', 'flowTaskAssignRejectNotice', '���̷ֽ�ƻ���������֪ͨ', '���̷ֽ�ƻ���������֪ͨ', '���ύ�����̷ֽ��ѱ����أ����ڡ��ҵ������ѷ������˽�����', '', '', '���̷ֽ�ƻ���������֪ͨ');


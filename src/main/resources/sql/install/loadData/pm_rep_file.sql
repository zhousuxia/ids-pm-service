--��Ŀ���ĵ����ͣ�idsProjectFile-projectFile��
insert into rep_file_type (ID, PARENTID, PARENTNAME, AVALIABLE, BIZCURRENT, BIZID, SECURITYLEVEL, POLICY_ID, FILETYPENAME, LIFECYCLEPOLICYID, LIFECYCLEPOLICYNAME, ICONID, ICONNAME, GENERATERULEID, GENERATERULENAME, STATUS, CREATENAME, UPDATEBY, UPDATENAME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, CREATETIME, UPDATETIME,FILETYPECODE)
values ('4028ef2d504608ba0150462418bf0001', '4028f00c549ecc8401549eea71950015', '�洢���ļ�����', '1', 'shengxiao', 'c2713517-59f7-45ac-a2e7-5d2e01eb08b3', '1', '1001', 'idsProjcetFile', '4028ef434d656636014d6586752e000b', 'PROJECT_DOCUMENT', '4028ef354b5d7bb1014b5d7cfd8c0002', 'ͼƬ', '4028ef2d506ff0c901506ff554f30000', 'projcetFile', '1', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', '��������Ա', '��������Ա', to_date('23-10-2015 10:41:15', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2015 10:41:15', 'dd-mm-yyyy hh24:mi:ss'),'projectFile');
--�ĵ����͵ĸ�������
insert into REP_FILE_TYPE_ATTRIBUTE (ID, CREATEBY, CREATENAME, CREATETIME, UPDATEBY, UPDATENAME, UPDATETIME, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, SECURITYLEVEL, POLICY_ID, FILETYPEID, NAME, REQUIRED, TITLE, TYPE, CREATEFULLNAME, UPDATEFULLNAME)
values ('4028ef2d5092878f0150929256570004', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_timestamp('08-10-2015 14:29:42.370000', 'dd-mm-yyyy hh24:mi:ss.ff'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_timestamp('08-10-2015 14:29:42.370000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', 'shengxiao', 'b5b124e7-4660-4298-983c-535358eba4bb', null, 1, '1001', '4028ef2d504608ba0150462418bf0001', 'text', 0, '�ı�', 'string', '��������Ա', '��������Ա');
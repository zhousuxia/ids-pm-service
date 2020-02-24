--我的计划
insert into PLANVIEW_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, ISDEFAULT, NAME, PUBLISHDEPT, PUBLISHPERSON, PUBLISHTIME, STATUS)
values ('4028f00763ba3b200163ba63fd26000c', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2018 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), 'true', '我的计划', '', '4028ef354b5d7bb1014b5d7cffa10043', to_timestamp('01-06-2018 16.08.25.126000', 'dd-mm-yyyy hh24:mi:ss.ff'), 'PUBLIC');

--全部计划
insert into PLANVIEW_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, ISDEFAULT, NAME, PUBLISHDEPT, PUBLISHPERSON, PUBLISHTIME, STATUS)
values ('4028f00763ba3b200163ba6446bf000f', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2018 15.28.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), 'true', '全部计划', '', '4028ef354b5d7bb1014b5d7cffa10043', to_timestamp('01-06-2018 16.08.43.967000', 'dd-mm-yyyy hh24:mi:ss.ff'), 'PUBLIC');

--查询条件
insert into PLANVIEW_SEARCH_CONDITION (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, ATTRIBUTENAME, ATTRIBUTEVALUE, PLANVIEWINFOID)
values ('4028f00763ba3b200163ba6446c30011', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2018 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), 'myPlan', '', '4028f00763ba3b200163ba63fd26000c');

--设置条件
insert into PLANVIEW_SET_CONDITION (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, DEPARTMENTID, PLANVIEWINFOID, SHOWRANGE, TIMERANGE)
values ('4028f00763b8df390163b91d75441008', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2018 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), '', '4028f00763ba3b200163ba63fd26000c', '', '');

insert into PLANVIEW_SET_CONDITION (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, DEPARTMENTID, PLANVIEWINFOID, SHOWRANGE, TIMERANGE)
values ('4028f00763b8df390163b91d75442008', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2018 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), '', '4028f00763ba3b200163ba6446bf000f', '', '');

--关联的用户、项目
insert into PLANVIEW_APPLY_PROJECT (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, PLANVIEWINFOID, PROJECTID)
values ('4028f00763ba3b200163ba6446c30014', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2018 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), '4028f00763ba3b200163ba63fd26000c', '');

insert into PLANVIEW_APPLY_PROJECT (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, PLANVIEWINFOID, PROJECTID)
values ('4028f00763ba3b200163ba6446c30015', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2018 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), '4028f00763ba3b200163ba6446bf000f', '');

insert into PLANVIEW_APPLY_USER (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, PLANVIEWINFOID, USERID)
values ('4028f00763ba3b200163ba6446c30016', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2018 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), '4028f00763ba3b200163ba63fd26000c', '');

insert into PLANVIEW_APPLY_USER (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, PLANVIEWINFOID, USERID)
values ('4028f00763ba3b200163ba6446c30017', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2018 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), '4028f00763ba3b200163ba6446bf000f', '');



----
insert into PLANVIEW_COLUMN_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, COLUMNID, PLANVIEWINFOID)
values ('4028f00763cd7cb70163cea6fdb90018', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2015 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), '', '', '', '', '', '', '', '', 'planNo,planLevel,planType,planTaskType,status,owner,planStartTime,planEndTime,assigner,assignTime,workTime,preposePlan,mileStone,creator,createTime', '4028f00763ba3b200163ba63fd26000c');

insert into PLANVIEW_COLUMN_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, COLUMNID, PLANVIEWINFOID)
values ('4028f00763c85b5b0163c942fba90023', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_timestamp('13-06-2015 15.27.00.787000', 'dd-mm-yyyy hh24:mi:ss.ff'), '', '', '', '', '', '', '', '', 'planNo,planLevel,planType,planTaskType,status,owner,planStartTime,planEndTime,assigner,assignTime,workTime,preposePlan,mileStone,creator,createTime', '4028f00763ba3b200163ba6446bf000f');

delete from REP_FILE_TYPE_ATTRIBUTE where id='4028ef2d5092878f0150929256570004';
delete from rep_file_type where id='4028ef2d504608ba0150462418bf0001';

--项目库文档类型（idsProjectFile-projectFile）
insert into rep_file_type (ID, PARENTID, PARENTNAME, AVALIABLE, BIZCURRENT, BIZID, SECURITYLEVEL, POLICY_ID, FILETYPENAME, LIFECYCLEPOLICYID, LIFECYCLEPOLICYNAME, ICONID, ICONNAME, GENERATERULEID, GENERATERULENAME, STATUS, CREATENAME, UPDATEBY, UPDATENAME, CREATEBY, CREATEFULLNAME, UPDATEFULLNAME, CREATETIME, UPDATETIME,FILETYPECODE)
values ('4028ef2d504608ba0150462418bf0001', '4028f00c549ecc8401549eea71950015', '存储库文件类型', '1', 'shengxiao', 'c2713517-59f7-45ac-a2e7-5d2e01eb08b3', '1', '1001', 'idsProjcetFile', '4028ef434d656636014d6586752e000b', 'PROJECT_DOCUMENT', '4028ef354b5d7bb1014b5d7cfd8c0002', '图片', '4028ef2d506ff0c901506ff554f30000', 'projcetFile', '启用', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', '超级管理员', to_date('23-10-2015 10:41:15', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2015 10:41:15', 'dd-mm-yyyy hh24:mi:ss'),'projectFile');
--文档类型的附加属性
insert into REP_FILE_TYPE_ATTRIBUTE (ID, CREATEBY, CREATENAME, CREATETIME, UPDATEBY, UPDATENAME, UPDATETIME, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, SECURITYLEVEL, POLICY_ID, FILETYPEID, NAME, REQUIRED, TITLE, TYPE, CREATEFULLNAME, UPDATEFULLNAME)
values ('4028ef2d5092878f0150929256570004', '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_timestamp('08-10-2015 14:29:42.370000', 'dd-mm-yyyy hh24:mi:ss.ff'), '4028ef354b5d7bb1014b5d7cffa10043', 'gladmin', to_timestamp('08-10-2015 14:29:42.370000', 'dd-mm-yyyy hh24:mi:ss.ff'), '1', 'shengxiao', 'b5b124e7-4660-4298-983c-535358eba4bb', null, 1, '1001', '4028ef2d504608ba0150462418bf0001', 'text', 0, '文本', 'string', '超级管理员', '超级管理员');

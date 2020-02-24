-------来源系统的页签------------
INSERT INTO PM_ACTIVITY_TYPE_MANAGE (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID,
                                     CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY,
                                     PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL,
                                     UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, AVALIABLE,
                                     CODE, NAME, REMAKE, STATUS, SOURCE, REMARK)
VALUES ('4028f00c6cefbdc9016cefcd30c00007', null, '4028ef506b08a241016b25649af70082', null, null, null,
        TO_TIMESTAMP('2019-09-02 10:28:17.706000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, '1', 'NZ001', '研发类', null, 'enable', '0', '研发类');

INSERT INTO PM_ACTIVITY_TYPE_MANAGE (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID,
                                     CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY,
                                     PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL,
                                     UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, AVALIABLE,
                                     CODE, NAME, REMAKE, STATUS, SOURCE, REMARK)
VALUES ('4028f00c6cefbdc9016cf00132700008', null, '4028ef506b08a241016b25649af70082', null, null, null,
        TO_TIMESTAMP('2019-09-02 11:25:06.027000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, '1', 'NZ002', '风险管理类', null, 'enable', '0', null);

INSERT INTO PM_ACTIVITY_TYPE_MANAGE (ID, CONTAINERPATH, CREATEBY, CREATEFULLNAME, CREATENAME, CREATEORGID,
                                     CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3, GLOBJPARENTID, INSTANCEENTITY,
                                     PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID, SECURITYLEVEL,
                                     UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATEORGID, UPDATETIME, AVALIABLE,
                                     CODE, NAME, REMAKE, STATUS, SOURCE, REMARK)
VALUES ('4028f00c6cefbdc9016cf0016c1d0009', null, '4028ef506b08a241016b25649af70082', null, null, null,
        TO_TIMESTAMP('2019-09-02 11:25:20.789000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, '1', 'NZ003', '评审类', null, 'enable', '0', null);

insert into PM_ACTIVITY_TYPE_MANAGE (ID, AVALIABLE, CODE, NAME, STATUS, SOURCE)
values ('4028f00d6db34426016db365b27c0000', '1', 'NZ004', 'PLM计划类', 'enable', '0');

------------活动类型编号规则--------
create sequence SEQ_GW20190902134814 minvalue 0 maxvalue 999 start with 1 increment by 1 nocache;

INSERT INTO SN_GENERATOR_INFO (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME,
                               UPDATENAME, UPDATETIME, CURVAL, DESCRIPTION, GENERATORCLASSNAME, NAME, NEXTVAL,
                               DELETEFLAG, EXT1, EXT2, EXT3, GENERATERULEID, GENERATORTYPE, STATUS, CREATEORGID,
                               SECURITYLEVEL, UPDATEORGID, CONTAINERPATH, GLOBJPARENTID, INSTANCEENTITY,
                               PARENTOBJECTBIZID, PARENTOBJECTNAME, PARENTSYSTABLEID)
VALUES ('4028ef506ce011be016cf06e711d0135', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin',
        TO_TIMESTAMP('2019-09-02 13:24:25.501000', 'YYYY-MM-DD HH24:MI:SS.FF6'), '4028ef354b5d7bb1014b5d7cffa10043',
        '超级管理员', 'gladmin', TO_TIMESTAMP('2019-09-02 13:25:34.232000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null,
        'AT001 , 永不归零', null, '活动类型编号', null, null, null, null, null, '4028ef506ce011be016cf06e710e0134', '1', '1',
        null, null, null, null, null, null, null, null, null);

INSERT INTO SN_GENERATOR_RULE (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3,
                               UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, DATEFORMATSTR,
                               GENERATORRULEVALUE, POWERVALUE, PREFIXSTR, STARTVALUE, STEP, SUFFIXSTR,
                               WARNINGVALVE, ZEROTYPE, CREATEORGID, SECURITYLEVEL, UPDATEORGID, CONTAINERPATH,
                               GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME,
                               PARENTSYSTABLEID, PREFIXSUFFIXGENERATORCLASSNAME)
VALUES ('4028ef506ce011be016cf06e710e0134', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin',
        TO_TIMESTAMP('2019-09-02 13:24:25.486000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, null, null, null,
        '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin',
        TO_TIMESTAMP('2019-09-02 13:24:25.486000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, null, 3, 'AT', 1, 1, null, null,
        '0', null, null, null, null, null, null, null, null, null, null);


INSERT INTO SN_GENERATOR_RULE_RELATE (ID, CLASSNAME, PROPERTYNAME, SEQUENCENAME, DESCRIPTION, CREATEBY,
                                      CREATEFULLNAME, CREATENAME, CREATETIME, DELETEFLAG, EXT1, EXT2, EXT3,
                                      UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, INITVALUE,
                                      GENERATORINFOID, CREATEORGID, SECURITYLEVEL, UPDATEORGID, CONTAINERPATH,
                                      GLOBJPARENTID, INSTANCEENTITY, PARENTOBJECTBIZID, PARENTOBJECTNAME,
                                      PARENTSYSTABLEID)
VALUES ('4028ef506ce011be016cf0843f420145', 'ActivityTypeCode', null, 'SEQ_GW20190902134814', null,
        '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin',
        TO_TIMESTAMP('2019-09-02 13:48:14.415000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, null, null, null,
        '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin',
        TO_TIMESTAMP('2019-09-02 13:48:14.415000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 1, '4028ef506ce011be016cf06e711d0135',
        null, null, null, null, null, null, null, null, null);


-----------

INSERT INTO T_S_FUNCTION (ID, FUNCTIONIFRAME, FUNCTIONLEVEL, FUNCTIONNAME, FUNCTIONORDER, FUNCTIONURL,
                          PARENTFUNCTIONID, ICONID, FUNCTIONTYPE, FUNCTIONCLS, APPKEY, APPNAME, MENUTYPE, APPTYPE,
                          MENUAPPTYPE)
VALUES ('4028ef506cd0cce3016cd25141eb01bb', null, 1, '活动类型管理', '3',
        'activityTypeManageController.do?activityTypeManagePage' || chr(38) || 'isIframe=true',
        '4028efec4cdf892b014cdf8e37b10005',
        '4028ef354b5d7bb1014b5d7cfd8c0002', null, null, null, null, '0', null, null);

INSERT INTO T_S_FUNCTION_RELATE (ID, FUNCTIONID, PARENTFUNCTIONID)
VALUES ('4028ef506cd0cce3016cd25141eb01bd', '4028ef506cd0cce3016cd25141eb01bb', '4028efec4cdf892b014cdf8e37b10005');

---------------菜单按钮权限------------------------

INSERT INTO T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
VALUES ('4028ef506ce011be016cf59e5ac70950', 'addActivityTypebtn', null, '新增', null, '4028ef506cd0cce3016cd25141eb01bb',
        null);
INSERT INTO T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
VALUES ('4028ef506ce011be016cf59e7b520952', 'deleteActiviryBtn', null, '删除', null, '4028ef506cd0cce3016cd25141eb01bb',
        null);
INSERT INTO T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
VALUES ('4028ef506ce011be016cf59e9dfe0954', 'activityStartTableBtn', null, '启用', null,
        '4028ef506cd0cce3016cd25141eb01bb', null);
INSERT INTO T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
VALUES ('4028ef506ce011be016cf59eb91c0956', 'activityStopTableBtn', null, '禁用', null,
        '4028ef506cd0cce3016cd25141eb01bb', null);
INSERT INTO T_S_OPERATION (ID, OPERATIONCODE, OPERATIONICON, OPERATIONNAME, STATUS, FUNCTIONID, ICONID)
VALUES ('4028ef506ce011be016cf59edb3c0958', 'updateActivityBtn', null, '修改', null, '4028ef506cd0cce3016cd25141eb01bb',
        null);


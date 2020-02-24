insert into PM_TEMPLATE_MENU (ID, CREATEBY, CREATENAME, CREATETIME, UPDATEBY, UPDATENAME, UPDATETIME, ICONCLS, PARENTID, SORT, STATUS, TEXT, URL)
values ('001', '', '', null, '', '', null, '', '', null, 1, '项目模板', '');
insert into PM_TEMPLATE_MENU (ID, CREATEBY, CREATENAME, CREATETIME, UPDATEBY, UPDATENAME, UPDATETIME, ICONCLS, PARENTID, SORT, STATUS, TEXT, URL)
values ('002', '', '', null, '', '', null, '', '001', 1, 1, '详细信息', 'projTemplateController.do?goTemplateDetail');
insert into PM_TEMPLATE_MENU (ID, CREATEBY, CREATENAME, CREATETIME, UPDATEBY, UPDATENAME, UPDATETIME, ICONCLS, PARENTID, SORT, STATUS, TEXT, URL)
values ('003', '', '', null, '', '', null, '', '001', 2, 1, '计划', 'projTemplateController.do?goTemplatePlan');
insert into PM_TEMPLATE_MENU (ID, CREATEBY, CREATENAME, CREATETIME, UPDATEBY, UPDATENAME, UPDATETIME, ICONCLS, PARENTID, SORT, STATUS, TEXT, URL)
values ('004', '', '', null, '', '', null, '', '001', 3, 1, '团队', 'projTemplateController.do?goProjTemplateTeam');
insert into PM_TEMPLATE_MENU (ID, CREATEBY, CREATENAME, CREATETIME, UPDATEBY, UPDATENAME, UPDATETIME, ICONCLS, PARENTID,  SORT, STATUS, TEXT, URL)
values ('005', '', '', null, '', '', null, '', '001', 4, 1, '项目库', 'projTemplateController.do?goTemplateLib');

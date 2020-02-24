--初始化项目树
insert into PM_MENU (ID, PARENTID, STATUS, TEXT, UNDERPROJECT, URL, INSERTRECENTLY, SORT)
values ('001', '', 1, '项目', 0, '', 0, null);

insert into PM_MENU (ID, PARENTID, STATUS, TEXT, UNDERPROJECT, URL, INSERTRECENTLY, SORT)
values ('005', '001', 1, '项目列表', 0, '/ids-pm-web/projectController.do?project'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', 0, null);

insert into PM_MENU (ID, PARENTID, STATUS, TEXT, UNDERPROJECT, URL, INSERTRECENTLY, SORT)
values ('006', '001', 1, '最近访问', 0, '', 0, null);

insert into PM_MENU (ID, PARENTID, STATUS, TEXT, UNDERPROJECT, URL, INSERTRECENTLY, SORT)
values ('002', '006', 1, '项目', 0, '', 0, null);

insert into PM_MENU (ID, PARENTID, STATUS, TEXT, UNDERPROJECT, URL, INSERTRECENTLY, SORT)
values ('007', '002', 1, '详细信息', 1, '/ids-pm-web/projectController.do?viewDetailBaseInfo'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', 1, 1);

insert into PM_MENU (ID, PARENTID, STATUS, TEXT, UNDERPROJECT, URL, INSERTRECENTLY, SORT)
values ('004', '002', 1, '计划', 1, '/ids-pm-web/planController.do?plan'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', 1, 2);

insert into PM_MENU (ID, PARENTID, STATUS, TEXT, UNDERPROJECT, URL, INSERTRECENTLY, SORT)
values ('003', '002', 1, '团队', 1, '/ids-pm-web/projRolesController.do?getProjRolesAndUsers'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', 1, 4);

insert into PM_MENU (ID, PARENTID, STATUS, TEXT, UNDERPROJECT, URL, INSERTRECENTLY, SORT)
values ('008', '002', 1, '项目库', 1, '/ids-pm-web/projLibController.do?goProjLibLayout'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', 1, 5);

insert into PM_MENU (ID, PARENTID, STATUS, TEXT, UNDERPROJECT, URL, INSERTRECENTLY, SORT)
values ('009', '002', 1, '统计分析', 1, '/ids-pm-web/projStatisticsController.do?goStatistics'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true', 1, 7);

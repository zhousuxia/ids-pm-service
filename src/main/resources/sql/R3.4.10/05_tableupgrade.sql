--项目计划门户部件
insert into portalpart_info (ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, SECURITYLEVEL, POLICY_ID, FIT, HEIGHT, MINIMIZABLE, MOREBT, TITLE, URL, WIDTH, ORDERBY,EXT1)
values ('297e26e64e6b637c014e6b69ef320005', '1', 'shengxiao', '91fde6c0-e3da-4da8-8637-2ca2e3293993', '', 1, '1001', '', 400, '', '', '计划预警', '/ids-pm-web/projStatisticsController.do?goProjPortletWarnPage', null, '5','PlanEarlyWarning');

insert into portalpart_info (ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, SECURITYLEVEL, POLICY_ID, FIT, HEIGHT, MINIMIZABLE, MOREBT, TITLE, URL, WIDTH, ORDERBY,EXT1)
values ('297e26e64e75d4c6014e75d9e9790007', '1', 'shengxiao', '9530a640-cfc5-4986-821f-2a0a2c96c4hh', '', 1, '1001', '', 400, '', 'viewMorePb', '项目看板', '/ids-pm-web/statisticalAnalysisController.do?goProjectBoardPortlet', null, '4','ProjectSituation');

insert into portalpart_info (ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, SECURITYLEVEL, POLICY_ID, FIT, HEIGHT, MINIMIZABLE, MOREBT, TITLE, URL, WIDTH, ORDERBY, EXT1)
values ('4028ef506eda78b2016eda84980e004a', '1', 'shengxiao', '2ed7756b-e011-4629-bace-4623b2a42040', '1', 1, '1001', '', 400, '', 'viewMoreProjectPortlet', '项目进度', '/ids-pm-web/projectController.do?projectPortlet', null, '12', '');

insert into portalpart_info (ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, SECURITYLEVEL, POLICY_ID, FIT, HEIGHT, MINIMIZABLE, MOREBT, TITLE, URL, WIDTH, ORDERBY, EXT1)
values ('4028ef506eca27e8016ed91f15260584', '1', 'shengxiao', '32075552-0096-4a56-b747-934a922f919d', '1', 1, '1001', '', 400, '', 'viewMoreProblems', '问题管理', '/ids-riskproblems-web/riskProblemTaskController.do?goProblemsHomePage', null, '11', '');

insert into portalpart_info (ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, SECURITYLEVEL, POLICY_ID, FIT, HEIGHT, MINIMIZABLE, MOREBT, TITLE, URL, WIDTH, ORDERBY, EXT1)
values ('4028ef506eca27e8016eca82b815008f', '1', 'shengxiao', 'ca85d8e0-7937-41d3-a603-ef6a632d4000', '1', null, '1001', '', null, '', '/ids-review-web/reviewFlowController.do?review'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true'||chr(38)||'functionId=4028efed516bc68d01516bd116780003', '评审管理', '/ids-review-web/reviewFlowController.do?topReviewManager', null, '9', '');

insert into portalpart_info (ID, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, SECURITYLEVEL, POLICY_ID, FIT, HEIGHT, MINIMIZABLE, MOREBT, TITLE, URL, WIDTH, ORDERBY, EXT1)
values ('4028ef506eca27e8016ecfec1fd102bb', '1', 'shengxiao', 'b88f79e2-252b-48a1-8c8e-787b30dd4eaa', '1', null, '1001', '', null, '', '', '项目计划统计', '/ids-pm-web/planController.do?goProjectPlanStatistic', null, '10', '');

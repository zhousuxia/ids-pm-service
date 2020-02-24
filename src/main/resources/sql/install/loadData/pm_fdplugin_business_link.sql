--插件与检索业务关联信息--
insert into fdplugin_business_link (ID, PLUGINCODE, BUSINESSTYPE, LINKINFO)
values ('11', 'pm-plugin', 'SEARCH', 'projectDocSearch');

insert into fdplugin_business_link (ID, PLUGINCODE, BUSINESSTYPE, LINKINFO)
values ('12', 'pm-plugin', 'SEARCH', 'planSearch');

insert into fdplugin_business_link (ID, PLUGINCODE, BUSINESSTYPE, LINKINFO)
values ('13', 'pm-plugin', 'SEARCH', 'projectSearch');

--插件与首页部件业务关联信息-项目--
insert into fdplugin_business_link (ID, PLUGINCODE, BUSINESSTYPE, LINKINFO)
values ('14', 'pm-plugin', 'PORTALPART', 'projectController.do?projectPortlet');
--插件与首页部件-项目看板--
insert into fdplugin_business_link (ID, PLUGINCODE, BUSINESSTYPE, LINKINFO)
values ('15', 'pm-plugin', 'PORTALPART', 'statisticalAnalysisController.do?goProjectBoardPortlet');
--插件与首页部件-计划预警--
insert into fdplugin_business_link (ID, PLUGINCODE, BUSINESSTYPE, LINKINFO)
values ('16', 'pm-plugin', 'PORTALPART', 'projStatisticsController.do?goProjPortletWarnPage');
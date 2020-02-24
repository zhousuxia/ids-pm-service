--新增反馈风险等级
delete from t_s_type where TYPECODE in(''nothing'',''low'',''high'',''middle'');
delete from t_s_typegroup  where TYPEGROUPCODE in (''risk'');

insert into T_S_TYPEGROUP (ID, TYPEGROUPCODE, TYPEGROUPNAME)
values (''4028ef584d462c99014d478954480179'', ''risk'', ''反馈风险等级'');

insert into T_S_TYPE (ID,TYPECODE,TYPENAME,TYPEPID,TYPEGROUPID)
values (''4028ef584d462c99014d4789a287017b'',''nothing'',''无'',null,''4028ef584d462c99014d478954480179'');
insert into T_S_TYPE (ID,TYPECODE,TYPENAME,TYPEPID,TYPEGROUPID)
values (''4028ef584d462c99014d4789dcb1017d'',''low'',''低'',null,''4028ef584d462c99014d478954480179'');
insert into T_S_TYPE (ID,TYPECODE,TYPENAME,TYPEPID,TYPEGROUPID)
values (''4028ef584d462c99014d478a141d017f'',''high'',''高'',null,''4028ef584d462c99014d478954480179'');
insert into T_S_TYPE (ID,TYPECODE,TYPENAME,TYPEPID,TYPEGROUPID)
values (''4028ef584d462c99014d478a432c0181'',''middle'',''中'',null,''4028ef584d462c99014d478954480179'');

--增加研发任务类型
insert into t_s_type (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028ef324ebe0c2f014ebe1c3a380008'', ''taskReportDevelop'', ''研发任务'', '''', ''4028ef324ebe0c2f014ebe1bc7990006'');

--业务编号字典表信息--
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f007598760a5015987698734000f'', ''riskCategory'', ''风险分类'', '''', ''4028b88159064c60015906afa892003c'');
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f00159a4ef920159a512bf43001e'', ''standardProblem'', ''问题名称'', '''', ''4028b88159064c60015906afa892003c'');
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f00159a6b4b50159a6bc258e000c'', ''riskModel'', ''风险指标体系'', '''', ''4028b88159064c60015906afa892003c'');
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f007598603bc0159861240ca0009'', ''epsConfig'', ''项目分类'', '''', ''4028b88159064c60015906afa892003c'');
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f001598771c40159878f6c8f000e'', ''riskTarget'', ''风险指标'', '''', ''4028b88159064c60015906afa892003c'');


update T_S_TYPE set TYPESTATUS=''1'';
update T_S_TYPEGROUP set TYPEGROUPSTATUS=''1'';
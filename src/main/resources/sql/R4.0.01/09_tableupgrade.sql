--�����������յȼ�
delete from t_s_type where TYPECODE in(''nothing'',''low'',''high'',''middle'');
delete from t_s_typegroup  where TYPEGROUPCODE in (''risk'');

insert into T_S_TYPEGROUP (ID, TYPEGROUPCODE, TYPEGROUPNAME)
values (''4028ef584d462c99014d478954480179'', ''risk'', ''�������յȼ�'');

insert into T_S_TYPE (ID,TYPECODE,TYPENAME,TYPEPID,TYPEGROUPID)
values (''4028ef584d462c99014d4789a287017b'',''nothing'',''��'',null,''4028ef584d462c99014d478954480179'');
insert into T_S_TYPE (ID,TYPECODE,TYPENAME,TYPEPID,TYPEGROUPID)
values (''4028ef584d462c99014d4789dcb1017d'',''low'',''��'',null,''4028ef584d462c99014d478954480179'');
insert into T_S_TYPE (ID,TYPECODE,TYPENAME,TYPEPID,TYPEGROUPID)
values (''4028ef584d462c99014d478a141d017f'',''high'',''��'',null,''4028ef584d462c99014d478954480179'');
insert into T_S_TYPE (ID,TYPECODE,TYPENAME,TYPEPID,TYPEGROUPID)
values (''4028ef584d462c99014d478a432c0181'',''middle'',''��'',null,''4028ef584d462c99014d478954480179'');

--�����з���������
insert into t_s_type (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028ef324ebe0c2f014ebe1c3a380008'', ''taskReportDevelop'', ''�з�����'', '''', ''4028ef324ebe0c2f014ebe1bc7990006'');

--ҵ�����ֵ����Ϣ--
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f007598760a5015987698734000f'', ''riskCategory'', ''���շ���'', '''', ''4028b88159064c60015906afa892003c'');
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f00159a4ef920159a512bf43001e'', ''standardProblem'', ''��������'', '''', ''4028b88159064c60015906afa892003c'');
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f00159a6b4b50159a6bc258e000c'', ''riskModel'', ''����ָ����ϵ'', '''', ''4028b88159064c60015906afa892003c'');
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f007598603bc0159861240ca0009'', ''epsConfig'', ''��Ŀ����'', '''', ''4028b88159064c60015906afa892003c'');
insert into T_S_TYPE (ID, TYPECODE, TYPENAME, TYPEPID, TYPEGROUPID)
values (''4028f001598771c40159878f6c8f000e'', ''riskTarget'', ''����ָ��'', '''', ''4028b88159064c60015906afa892003c'');


update T_S_TYPE set TYPESTATUS=''1'';
update T_S_TYPEGROUP set TYPEGROUPSTATUS=''1'';
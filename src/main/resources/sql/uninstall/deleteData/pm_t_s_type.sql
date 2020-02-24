--新增反馈风险等级
delete from T_S_TYPE where TYPEGROUPID = '4028ef584d462c99014d478954480179';
delete from T_S_TYPEGROUP where id = '4028ef584d462c99014d478954480179';

--增加研发任务类型
delete from T_S_TYPE where id = '4028ef324ebe0c2f014ebe1c3a380008';

--业务编号字典表信息--
delete from T_S_TYPE where id = '4028f007598760a5015987698734000f';
delete from T_S_TYPE where id = '4028f00159a4ef920159a512bf43001e';
delete from T_S_TYPE where id = '4028f00159a6b4b50159a6bc258e000c';
delete from T_S_TYPE where id = '4028f007598603bc0159861240ca0009';
delete from T_S_TYPE where id = '4028f001598771c40159878f6c8f000e';
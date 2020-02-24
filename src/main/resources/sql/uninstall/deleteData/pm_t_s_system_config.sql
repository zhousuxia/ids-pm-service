--IDS3.4webSocket配置，如需执行在insertOrder中添加--
--删除系统默认配置信息
delete from T_S_SYSTEM_CONFIG where id = '4028f0055d7777d0015d7c7e40050022';
delete from T_S_SYSTEM_CONFIG where id = '4028ef3b5261be8b015261c5377e0000';

--新增系统默认配置信息
delete from T_S_SYSTEM_CONFIG where id = '4028f0125f3317aa015f332642020015';

delete from t_s_system_config where id = '4028f0065f5cc1c7015f5ccd4ffc0013';

delete from t_s_system_config where id = '4028efe55082fef4015083eda5220092';

--PM访问RDF--
delete from T_S_SYSTEM_CONFIG where id='4028f00162f1794c0162f1c016290015';
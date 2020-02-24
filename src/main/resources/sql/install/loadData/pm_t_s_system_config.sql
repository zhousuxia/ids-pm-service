--IDS3.4webSocket配置，如需执行在insertOrder中添加--
--IDS访问IDS-REVIEW--
insert into T_S_SYSTEM_CONFIG (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028f0055d7777d0015d7c7e40050022', 'IDS_REVIEW_Task_Support', 'http://127.0.0.1:8080/foundation/support/reviewSupport?wsdl', 'webservice', 'IDS调用REVIEW任务的webservice地址');
insert into t_s_system_config (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028ef3b5261be8b015261c5377e0000', 'IDS_REVIEW_ViewTask_HttpUrl', 'http://127.0.0.1:8080/foundation/reviewFlowController.do?goCheckReview', 'http', 'IDS访问REVIEW任务的http地址');

--IDS访问KLM--
insert into t_s_system_config (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028f0065f5cc1c7015f5ccd4ffc0013', 'IDS_KLM_Knowledge_Support', 'http://127.0.0.1:8080/foundation/support/KLMKnowledgeSupport?wsdl', 'webservice', 'IDS调用KLM知识的webservice地址');
insert into t_s_system_config (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028efe55082fef4015083eda5220092', 'IDS_KLM_HttpUrl', 'http://127.0.0.1:8080/foundation', 'http', 'IDS访问KLM的http地址');

--IDS访问KDD--
insert into t_s_system_config (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028f0125f3317aa015f332642020015', 'IDS_KDD_Product_Support', 'http://127.0.0.1:8080/foundation/support/KDDProductSupport?wsdl', 'webservice', 'IDS调用KDD产品的webservice地址');

--PM访问RDF--
insert into T_S_SYSTEM_CONFIG (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028f00162f1794c0162f1c016290015', 'PM_RDF_Task_Support', 'http://127.0.0.1:8080/foundation/support/RDFTaskSupport?wsdl', 'webservice', 'PM调用RDFLOW任务的webservice地址');

--IDS3.4webSocket���ã�����ִ����insertOrder�����--
--IDS����IDS-REVIEW--
insert into T_S_SYSTEM_CONFIG (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028f0055d7777d0015d7c7e40050022', 'IDS_REVIEW_Task_Support', 'http://127.0.0.1:8080/foundation/support/reviewSupport?wsdl', 'webservice', 'IDS����REVIEW�����webservice��ַ');
insert into t_s_system_config (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028ef3b5261be8b015261c5377e0000', 'IDS_REVIEW_ViewTask_HttpUrl', 'http://127.0.0.1:8080/foundation/reviewFlowController.do?goCheckReview', 'http', 'IDS����REVIEW�����http��ַ');

--IDS����KLM--
insert into t_s_system_config (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028f0065f5cc1c7015f5ccd4ffc0013', 'IDS_KLM_Knowledge_Support', 'http://127.0.0.1:8080/foundation/support/KLMKnowledgeSupport?wsdl', 'webservice', 'IDS����KLM֪ʶ��webservice��ַ');
insert into t_s_system_config (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028efe55082fef4015083eda5220092', 'IDS_KLM_HttpUrl', 'http://127.0.0.1:8080/foundation', 'http', 'IDS����KLM��http��ַ');

--IDS����KDD--
insert into t_s_system_config (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028f0125f3317aa015f332642020015', 'IDS_KDD_Product_Support', 'http://127.0.0.1:8080/foundation/support/KDDProductSupport?wsdl', 'webservice', 'IDS����KDD��Ʒ��webservice��ַ');

--PM����RDF--
insert into T_S_SYSTEM_CONFIG (ID, KEY, VALUE, TYPE, CONTENT)
values ('4028f00162f1794c0162f1c016290015', 'PM_RDF_Task_Support', 'http://127.0.0.1:8080/foundation/support/RDFTaskSupport?wsdl', 'webservice', 'PM����RDFLOW�����webservice��ַ');

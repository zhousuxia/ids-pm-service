--��Ŀ�ƻ��Ż�����
update portalpart_info set APPKEY='ids-pm-web',MOREBT='/ids-pm-web/statisticalAnalysisController.do?goProjectBoard'||chr(38)||'isIframe=true' where id='297e26e64e75d4c6014e75d9e9790007';
update portalpart_info set APPKEY='ids-pm-web' where id='297e26e64e6b637c014e6b69ef320005';

--��������Ż�����--
update portalpart_info set APPKEY='ids-riskproblems-web',MOREBT='/ids-riskproblems-web/riskProblemTaskController.do?goProblemList'||chr(38)||'isIframe=true' where id='4028ef506eca27e8016ed91f15260584';

--��Ŀ����--
update portalpart_info set APPKEY='ids-pm-web',MOREBT='/ids-pm-web/projectMenuController.do?projectMenu'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true' where id='4028ef506eda78b2016eda84980e004a';

--�������--
update portalpart_info set APPKEY='ids-pm-web',MOREBT='/ids-review-web/reviewFlowController.do?review'||chr(38)||'isIframe=true'||chr(38)||'afterIframe=true' where id='4028ef506eca27e8016eca82b815008f';


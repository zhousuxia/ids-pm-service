--基本信息
update pm_object_property_info set OPERATIONEVENT = 'onChange="Plan.ownerChange()"' where id='297e02af6d18ed7d016d19d384b5001c';
update pm_object_property_info set OPERATIONEVENT = 'onChange="Plan.workTimeLinkage(''workTime'')"' where id='297e02af6d18ed7d016d19d9493e001f';
update pm_object_property_info set OPERATIONEVENT = 'onChange="Plan.milestoneChange()"' where id='297e02af6d18ed7d016d19deb7580020';
update pm_object_property_info set OPERATIONEVENT = 'onChange="Plan.workTimeLinkage(''planStartTime'')"' where id='297e02af6d18ed7d016d19dff0a30021';
update pm_object_property_info set OPERATIONEVENT = 'onChange="Plan.workTimeLinkage(''planEndTime'')"' where id='297e02af6d18ed7d016d19dff0a60022';
--输入
update pm_object_property_info set FORMAT = 'Inputs.addLink' where id='297e02af6d18ed7d016d18f341b50006';
update pm_object_property_info set FORMAT = 'Inputs.showOrigin' where id='297e02af6d18ed7d016d18f341b70007';
update pm_object_property_info set OPERATIONEVENT = 'Inputs.addLocalFile()' where id='297e02af6d1a664d016d1a97bd860007';
update pm_object_property_info set OPERATIONEVENT = 'Inputs.deleteSelectionsInputs(''inputsList'', ''inputsController.do?doBatchDelInputs'')' where id='297e02af6d1a664d016d1a97bd880008';
update pm_object_property_info set OPERATIONEVENT = 'Inputs.goProjLibLink()' where id='297e02af6d1a664d016d1a97bd8b0009';
update pm_object_property_info set OPERATIONEVENT = 'Inputs.goPlanLink()' where id='297e02af6d1a664d016d1a97bd8d000a';
update pm_object_property_info set OPERATIONEVENT = 'Inputs.addInputsNew()' where id='297e02af6d1a664d016d1a8ce42e00a1';
--输出
update pm_object_property_info set FORMAT = 'DeliverablesInfo.getDeliverableName' where id='297e02af6d1ef39c016d1efa723e0006';
update pm_object_property_info set FORMAT = 'DeliverablesInfo.addLink' where id='297e02af6d1ef39c016d1efa72410007';
update pm_object_property_info set OPERATIONEVENT = 'DeliverablesInfo.addDeliverable()' where id='297e02af6d1ef39c016d1efa72350003';
--资源
update pm_object_property_info set FORMAT = 'ResourceLinkInfo.resourceNameLink' where id='297e02af6d1e0823016d1eebaf480092';
update pm_object_property_info set FORMAT = 'ResourceLinkInfo.viewUseRate2' where id='297e02af6d1e0823016d1eebaf500096';
update pm_object_property_info set OPERATIONEVENT = 'ResourceLinkInfo.addResource()' where id='297e02af6d1e0823016d1eebaf41008f';
update pm_object_property_info set OPERATIONEVENT = 'ResourceLinkInfo.deleteSelections2(''resourceList'', ''resourceLinkInfoController.do?doBatchDel'')' where id='297e02af6d1e0823016d1eebaf430090';
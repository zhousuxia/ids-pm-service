delete from FD_TASK_CONFIG where CONFIGKEY in ('approveLaunchedTask','approveTodoTask','approveFinishedTask');

--工作台任务-审批任务初始化数据--
insert into FD_TASK_CONFIG (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, CONFIGKEY, CONFIGNAME, CONFIGTYPE, EXECUTECLASS, EXECUTESQL, EXECUTECOUNTSQL, EXECUTETYPE, HANDLEBUTTON, OPENID, OPENTITLE, OPENTYPE, TASKURL)
values ('120f93cbb2904ec899ba4d095cdb6823', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'approveLaunchedTask', '已发起-审批任务', '2', '', 'select distinct
       to_char(st.taskId) as taskId,
       st.taskNumber as businessId,
       st.taskNumber as taskNumber,
       st.objectname as businessData,
       st.title as taskTitle,
       st.processDefinitionName as processDefinitionName,
       st.type as processDefinitionKey,
       st.status as taskStatus,
       ''taskReportApprove'' as taskType,
       null as taskKey,
       st.title as taskName,
       st.creater as launcher,
       to_char(st.starttime, ''yyyy-MM-dd HH24:mi:ss'') as launchtime,
       st.createrFullname as launchername,
       st.createrFullname || ''-'' || st.creater as launcherfullname,
       to_char(st.creater) as assignee,
       u.realname as assigneeFullname,
       st.procinstid as processInstanceId,
       st.title as openTitle,
       (case when (st.type = ''qualityExplainReplyProcess'') then ''2'' when (st.type = ''qualityExternalProblemProcess'') then ''2'' when (st.type = ''qualityOtherProblemProcess'') then ''2'' when (st.type = ''qualityGeneralProblemProcess'') then ''2'' else ''1'' end) as openType,
       (case when (tv.text_ is null) then ''myStartedTaskController.do?initActivitiTask'||chr(38)||'isIframe=true'||chr(38)||'procInstId=''||ht.proc_inst_id_||'''||chr(38)||'taskNumber=''||st.taskNumber else tv.text_||'''||chr(38)||'procInstId=''||ht.proc_inst_id_||'''||chr(38)||'taskNumber=''||st.taskNumber end) as taskURL
  from act_fd_started_task st
  left join act_hi_taskinst ht on (ht.proc_inst_id_ = st.procInstId)
  left join act_hi_varinst tv on (ht.proc_inst_id_ = tv.proc_inst_id_ and tv.name_ = ''viewUrl'')
  left join t_s_base_user u on u.id = st.creater
 where st.status not in (''已终止'', ''已删除'')
   and st.type not in (''reviewProcess'', ''practicableProcess'')
   and st.creater = ${user.username}
 order by launchtime desc', 'select count(distinct st.taskId)
  from act_fd_started_task st
  left join act_hi_taskinst ht on (ht.proc_inst_id_ = st.procInstId)
  left join act_hi_varinst tv on (ht.proc_inst_id_ = tv.proc_inst_id_ and tv.name_ = ''viewUrl'')
  left join t_s_base_user u on u.id = st.creater
 where st.status not in (''已终止'', ''已删除'')
   and st.type not in (''reviewProcess'', ''practicableProcess'')
   and st.creater = ${user.username}', 'SQL', '[]', '${taskId}', '${taskName}', '1', 'myStartedTaskController.do?initActivitiTask'||chr(38)||'isIframe=true'||chr(38)||'procInstId=${processInstanceId}'||chr(38)||'taskNumber=${taskNumber}');

insert into FD_TASK_CONFIG (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, CONFIGKEY, CONFIGNAME, CONFIGTYPE, EXECUTECLASS, EXECUTESQL, EXECUTECOUNTSQL, EXECUTETYPE, HANDLEBUTTON, OPENID, OPENTITLE, OPENTYPE, TASKURL)
values ('0c570a4a77594da9afc6a3226b29234a', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'approveTodoTask', '待办-审批任务', '1', '', 'select distinct to_char(T.id_) as id,
                to_char(T.id_) as taskId,
                S.taskNumber as businessId,
                S.taskNumber as taskNumber,
                S.objectName as businessData,
                S.processDefinitionName as processDefinitionName,
                (case when (S.objectName is not null) then to_char(S.objectName ||''-''||S.processDefinitionName ||''-''||T.name_||''：''||T.proc_inst_id_) else to_char(S.processDefinitionName||''-''||T.name_ ||''：''||T.proc_inst_id_) end) as taskTitle,
                S.type as processDefinitionKey,
                T.name_ as taskName,
                ''taskReportApprove'' as taskType,
                S.status as taskStatus,
                T.task_def_key_ as taskKey,
                S.creater as launcher,
                to_char(T.Create_Time_, ''yyyy-MM-dd HH24:mi:ss'') as launchtime,
                S.createrFullname as launchername,
                S.createrFullname || ''-'' || S.creater as launcherfullname,
                T.assignee_ as assignee,
                U.realname as assigneeFullname,
                to_char(T.proc_inst_id_) as processInstanceId,
                to_char(T.id_) as openId,
                (case when (S.objectName is not null) then to_char(S.objectName ||''-''||S.processDefinitionName||''-''||T.name_||''：''||T.proc_inst_id_) else to_char(S.processDefinitionName ||''-''||T.name_||''：''||T.proc_inst_id_) end) as openTitle,
                ''1'' as openType,
                (case when (T.form_key_ is not null) then T.form_key_||'''||chr(38)||'taskNumber='' || S.Tasknumber when (T.task_def_key_ = ''editTask'' ) then tv.text_||'''||chr(38)||'taskId=''||T.id_||'''||chr(38)||'taskNumber='' || S.Tasknumber  else T.form_key_ end) as taskURL
  from ACT_RU_TASK T
  left join act_fd_started_task S on T.proc_inst_id_ = S.procInstId
  left join act_ru_variable tv on (T.Proc_Inst_Id_ = tv.proc_inst_id_ and tv.name_ = ''editUrl'')
  left join t_s_base_user U on T.assignee_ = U.username
 where S.status not in (''已挂起'', ''已终止'', ''已删除'')
   and S.type not in (''reviewProcess'', ''practicableProcess'')
   and T.assignee_ = ${user.username}
 order by launchtime desc', 'select count(T.id_)
   from ACT_RU_TASK T
  left join act_fd_started_task S on T.proc_inst_id_ = S.procInstId
  left join act_ru_variable tv on (T.Proc_Inst_Id_ = tv.proc_inst_id_ and tv.name_ = ''editUrl'')
  left join t_s_base_user U on T.assignee_ = U.username
 where S.status not in (''已挂起'', ''已终止'', ''已删除'')
   and S.type not in (''reviewProcess'', ''practicableProcess'')
   and T.assignee_ = ${user.username}', 'SQL', '[]', '${taskId}', '${taskName}', '1', 'myTodoTaskController.do?initActivitiTask'||chr(38)||'isIframe=true'||chr(38)||'taskId=${taskId}'||chr(38)||'taskNumber=${taskNumber}');

insert into FD_TASK_CONFIG (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, CONFIGKEY, CONFIGNAME, CONFIGTYPE, EXECUTECLASS, EXECUTESQL, EXECUTECOUNTSQL, EXECUTETYPE, HANDLEBUTTON, OPENID, OPENTITLE, OPENTYPE, TASKURL)
values ('168583bcb2d444bfa2a2f6478f3f2a71', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'approveFinishedTask', '已办-审批任务', '3', '', 'select distinct t12.taskid as id,
       t12.taskid as taskId,
       t12.taskNumber as businessId,
       t12.taskNumber as taskNumber,
       t12.objectname as businessData,
       t12.processDefinitionName as processDefinitionName,
       (case when (t12.objectName is not null) then case when (T.NAME_ is not null) then to_char(t12.objectName || ''-'' || t12.processDefinitionName || ''-'' || T.NAME_ || ''：'' || t12.procInstId) else to_char(t12.objectName || ''-'' || t12.processDefinitionName || ''：'' || t12.procInstId) end else case when (T.NAME_ is not null) then to_char(t12.processDefinitionName || ''：'' || t12.procInstId) else to_char(t12.processDefinitionName || ''：'' || t12.procInstId) end end) as taskTitle,
       t12.type as processDefinitionKey,
       t12.status as taskStatus,
       to_char(T.NAME_) as taskName,
       ''taskReportApprove'' as taskType,
       t12.creater as launcher,
       to_char(t12.starttime, ''yyyy-MM-dd HH24:mi:ss'') as launchtime,
       t12.createrFullname as launchername,
       t12.createrFullname || ''-'' || t12.creater as launcherfullname,
       to_char(T.ASSIGNEE_) as assignee,
       t13.realname as assigneeFullname,
       to_char(t12.procInstId) as processInstanceId,
       to_char(T.task_def_key_) as taskKey,
       (case when (t12.objectName is not null) then case when (T.NAME_ is not null) then to_char(t12.objectName || ''-'' || t12.processDefinitionName || ''-'' || T.NAME_ || ''：'' || t12.procInstId) else to_char(t12.objectName || ''-'' || t12.processDefinitionName || ''：'' || t12.procInstId) end else case when (T.NAME_ is not null) then to_char(t12.processDefinitionName || ''-'' || T.NAME_ || ''：'' || t12.procInstId) else to_char(t12.processDefinitionName || ''：'' || t12.procInstId) end end) as openTitle,
       (case
         when (t12.type = ''qualityExplainReplyProcess'') then
          ''2''
         when (t12.type = ''qualityExternalProblemProcess'') then
          ''2''
         when (t12.type = ''qualityOtherProblemProcess'') then
          ''2''
         when (t12.type = ''qualityGeneralProblemProcess'') then
          ''2''
         else
          ''1''
       end) as openType,
       (case
         when (tv.text_ is null) then
          ''myFinishedTaskController.do?initActivitiTask'||chr(38)||'isIframe=true'||chr(38)||'procInstId=''||t12.procInstId||'''||chr(38)||'taskNumber='' || t12.taskNumber
         else
          to_char(tv.text_||'''||chr(38)||'procInstId=''||t12.procInstId ||'''||chr(38)||'taskNumber='' || t12.taskNumber)
       end) as taskURL
  from act_fd_started_task t12
  left join act_hi_taskinst t11 on (t12.procInstId = t11.proc_inst_id_ )
  left join act_hi_varinst tv on (t12.procInstId = tv.proc_inst_id_ and
                                 tv.name_ = ''viewUrl'')
  left join ACT_RU_TASK T on t12.procInstId = T.proc_inst_id_
  left join t_s_base_user t13 on (T.assignee_ = t13.username)
 where t11.end_time_ is not null
   and t12.type not in (''reviewProcess'', ''practicableProcess'')
   and t12.status not in (''已挂起'', ''已终止'', ''已删除'')
   and t11.assignee_ = ${user.username}
 order by launchtime desc', ' select count(distinct t12.taskid)
  from act_fd_started_task t12
  left join act_hi_taskinst t11 on (t12.procInstId = t11.proc_inst_id_ )
  left join act_hi_varinst tv on (t12.procInstId = tv.proc_inst_id_ and
                                 tv.name_ = ''viewUrl'')
  left join ACT_RU_TASK T on t12.procInstId = T.proc_inst_id_
  left join t_s_base_user t13 on (T.assignee_ = t13.username)
  left join PM_PLANOWNER_APPLYCHANGE_INFO applychange on (t12.type =
                                                         ''changeApplyProcess'' and
                                                         applychange.id =
                                                         t12.Tasknumber)
  left join pm_plan pl on pl.id = applychange.planid
  left join view_project_work v_pj on v_pj.formid = t12.taskNumber
  left join pm_project pj on pj.id = v_pj.id
 where t11.end_time_ is not null
   and t12.type not in (''reviewProcess'', ''practicableProcess'')
   and t12.status not in (''已挂起'', ''已终止'', ''已删除'')
   and t11.assignee_ = ${user.username}', 'SQL', '[]', '${taskId}', '${taskName}', '1', 'myFinishedTaskController.do?initActivitiTask'||chr(38)||'isIframe=true'||chr(38)||'procInstId=${processInstanceId}'||chr(38)||'taskNumber=${taskNumber}');

  insert into FD_TASK_CONFIG (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, CONFIGKEY, CONFIGNAME, CONFIGTYPE, EXECUTECLASS, EXECUTESQL, EXECUTECOUNTSQL, EXECUTETYPE, HANDLEBUTTON, OPENID, OPENTITLE, OPENTYPE, TASKURL)
values ('9d8eba6c3b7347508c62c6f5360e89a0', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'planLaunchedTask', '已发起-研发任务', '2', '', '      select t.id as id,
             t.id as taskId,
             t.planname as taskTitle,
             t.planname as taskName,
             pj.name as businessObject,
             t.id as businessId,
             t.createby as createby,
             t.createname as createname,
             t.createfullname as createfullname,
             t.createtime as createtime,
             lu.username as launchername,
             lu.realname || ''-'' || lu.username as launcherfullname,
             to_char(t.launchtime, ''yyyy-MM-dd HH24:mi:ss'') as launchtime,
             t.bizcurrent as bizcurrent,
             (case when t.projectstatus = ''PAUSE'' then ''暂停'' else planstatus.title end) as taskStatus,
             ''taskReportDevelop'' as taskType,
             t.planstarttime as beginTime,
             t.planendtime as endTime,
             t.progressrate as progressrate,
             t.planname as openTitle,
	     ''2'' as openType,
             '''''''' as taskURL
        from pm_plan t
        join pm_project pj on pj.id = t.projectid
        join t_s_base_user lu on lu.id = t.launcher
        join (select status.name, status.title
                from life_cycle_status status
               where status.policy_id in (select id from life_cycle_policy policy where policy.name = ''PLAN'' and policy.avaliable=''1'')) planstatus on planstatus.name = t.bizcurrent
 where t.avaliable = ''1''
   and t.bizcurrent = ''EDITING''
   and t.flowstatus = ''ORDERED''
   and (t.projectstatus is null or t.projectstatus <> ''CLOSE'')
   and lu.username = ${user.username}
 order by t.launchtime desc', '      select count(t.id)
        from pm_plan t
        join pm_project pj on pj.id = t.projectid
        join t_s_base_user lu on lu.id = t.launcher
        join (select status.name, status.title
                from life_cycle_status status
               where status.policy_id in (select id from life_cycle_policy policy where policy.name = ''PLAN'' and policy.avaliable=''1'')) planstatus on planstatus.name = t.bizcurrent
 where t.avaliable = ''1''
   and t.bizcurrent = ''EDITING''
   and t.flowstatus = ''ORDERED''
   and (t.projectstatus is null or t.projectstatus <> ''CLOSE'')
   and lu.username = ${user.username}', 'SQL', '[]', '${taskId}', '${taskTitle}', '2', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${taskId}');

insert into FD_TASK_CONFIG (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, CONFIGKEY, CONFIGNAME, CONFIGTYPE, EXECUTECLASS, EXECUTESQL, EXECUTECOUNTSQL, EXECUTETYPE, HANDLEBUTTON, OPENID, OPENTITLE, OPENTYPE, TASKURL)
values ('7c2d4bc674e64a358ad75889ac0e81ac', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'planTodoTask', '待办-研发任务', '1', '', '      select t.id as id,
             t.id as taskId,
             t.planname as taskTitle,
             t.planname as taskName,
             pj.name as businessObject,
             t.id as businessId,
             t.createby as createby,
             t.createname as createname,
             t.createfullname as createfullname,
             t.createtime as createtime,
             lu.username as launchername,
             lu.realname || ''-'' || lu.username as launcherfullname,
             to_char(t.assigntime, ''yyyy-MM-dd HH24:mi:ss'') as launchtime,
             t.bizcurrent as bizcurrent,
             planstatus.title as taskStatus,
             ''taskReportDevelop'' as taskType,
             t.planstarttime as beginTime,
             t.planendtime as endTime,
             t.progressrate as progressrate,
             t.planname as openTitle,
	     ''2'' as openType,
             '''''''' as taskURL
        from pm_plan t
        join pm_project pj on pj.id = t.projectid
        join t_s_base_user ou on ou.id = t.owner
        join t_s_base_user lu on lu.id = t.launcher
        join (select status.name, status.title
                from life_cycle_status status
               where status.policy_id in
                     (select id
                        from life_cycle_policy policy
                       where policy.name = ''PLAN'' and policy.avaliable=''1'')) planstatus on planstatus.name =
                                                                  t.bizcurrent
       where t.avaliable = ''1''
         and t.launchtime is not null
         and t.planstarttime is not null
         and t.planendtime is not null
         and ou.username = ${user.username}
         and t.bizcurrent = ''ORDERED''
         and pj.bizcurrent = ''STARTING''
       order by t.launchtime desc', '      select count(t.id)
        from pm_plan t
        join pm_project pj on pj.id = t.projectid
        join t_s_base_user ou on ou.id = t.owner
        join t_s_base_user lu on lu.id = t.launcher
        join (select status.name, status.title
                from life_cycle_status status
               where status.policy_id in
                     (select id
                        from life_cycle_policy policy
                       where policy.name = ''PLAN'' and policy.avaliable=''1'')) planstatus on planstatus.name =
                                                                  t.bizcurrent
       where t.avaliable = ''1''
         and t.launchtime is not null
         and t.planstarttime is not null
         and t.planendtime is not null
         and ou.username = ${user.username}
         and t.bizcurrent = ''ORDERED''
         and pj.bizcurrent = ''STARTING''', 'SQL', '[]', '${taskId}', '${taskTitle}', '2', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${taskId}');

insert into FD_TASK_CONFIG (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, CONFIGKEY, CONFIGNAME, CONFIGTYPE, EXECUTECLASS, EXECUTESQL, EXECUTECOUNTSQL, EXECUTETYPE, HANDLEBUTTON, OPENID, OPENTITLE, OPENTYPE, TASKURL)
values ('86774017001f45678511f745a20a582e', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 'planFinishedTask', '已办-研发任务', '3', '', '      select t.id as id,
             t.id as taskId,
             t.planname as taskTitle,
             t.planname as taskName,
             pj.name as businessObject,
             t.id as businessId,
             t.createby as createby,
             t.createname as createname,
             t.createfullname as createfullname,
             t.createtime as createtime,
             lu.username as launchername,
             lu.realname || ''-'' || lu.username as launcherfullname,
             to_char(t.assigntime, ''yyyy-MM-dd HH24:mi:ss'') as launchtime,
             t.bizcurrent as bizcurrent,
             planstatus.title as taskStatus,
             ''taskReportDevelop'' as taskType,
             t.planstarttime as beginTime,
             t.planendtime as endTime,
             t.progressrate as progressrate,
             t.planname as openTitle,
	     ''2'' as openType,
             '''''''' as taskURL
        from pm_plan t
        join pm_project pj on pj.id = t.projectid
        join t_s_base_user ou on ou.id = t.owner
        join t_s_base_user lu on lu.id = t.launcher
        join (select status.name, status.title
                from life_cycle_status status
               where status.policy_id in
                     (select id
                        from life_cycle_policy policy
                       where policy.name = ''PLAN'' and policy.avaliable=''1'')) planstatus on planstatus.name =
                                                                  t.bizcurrent
       where t.avaliable = ''1''
         and t.launchtime is not null
         and t.planstarttime is not null
         and t.planendtime is not null
         and ou.username = ${user.username}
         and t.bizcurrent = ''FINISH''
         and (t.projectstatus is null or t.projectstatus <> ''CLOSE'')
       order by t.launchtime desc', '      select count(t.id)
        from pm_plan t
        join pm_project pj on pj.id = t.projectid
        join t_s_base_user ou on ou.id = t.owner
        join t_s_base_user lu on lu.id = t.launcher
        join (select status.name, status.title
                from life_cycle_status status
               where status.policy_id in
                     (select id
                        from life_cycle_policy policy
                       where policy.name = ''PLAN'' and policy.avaliable=''1'')) planstatus on planstatus.name =
                                                                  t.bizcurrent
       where t.avaliable = ''1''
         and t.launchtime is not null
         and t.planstarttime is not null
         and t.planendtime is not null
         and ou.username = ${user.username}
         and t.bizcurrent = ''FINISH''
         and (t.projectstatus is null or t.projectstatus <> ''CLOSE'')', 'SQL', '[]', '${taskId}', '${taskTitle}', '2', '/ids-pm-web/taskDetailController.do?goCheck'||chr(38)||'isIframe=true'||chr(38)||'id=${taskId}');


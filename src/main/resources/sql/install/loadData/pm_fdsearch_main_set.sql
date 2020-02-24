--全局检索SQL初始化--
--全局检索-计划--
insert into FDSEARCH_MAIN_SET (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, ENTITYURL, RESULTCONTENT, SEARCHCODE, SEARCHIMPL, SEARCHNAME, SEARCHSQL, SEARCHTYPE, SEARCHURL, STATUS, ORDERBYNUM, DELETEFLAG, EXT1, EXT2, EXT3,APPKEY)
values ('e7faa64587b54971b9efa8e08761514f', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '', '<div class="list">
<div class="title"><a>$[planName]</a><i><span class="marginRight">$[createFullName]</span><span class="marginRight"> $[createTime]</span></i></div>
<div class="clearboth"></div>
<div class="sort">$[projectName]</div>
<div class="sort">$[owner]</div>
<div class="sort">$[status]</div>
<div class="sort">$[planlevelName]</div>
<div class="sort" ><span  class="marginRight">$[planTime]</span><span  class="marginRight">$[workTime]</span></div>
<div class="sort"><span  class="marginRight">$[taskType]</span><span  class="marginRight">$[taskNameType]</span></div>
<div class="sort" ><span  class="marginRight"> $[assigner]</span><span  class="marginRight">$[assignTime]</span></div>
<div class="sort">$[preposeName]</div>
<div class="sort"><span  class="marginRight">$[inputName]</span><span  class="marginRight">$[outputName]</span></div>
</div>', 'planSearch', '', '计划', ' select  distinct pl.id as id,pl.planName as planName,to_char(pl.createTime,''yyyy-mm-dd'') as createTime ,pl.createFullName||''-''||pl.createName as createFullName,p.name as projectName,(select replace(wm_concat(pp.planName),'','','','') from pm_plan pp where pp.id = (select pre.preposeplanid from pm_prepose_plan pre where pre.planid = pl.id and rownum = 1)) as preposeName,(select replace(wm_concat(input.name),'','','','') from PM_INPUTS input where input.useobjectid = pl.id) as inputName,(select replace(wm_concat(output.name),'','','','') from PM_DELIVERABLES_INFO output where output.useobjectid = pl.id) as outputName, parentPlanId, u.realName||''-''||u.userName as owner, planStartTime, planEndTime,decode(pl.bizCurrent,''EDITING'',''拟制中'',''ORDERED'',''已下达'',''FEEDBACKING'',''完工反馈中'',''FINISH'',''已完工'',''INVALID'',''已废弃'') as status,c.name as planlevelName,case when pl.planStartTime is null then '' '' else to_char(pl.planStartTime,''yyyy-mm-dd'')||''~''||to_char(pl.planEndTime,''yyyy-mm-dd'') end as planTime,workTime,
uu.realName||''-''||uu.userName as assigner,to_char(pl.assignTime,''yyyy/mm/dd'') as assignTime,pl.taskType as taskType,(select atm.name from PM_ACTIVITY_TYPE_MANAGE atm where atm.id = pl.tasknametype) as taskNameType
from PM_PLAN pl
join pm_project p on pl.projectId = p.id
left join t_s_base_user u on pl.owner = u.id
left join CM_BUSINESS_CONFIG c on pl.planlevel = c.id
left join t_s_base_user uu on pl.assigner= uu.id
join (select distinct pl.projectId pid
          from pm_plan pl
          join pm_proj_team_link pjtmlk on pjtmlk.projectid = pl.projectId
          join fd_team_role_user tu on tu.teamid = pjtmlk.teamid
         where tu.userid = ''$[user.id]''
        union
        select distinct pl.projectId pid
          from pm_plan pl
          join pm_proj_team_link pjtmlk on pjtmlk.projectid = pl.projectId
          join fd_team_role_group tg on tg.teamid = pjtmlk.teamid
          join t_s_group_user gu on gu.groupid = tg.groupid
         where gu.userid = ''$[user.id]''
        union
        select distinct pl.projectId pid
          from pm_plan pl
          join (select tru.userid userid
                  from t_s_role_user tru
                  join t_s_role r on tru.roleid = r.id
                 where r.rolecode = ''PMO''
                union
                select tgu.userid userid
                  from t_s_role_group trg
                  join t_s_role gr on trg.roleid = gr.id
                  join t_s_group_user tgu on tgu.groupid = trg.groupid
                 where gr.rolecode = ''PMO'') RU on 1 = 1
         where RU.Userid = ''$[user.id]'') uu on uu.pid = p.id
where pl.planName like ? or u.realName like ? or u.userName like ? or uu.realName like ? or uu.userName like ?
order by pl.planEndTime desc', '0', '', '1', '12', '', '', '', '','ids-pm-web');

--全局检索-项目--
insert into FDSEARCH_MAIN_SET (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, ENTITYURL, RESULTCONTENT, SEARCHCODE, SEARCHIMPL, SEARCHNAME, SEARCHSQL, SEARCHTYPE, SEARCHURL, STATUS, ORDERBYNUM,APPKEY)
values ('3e2c459e4f704ae2ab6c8f47450e5001', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '', '<div class="list">
<div class="title"><a>$[projectName]</a><i><span class="marginRight">$[createName]</span><span class="marginRight">$[createtime]</span></i></div>
<p>$[projectManagerName]</p>
<p>$[startTime]~$[endTime]</p>
<p>$[bizCurrent]</p>
<p>$[phaseName]</p>
<p>$[epsName]</p>
</div>', 'projectSearch', '', '项目', 'select p.id projectId,
       p.name || ''-'' || p.projectnumber projectName,
       p.projectnumber projectNumber,
       bc1.name phaseName,
       p.projectmanagernames projectManagerName,
       to_char(p.startprojecttime, ''yyyy-mm-dd'') as startTime,
       to_char(p.endprojecttime, ''yyyy-mm-dd'') as endTime,
       bc2.name epsName,
       bu.realname || ''-'' || bu.username createName,
       lcs.title bizCurrent,
       p.createtime createtime
  from pm_project p
  left join cm_business_config bc1 on bc1.id = p.phase
  left join cm_eps_config bc2 on bc2.id = p.eps
  left join t_s_base_user bu on bu.id = p.createby
  join (select distinct pj.id pid
  from pm_project pj
  join pm_proj_team_link pjtmlk on pjtmlk.projectid = pj.id
  join fd_team_role_user tu on tu.teamid = pjtmlk.teamid
         where tu.userid = ''$[user.id]''
        union
        select distinct pj.id pid
  from pm_project pj
  join pm_proj_team_link pjtmlk on pjtmlk.projectid = pj.id
  join fd_team_role_group tg on tg.teamid = pjtmlk.teamid
  join t_s_group_user gu on gu.groupid = tg.groupid
         where gu.userid = ''$[user.id]''
        union
        select distinct pj.id pid
  from pm_project pj
  join (select tru.userid userid
          from t_s_role_user tru
          join t_s_role r on tru.roleid = r.id
         where r.rolecode = ''PMO''  union
        select tgu.userid userid
          from t_s_role_group trg
          join t_s_role gr on trg.roleid = gr.id
          join t_s_group_user tgu on tgu.groupid = trg.groupid
         where gr.rolecode = ''PMO'') RU on 1 = 1
         where RU.Userid = ''$[user.id]'') uu on uu.pid = p.id
  join life_cycle_policy lcp on lcp.name = ''PROJECT_MANAGER''
  join life_cycle_status lcs on lcs.policy_id = lcp.id
    and lcs.name = p.bizcurrent
 where p.name || ''-'' || p.projectnumber like ?
    or p.projectnumber like ?
    or bc1.name like ?
    or p.projectmanagernames like ?
    or p.startprojecttime like ?
    or p.endprojecttime like ?
    or bc2.name like ?
    or bu.realname || ''-'' || bu.username like ?
 order by p.projectnumber asc', '0', '', '1', '13','ids-pm-web');

--全局检索-文档--
insert into FDSEARCH_MAIN_SET (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, ENTITYURL, RESULTCONTENT, SEARCHCODE, SEARCHIMPL, SEARCHNAME, SEARCHSQL, SEARCHTYPE, SEARCHURL, STATUS, ORDERBYNUM,APPKEY)
values ('a6b3a4b63ac04d0ea1d4c58951218e4b', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('04-08-2015 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '', '<div class="list">
<div class="title clearboth"><a href="#">$[fileName]</a><i><span class="marginRight">$[createName] </span><span class="marginRight">    $[createTime]</span></i></div>
<p>$[securityLevel]</p>
<p>$[path]</p>
<p>$[version ]</p>
<p ><span class="marginRight">$[updateName]</span><span class="marginRight">$[updateTime]</span></p>
</div>','projectDocSearch','','文档',' select distinct rf.id id,rf.filename || ''-'' || rf.filenumber fileName,ap.path path,
 case when rf.securitylevel = ''1'' then ''非密''
 when rf.securitylevel = ''2'' then ''秘密''
 when rf.securitylevel = ''3'' then ''机密''
 when rf.securitylevel = ''4'' then ''绝密''
 end as securityLevel,bu1.realname||''-''||bu1.username createName,
 to_char(rf.createtime,''yyyy-mm-dd'') createTime,bu2.realname||''-''||bu2.username updateName,
 to_char(rf.updatetime,''yyyy-mm-dd'') updateTime,rf.bizversion version
 from rep_file rf
 join PM_PROJ_TEAM_LINK ptl on ptl.libid = rf.libid
 join t_s_base_user bu1 on bu1.id = rf.createby
 join t_s_base_user bu2 on bu2.id = rf.updateby
 join
 (SELECT A.ID,substr(A.NAMEPATH, 0, length(A.NAMEPATH) - length(A.NAME) - 1) PATH
 FROM
 (select category.id ID,category.filename NAME,sys_connect_by_path(category.filename, ''/'') NAMEPATH
 from
 (select f.id,f.parentid,f.filename,f.filetype from rep_file f) category
 where category.filetype = 1 start with category.parentid in
 (select l.id from rep_library l)
 connect by category.parentid = prior category.id order by category.id)
 A) ap on ap.id = rf.id
 join pm_project p on p.id = ptl.projectid
 join
 (select distinct pj.id pid, pj.name
 from pm_project pj
 join pm_proj_team_link pjtmlk on pjtmlk.projectid = pj.id
 join fd_team_role_user tu on tu.teamid = pjtmlk.teamid
 where tu.userid = ''$[user.id]''
 union
 select distinct pj.id pid, pj.name
 from pm_project pj
 join pm_proj_team_link pjtmlk on pjtmlk.projectid = pj.id
 join fd_team_role_group tg on tg.teamid = pjtmlk.teamid
 join t_s_group_user gu on gu.groupid = tg.groupid
 where gu.userid = ''$[user.id]'') pp on pp.pid = p.id
 join (select u.securitylevel from t_s_base_user u where u.id = ''$[user.id]'') uu on 1 = 1
 where rf.filenumber is not null and rf.avaliable = ''1'' and to_number(uu.securitylevel) >= to_number(rf.securitylevel)
 and rf.id not in
 (SELECT ID
 FROM
 (SELECT distinct ID,FILENAME,PATH,FILETYPEID,AVALIABLE,FILETYPE,CREATEBY,AUTH1,
 (CASE WHEN D.FILEID IS NOT NULL THEN ''true'' ELSE ''false'' END) AUTH2,D.projectid
 FROM
 (SELECT RF.ID ID,RF.FILENAME FILENAME,B.PATH PATH,RF.FILETYPEID FILETYPEID,
 RF.AVALIABLE AVALIABLE,RF.FILETYPE FILETYPE,RF.CREATEBY CREATEBY,
 (CASE WHEN LIBAUTH.LIBID IS NOT NULL THEN ''false'' ELSE ''true'' END) AUTH1 FROM REP_FILE RF
 LEFT JOIN
 (SELECT A.ID,substr(A.IDPATH, 2, length(A.IDPATH) - length(A.ID) - 2) PATH
 FROM (SELECT CATEGORY.ID ID, SYS_CONNECT_BY_PATH(CATEGORY.ID,'','') IDPATH
 FROM (SELECT F.ID, F.PARENTID, F.FILENAME, F.FILETYPE FROM REP_FILE F) CATEGORY
 WHERE CATEGORY.FILETYPE = 1
 START WITH CATEGORY.PARENTID IN (SELECT L.ID FROM REP_LIBRARY L)
 CONNECT BY CATEGORY.PARENTID = PRIOR CATEGORY.ID ORDER BY CATEGORY.ID) A
 ) B ON B.ID = RF.ID
 LEFT JOIN (SELECT DISTINCT F.LIBID, F.FILENAME
 FROM REP_ROLE_FILE_AUTH FU
 JOIN REP_FILE F ON F.ID = FU.FILEID
 WHERE FU.PERMISSIONCODE IS NOT NULL
 AND FU.PERMISSIONCODE > 0) LIBAUTH ON LIBAUTH.LIBID = RF.LIBID
 WHERE RF.AVALIABLE = ''1'' AND RF.FILETYPE = 1) C
 JOIN
 (SELECT FU.FILEID, FU.ROLEID, RL.PROJECTID 
FROM REP_ROLE_FILE_AUTH FU 
join rep_file f on f.id = FU.FILEID
 join
 (select tu.roleid ROLEID,pjtmlk.libid,pjtmlk.Projectid
 from fd_team_role_user tu
 join pm_proj_team_link pjtmlk on tu.teamid =pjtmlk.teamid
 where tu.userid = ''$[user.id]''
 union
 select tg.roleid ROLEID,pjtmlk.libid,pjtmlk.Projectid
 from fd_team_role_group tg
 join t_s_group_user gu on tg.groupid = gu.groupid
 join pm_proj_team_link pjtmlk on tg.teamid =pjtmlk.teamid
 where gu.userid = ''$[user.id]'') RL
 ON (FU.ROLEID =RL.ROLEID AND f.libid =RL.Libid)
 WHERE FU.PERMISSIONCODE IS  NULL
 or FU.PERMISSIONCODE = 0 ) D
 ON C.PATH LIKE ''%''||D.FILEID||''%'' ) AUTH
 WHERE AUTH.AUTH1 = ''true'' or AUTH.AUTH2 = ''true''  )
 and (rf.filenumber like ? or rf.filename like ? or bu1.realname||''-''||bu1.username like ? or bu2.realname||''-''||bu2.username like ?)
	 order by to_char(rf.updatetime, ''yyyy-mm-dd'') desc ', '0', '', '1', '11','ids-pm-web');

insert into dynamic_form (ID, CREATEBY, CREATEFULLNAME, CREATENAME, CREATETIME, UPDATEBY, UPDATEFULLNAME, UPDATENAME, UPDATETIME, AVALIABLE, BIZCURRENT, BIZID, BIZVERSION, FIRSTBY, FIRSTFULLNAME, FIRSTNAME, FIRSTTIME, SECURITYLEVEL, POLICY_ID, DBID, FORMCODE, FORMCONTENT, FORMNAME, FORMPARSE, FDFORMPARSE, FORMSTYLEID, MAINTABLESOURCE, NEWVERSION, IFLOCK, FROMVERSION, DELETEFLAG, EXT1, EXT2, EXT3)
values ('4028f007655a84fd01655ae96cc70037', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('21-08-2018 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('21-08-2018 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), '1', 'shengxiao', '1b039524-5bc9-4af7-bf86-7a20919b38fd', '1', '4028ef354b5d7bb1014b5d7cffa10043', '超级管理员', 'gladmin', to_date('21-08-2018 15:14:02', 'dd-mm-yyyy hh24:mi:ss'), 1, '1001', '', 'docDefaultForm', '{"fields":2,"template":"<p><input name=\"leader\" class=\"dynamic-selectuser easyui-searchbox\" ext1=\"undefined\" fieldname=\"leader\" title=\"室领导\" leipiplugins=\"dynccommonuser\" plugins=\"dynccommonuser\" fieldtype=\"selectuser\" data-options=\"required:true,editable:false,searcher:function (){try {$(this).searchuser(); } catch (e) {}}\"/><input name=\"deptLeader\" class=\"dynamic-selectuser easyui-searchbox\" ext1=\"undefined\" fieldname=\"deptLeader\" title=\"部领导\" leipiplugins=\"dynccommonuser\" plugins=\"dynccommonuser\" fieldtype=\"selectuser\" data-options=\"required:true,editable:false,searcher:function (){try {$(this).searchuser(); } catch (e) {}}\"/></p>","add_fields":{"leader":{"fieldname":"leader","fieldtitle":"室领导","fieldtype":"selectuser","fieldmulti":"false","fieldflow":"1","orgrequired":"true","plugins":"dynccommonuser"},"deptLeader":{"fieldname":"deptLeader","fieldtitle":"部领导","fieldtype":"selectuser","fieldmulti":"false","fieldflow":"1","orgrequired":"true","plugins":"dynccommonuser"}},"form_map":{"elements":[{"key":"leader","value":{"fieldname":"leader","fieldtitle":"室领导","fieldtype":"selectuser","fieldmulti":"false","fieldflow":"1","orgrequired":"true","plugins":"dynccommonuser"}},{"key":"deptLeader","value":{"fieldname":"deptLeader","fieldtitle":"部领导","fieldtype":"selectuser","fieldmulti":"false","fieldflow":"1","orgrequired":"true","plugins":"dynccommonuser"}}]}}', '文档审批默认表单', '<p><input name="leader" class="dynamic-selectuser easyui-searchbox" ext1="undefined" fieldname="leader" title="室领导" leipiplugins="dynccommonuser" plugins="dynccommonuser" fieldtype="selectuser" data-options="required:true,editable:false,searcher:function (){try {$(this).searchuser(); } catch (e) {}}"/><input name="deptLeader" class="dynamic-selectuser easyui-searchbox" ext1="undefined" fieldname="deptLeader" title="部领导" leipiplugins="dynccommonuser" plugins="dynccommonuser" fieldtype="selectuser" data-options="required:true,editable:false,searcher:function (){try {$(this).searchuser(); } catch (e) {}}"/></p>', '<span class="glaway_search_box_width_330" id="glaway_input_readonly_leader"><span class="search_title_box"><span class="search_title" title="室领导">室领导'||chr(38)||'nbsp;'||chr(38)||'nbsp;</span></span><input name="leader_condition" type="hidden" value="in"><input id="leader" name="leader" class=" glaway-input " type="text" leipiplugins="dynccommonuser" data-options="isTrim:true,required:true,hiddenInputId:''selectUserIdHidden_leader'',searcher:function(){ var result ; try{   result=$(this).searchuser().apply(this,arguments); }catch(e){} return result; },editable:false,prompt:'''',maxLength:30"></span><input type="hidden" id="selectUserIdHidden_leader" name="selectUserIdHidden_leader"><input type="hidden" id="selectUserNameHidden_leader" name="selectUserNameHidden_leader"><script>$(function() {});</script><script>$(function(){$(''#leader'').searchbox(); var options = $(''#leader'').searchbox(''options'');$(''#leader'').searchbox(''textbox'').one(''focus'',function(){ var temp =options.maxLength;if(temp)$(this).attr(''maxlength'', Number(temp));}).focusout(function (){ var isTrim= options.isTrim; if(isTrim){ $(this).val($.trim($(this).val()));}});});</script><span class="glaway_search_box_width_330" id="glaway_input_readonly_deptLeader"><span class="search_title_box"><span class="search_title" title="部领导">部领导'||chr(38)||'nbsp;'||chr(38)||'nbsp;</span></span><input name="deptLeader_condition" type="hidden" value="in"><input id="deptLeader" name="deptLeader" class=" glaway-input " type="text" leipiplugins="dynccommonuser" data-options="isTrim:true,required:true,hiddenInputId:''selectUserIdHidden_deptLeader'',searcher:function(){ var result ; try{   result=$(this).searchuser().apply(this,arguments); }catch(e){} return result; },editable:false,prompt:'''',maxLength:30"></span><input type="hidden" id="selectUserIdHidden_deptLeader" name="selectUserIdHidden_deptLeader"><input type="hidden" id="selectUserNameHidden_deptLeader" name="selectUserNameHidden_deptLeader"><script>$(function() {});</script><script>$(function(){$(''#deptLeader'').searchbox(); var options = $(''#deptLeader'').searchbox(''options'');$(''#deptLeader'').searchbox(''textbox'').one(''focus'',function(){ var temp =options.maxLength;if(temp)$(this).attr(''maxlength'', Number(temp));}).focusout(function (){ var isTrim= options.isTrim; if(isTrim){ $(this).val($.trim($(this).val()));}});});</script>', '', '', 'Y', '0', '', '', '', '', '');

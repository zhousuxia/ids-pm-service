update FDSEARCH_MAIN_SET set SEARCHSQL = 'select  distinct pl.id as id,pl.planName as planName,to_char(pl.createTime,''yyyy-mm-dd'') as createTime ,pl.createFullName||''-''||pl.createName as createFullName,p.name as projectName,(select replace(wm_concat(pp.planName),'','','','') from pm_plan pp where pp.id = (select pre.preposeplanid from pm_prepose_plan pre where pre.planid = pl.id and rownum = 1)) as preposeName,(select replace(wm_concat(input.name),'','','','') from PM_INPUTS input where input.useobjectid = pl.id) as inputName,(select replace(wm_concat(output.name),'','','','') from PM_DELIVERABLES_INFO output where output.useobjectid = pl.id) as outputName, parentPlanId, u.realName||''-''||u.userName as owner, planStartTime, planEndTime,decode(pl.bizCurrent,''EDITING'',''拟制中'',''ORDERED'',''已下达'',''FEEDBACKING'',''完工反馈中'',''FINISH'',''已完工'',''INVALID'',''已废弃'') as status,c.name as planlevelName,case when pl.planStartTime is null then '' '' else to_char(pl.planStartTime,''yyyy-mm-dd'')||''~''||to_char(pl.planEndTime,''yyyy-mm-dd'') end as planTime,workTime,
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
order by pl.planEndTime desc',APPKEY='ids-pm-web' where ID = 'e7faa64587b54971b9efa8e08761514f';

update FDSEARCH_MAIN_SET set APPKEY='ids-pm-web' where ID='3e2c459e4f704ae2ab6c8f47450e5001';
update FDSEARCH_MAIN_SET set APPKEY='ids-pm-web' where ID='a6b3a4b63ac04d0ea1d4c58951218e4b';
update FDSEARCH_RESULT_SET set LINK = '/ids-pm-web/projectMenuController.do?projectMenu''||chr(38)||''projectId=$[PROJECTID]''||chr(38)||''tabTitle=项目计划''||chr(38)||''tabKey=4028efee4c3617ec014c362eed6c0012''||chr(38)||''isIframe=true' where ID = '4028f00455c479b70155c4910eef0012';
update FDSEARCH_RESULT_SET set LINK = '/ids-pm-web/projLibController.do?viewProjectDocDetailForSearch''||chr(38)||''id=$[id]' where ID = '4028f00755c906050155c97ac6b20029';
update FDSEARCH_RESULT_SET set LINK = '/ids-pm-web/taskDetailController.do?goCheck''||chr(38)||''isIframe=true''||chr(38)||''id=$[id]' where ID = '4028f00755c323480155c3f50f2a00ae';

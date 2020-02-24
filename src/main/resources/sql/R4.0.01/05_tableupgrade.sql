--更新原有计划的活动类型
update pm_plan t
   set t.tasknametype = (CASE WHEN t.tasknametype is null THEN '4028f00c6cefbdc9016cefcd30c00007' WHEN t.tasknametype in ('0', '1', '3') THEN
         (select act.id
           from PM_ACTIVITY_TYPE_MANAGE act, t_s_type type
          where act.name = type.typename
            and type.typegroupid =
                '4028ef3b519e5ee801519e7ca96f0000'
            and t.tasknametype = type.typecode)
            ELSE  t.tasknametype
            END);

--活动名称库活动类型更新         
update CM_NAME_STANDARD t
   set t.activecategory = (CASE WHEN t.activecategory is null THEN '4028f00c6cefbdc9016cefcd30c00007' WHEN t.activecategory in ('0', '1', '3') THEN
         (select act.id
           from PM_ACTIVITY_TYPE_MANAGE act, t_s_type type
          where act.name = type.typename
            and type.typegroupid =
                '4028ef3b519e5ee801519e7ca96f0000'
            and t.activecategory = type.typecode)
            ELSE  t.activecategory
            END);

--修改字段类型
alter table PM_DATA_SOURCE_OBJECT add newobjectModelProperty clob;
update PM_DATA_SOURCE_OBJECT set newobjectModelProperty = objectModelProperty;
alter table PM_DATA_SOURCE_OBJECT drop column objectModelProperty;
alter table PM_DATA_SOURCE_OBJECT rename column newobjectModelProperty to objectModelProperty;

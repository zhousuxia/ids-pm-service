update pm_flowtask t
   set t.tasknametype = (CASE WHEN t.tasknametype is null THEN '4028f00c6cefbdc9016cefcd30c00007' WHEN t.tasknametype in ('0', '1', '3') THEN
         (select act.id
           from PM_ACTIVITY_TYPE_MANAGE act, t_s_type type
          where act.name = type.typename
            and type.typegroupid =
                '4028ef3b519e5ee801519e7ca96f0000'
            and t.tasknametype = type.typecode)
            ELSE  t.tasknametype
            END);
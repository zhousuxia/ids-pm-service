--计划预警或预期定时任务--
insert into timer_fd_job (ID, JOBDESC, PACKAGENAME)
values ('82CF4B7815A541B89B5DE90FA0B28CDD', '计划逾期或预警定时任务', 'feign/planWarnAndOverTimerRestController/executeJob.do');
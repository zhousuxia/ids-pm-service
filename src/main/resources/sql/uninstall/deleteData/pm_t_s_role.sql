delete from t_s_role_operation t where t.roleid = '297e26e64cb20d73014cb21128f20001';
delete from t_s_role_function r where r.roleid = '297e26e64cb20d73014cb21128f20001';
delete from t_s_role_user where roleid = '297e26e64cb20d73014cb21128f20001';
delete from t_s_role_group  where roleid = '297e26e64cb20d73014cb21128f20001';

delete from t_s_role_operation t where t.roleid = '4028effc4c25a73e014c25afdde30012';
delete from t_s_role_function r where r.roleid = '4028effc4c25a73e014c25afdde30012';
delete from t_s_role_user where roleid = '4028effc4c25a73e014c25afdde30012';
delete from t_s_role_group  where roleid = '4028effc4c25a73e014c25afdde30012';

--������Ŀ����Ա����Ŀ�����ɫ
delete from t_s_role where id = '297e26e64cb20d73014cb21128f20001';
delete from t_s_role where id = '4028effc4c25a73e014c25afdde30012';
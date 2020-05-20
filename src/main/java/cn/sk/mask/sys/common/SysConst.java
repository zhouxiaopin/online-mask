package cn.sk.mask.sys.common;

/**
 * 常量类
 */
public class SysConst {

    //下载文件前缀
    public static final String DOWNLOAD_FILE_PREFIX = "static/file/";
    public static final String UPLOAD_FILE_PREFIX = "static/upload/";
    public static final String UPLOAD_FILE_IMG_PREFIX = "img/";

    public interface RecordStatus{
        String DELETE = "00";//记录删除
        String ABLE = "01";//记录可用
        String DISABLE = "02";//记录禁用
    }

    public interface ResponseMsg{
        String OPRT_SUCCE = "操作成功";
        String ADD_SUCCE = "添加成功";
        String UPDATE_SUCCE = "修改成功";
        String DELET_SUCCE = "删除成功";
        String QUERY_SUCCE = "查询成功";

        String OPRT_FAIL = "操作失败";
        String ADD_FAIL = "添加失败";
        String UPDATE_FAIL = "修改失败";
        String DELET_FAIL = "删除失败";
        String QUERY_FAIL = "查询失败";
        String NO_PAGE = "没有配置页面";

        String RECORD_EXISTS_NO = "该记录不存在";

    }

    public interface Dict{
        String RECORDSTATUS_DICTCODE = "record_status";
        //系统权限
        class SysPermis{
            //类型
            public static final String MENU_TYPE = "menu_type";
            //级别
            public static final String MENU_LEVEL = "menu_level";
        }
        //系统资源
        class SysResource{
            //类型
            public static final String RES_TYPE = "res_type";
            //级别
            public static final String RES_LEVEL = "res_level";
            //左边图标
            public static final String LEFT_ICON = "menu_icon";
        }
        //sql语句配置
        class SysSqlConf{
            //类型
            public static final String STATEMENT_TYPE = "statement_type";
        }
    }

    public interface SessionKey{
        String SYSUSER_INFO = "sysUserInfo";
    }

    public interface Permis{
        //权限类型
        String MENU = "01";
        String BUTTON = "02";

        Integer DEFAULT_PARENTID = 0;
    }
    public interface SysResource{
        Integer DEFAULT_PARENTID = 0;
    }
    //shiro权限配置
    public interface ShiroPermis{
        //添加
        String ADD = "add";
        //修改
        String UPDATE = "update";
        //禁用启用
        String UPDATE_RECORDSTATUS = "updateRecordStatus";
        //删除
        String DEL = "del";
        //硬删除
        String REAL_DEL = "realDel";
        //批量删除
        String BATCH_DEL = "batchDel";
        //批量硬删除
        String BATCH_REAL_DEL = "batchRealDel";

        //系统用户
        class SysUser{
            public static final String SYSUSER = "sysUser";
            public static final String ADD = SYSUSER+":"+ShiroPermis.ADD;
            public static final String UPDATE = SYSUSER+":"+ShiroPermis.UPDATE;
            public static final String UPDATE_RECORDSTATUS = SYSUSER+":"+ShiroPermis.UPDATE_RECORDSTATUS;
            public static final String UPDATE_PSD = SYSUSER+":updatePsd";
            public static final String DEL = SYSUSER+":"+ShiroPermis.DEL;
            public static final String REAL_DEL = SYSUSER+":"+ShiroPermis.REAL_DEL;
            public static final String BATCH_DEL = SYSUSER+":"+ShiroPermis.BATCH_DEL;
            public static final String BATCH_REAL_DEL = SYSUSER+":"+ShiroPermis.BATCH_REAL_DEL;
        }

        //系统角色
        class SysRole{
            public static final String SYSROLE = "sysRole";
            public static final String ADD = SYSROLE+":"+ShiroPermis.ADD;
            public static final String UPDATE = SYSROLE+":"+ShiroPermis.UPDATE;
            public static final String UPDATE_RECORDSTATUS = SYSROLE+":"+ShiroPermis.UPDATE_RECORDSTATUS;
            public static final String DEL = SYSROLE+":"+ShiroPermis.DEL;
            public static final String REAL_DEL = SYSROLE+":"+ShiroPermis.REAL_DEL;
            public static final String BATCH_DEL = SYSROLE+":"+ShiroPermis.BATCH_DEL;
            public static final String BATCH_REAL_DEL = SYSROLE+":"+ShiroPermis.BATCH_REAL_DEL;
        }
        //系统权限
        class SysPermis{
            public static final String SYSPERMIS = "sysPermis";
            public static final String ADD = SYSPERMIS+":"+ShiroPermis.ADD;
            public static final String UPDATE = SYSPERMIS+":"+ShiroPermis.UPDATE;
            public static final String UPDATE_RECORDSTATUS = SYSPERMIS+":"+ShiroPermis.UPDATE_RECORDSTATUS;
            public static final String DEL = SYSPERMIS+":"+ShiroPermis.DEL;
            public static final String REAL_DEL = SYSPERMIS+":"+ShiroPermis.REAL_DEL;
            public static final String BATCH_DEL = SYSPERMIS+":"+ShiroPermis.BATCH_DEL;
            public static final String BATCH_REAL_DEL = SYSPERMIS+":"+ShiroPermis.BATCH_REAL_DEL;
        }
        //系统资源
        class SysResource{
            public static final String SYSRESOURCE = "sysResource";
            public static final String ADD = SYSRESOURCE+":"+ShiroPermis.ADD;
            public static final String UPDATE = SYSRESOURCE+":"+ShiroPermis.UPDATE;
            public static final String UPDATE_RECORDSTATUS = SYSRESOURCE+":"+ShiroPermis.UPDATE_RECORDSTATUS;
            public static final String DEL = SYSRESOURCE+":"+ShiroPermis.DEL;
            public static final String REAL_DEL = SYSRESOURCE+":"+ShiroPermis.REAL_DEL;
            public static final String BATCH_DEL = SYSRESOURCE+":"+ShiroPermis.BATCH_DEL;
            public static final String BATCH_REAL_DEL = SYSRESOURCE+":"+ShiroPermis.BATCH_REAL_DEL;
        }
        //系统日志
        class SysLog{
            public static final String SYSlOG = "sysLog";
            public static final String ADD = SYSlOG+":"+ShiroPermis.ADD;
            public static final String UPDATE = SYSlOG+":"+ShiroPermis.UPDATE;
            public static final String UPDATE_RECORDSTATUS = SYSlOG+":"+ShiroPermis.UPDATE_RECORDSTATUS;
            public static final String DEL = SYSlOG+":"+ShiroPermis.DEL;
            public static final String REAL_DEL = SYSlOG+":"+ShiroPermis.REAL_DEL;
            public static final String BATCH_DEL = SYSlOG+":"+ShiroPermis.BATCH_DEL;
            public static final String BATCH_REAL_DEL = SYSlOG+":"+ShiroPermis.BATCH_REAL_DEL;
        }
        //数字字典
        class SysDict{
            public static final String SYSDICT = "sysDict";
            public static final String ADD = SYSDICT+":"+ShiroPermis.ADD;
            public static final String UPDATE = SYSDICT+":"+ShiroPermis.UPDATE;
            public static final String UPDATE_RECORDSTATUS = SYSDICT+":"+ShiroPermis.UPDATE_RECORDSTATUS;
            public static final String DEL = SYSDICT+":"+ShiroPermis.DEL;
            public static final String REAL_DEL = SYSDICT+":"+ShiroPermis.REAL_DEL;
            public static final String BATCH_DEL = SYSDICT+":"+ShiroPermis.BATCH_DEL;
            public static final String BATCH_REAL_DEL = SYSDICT+":"+ShiroPermis.BATCH_REAL_DEL;
        }
        //sql语句配置
        class SysSqlConf{
            public static final String SYSSQLCONF = "sysSqlConf";
            public static final String ADD = SYSSQLCONF+":"+ShiroPermis.ADD;
            public static final String UPDATE = SYSSQLCONF+":"+ShiroPermis.UPDATE;
            public static final String UPDATE_RECORDSTATUS = SYSSQLCONF+":"+ShiroPermis.UPDATE_RECORDSTATUS;
            public static final String DEL = SYSSQLCONF+":"+ShiroPermis.DEL;
            public static final String REAL_DEL = SYSSQLCONF+":"+ShiroPermis.REAL_DEL;
            public static final String BATCH_DEL = SYSSQLCONF+":"+ShiroPermis.BATCH_DEL;
            public static final String BATCH_REAL_DEL = SYSSQLCONF+":"+ShiroPermis.BATCH_REAL_DEL;
        }
    }

}

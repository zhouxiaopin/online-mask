package cn.sk.mask.business.common;

import cn.sk.mask.sys.common.SysConst;

/**
 * 常量类
 */
public class Const {

    //树形默认父id
    public static final int DEFAULT_PARENTID = 0;
    //默认分割符号
    public static final String DEFAULT_SPLIT_SYMBOL = ";";

    //统一用户api接口前缀
    public interface Url{

    }
    //统一用户系统的报修系统分配权限标识
    public static final String UA_SYS_REPAIR_WX_PERMIS_FLAG = "repairWx";

    //上传的目录
    public static final String UPLOAD_IMG_PATH = SysConst.UPLOAD_FILE_PREFIX + SysConst.UPLOAD_FILE_IMG_PREFIX;

    //字典类型
    public interface Dict{
        //机构
        class Organ{
            //类型
            public static final String ORG_TYPE = "org_type";
        }
        //报修
        class Repair{
            //报修类型
            public static final String REPAIR_TYPE = "repair_type";
            //报修位置（楼层）
            public static final String REPAIR_POSI_STOREY = "repair_posi_storey";
            //报修记录状态
            public static final String REPAIR_RECORD_STATUS = "repair_record_status";
        }

    }

    //shiro权限配置
    public interface ShiroPermis{
        //机构信息
        class Category{
            public static final String MODEL_NAME = "category";
            public static final String ADD = MODEL_NAME+":"+ SysConst.ShiroPermis.ADD;
            public static final String UPDATE = MODEL_NAME+":"+ SysConst.ShiroPermis.UPDATE;
            public static final String UPDATE_RECORDSTATUS = MODEL_NAME+":"+ SysConst.ShiroPermis.UPDATE_RECORDSTATUS;
            public static final String DEL = MODEL_NAME+":"+ SysConst.ShiroPermis.DEL;
            public static final String REAL_DEL = MODEL_NAME+":"+ SysConst.ShiroPermis.REAL_DEL;
            public static final String BATCH_DEL = MODEL_NAME+":"+ SysConst.ShiroPermis.BATCH_DEL;
            public static final String BATCH_REAL_DEL = MODEL_NAME+":"+ SysConst.ShiroPermis.BATCH_REAL_DEL;
        }


    }

}

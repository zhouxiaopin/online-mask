package cn.sk.mask.sys.service.impl;

import cn.sk.mask.base.service.impl.BaseServiceImpl;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.mapper.SysSqlConfMapper;
import cn.sk.mask.sys.pojo.SysDictCustom;
import cn.sk.mask.sys.pojo.SysDictQueryVo;
import cn.sk.mask.sys.pojo.SysSqlConfCustom;
import cn.sk.mask.sys.pojo.SysSqlConfQueryVo;
import cn.sk.mask.sys.service.ISysSqlConfService;
import cn.sk.mask.sys.vo.DataTableVo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统sql语句配置业务逻辑接口实现类
 */
@Service
public class SysSqlConfServiceImpl extends BaseServiceImpl<SysSqlConfCustom, SysSqlConfQueryVo> implements ISysSqlConfService {
    @Autowired
    private SysSqlConfMapper sysSqlConfMapper;

    @Override
    public DataTableVo<SysSqlConfCustom> queryObjsByPage(SysSqlConfQueryVo entityQueryVo) {
        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
        SysDictCustom condition = new SysDictCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(SysConst.Dict.SysSqlConf.STATEMENT_TYPE);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        sysDictQueryVo.setSysDictCustom(condition);
        List<SysDictCustom> sysDictCustoms = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //类型
        Map<String,String> statementTypeMap = Maps.newHashMap();
        for(int i = 0,len = sysDictCustoms.size(); i < len; i++) {
            SysDictCustom sysDictCustom = sysDictCustoms.get(i);
            statementTypeMap.put(sysDictCustom.getDictCode(),sysDictCustom.getCodeName());
        }

        //数据封装
        DataTableVo<SysSqlConfCustom> dataTableVo = super.queryObjsByPage(entityQueryVo);
        List<SysSqlConfCustom> data = dataTableVo.getData();
        for(int i = 0,len = data.size(); i < len; i++) {
            SysSqlConfCustom sysSqlConfCustom = data.get(i);
            sysSqlConfCustom.setScType(statementTypeMap.get(sysSqlConfCustom.getScType()));
        }
        return dataTableVo;
    }

}

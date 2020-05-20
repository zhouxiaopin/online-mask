package cn.sk.mask.business.service.impl;

import cn.sk.mask.business.service.IOrganService;
import org.springframework.stereotype.Service;

/**
 * 机构信息业务逻辑接口实现类
 */
@Service
public class OrganServiceImpl implements IOrganService {
//    @Autowired
//    private OrganMapper organMapper;
//
//    @Override
//    public ServerResponse<List<Map<String, Object>>> queryOrganTree(OrganQueryVo organQueryVo) {
//        List<OrganCustom> organCustoms = organMapper.selectListByQueryVo(organQueryVo);
//        List<Map<String, Object>> data = Lists.newArrayList();
//        for (int i = 0,len = organCustoms.size(); i < len; i++){
//            OrganCustom organCustom = organCustoms.get(i);
////            {id:6, pId:0, name:"福建省", open:true, nocheck:true},
////            { id:1, pId:0, name:"一级分类", open:true},
//            Map<String,Object> item = Maps.newHashMap();
//            item.put("id",organCustom.getOrgId());
//            item.put("pId",organCustom.getParentId());
//            item.put("name",organCustom.getOrgName());
//            item.put("open",true);
//            data.add(item);
////
//        }
//        return ServerResponse.createBySuccess(data);
//    }


}

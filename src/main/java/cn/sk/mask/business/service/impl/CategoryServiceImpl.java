package cn.sk.mask.business.service.impl;

import cn.sk.mask.base.service.impl.BaseServiceImpl;
import cn.sk.mask.business.mapper.CategoryMapper;
import cn.sk.mask.business.pojo.CategoryCustom;
import cn.sk.mask.business.pojo.CategoryQueryVo;
import cn.sk.mask.business.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryCustom, CategoryQueryVo> implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

   /* @Override
    public ServerResponse<List<Map<String, Object>>> queryCategoryTree(CategoryQueryVo categoryQueryVo) {
        List<CategoryCustom> categoryCustoms = categoryMapper.selectListByQueryVo(categoryQueryVo);
        List<Map<String, Object>> data = Lists.newArrayList();
        for (int i = 0,len = categoryCustoms.size(); i < len; i++){
            CategoryCustom categoryCustom = categoryCustoms.get(i);
//            {id:6, pId:0, name:"福建省", open:true, nocheck:true},
//            { id:1, pId:0, name:"一级分类", open:true},
            Map<String,Object> item = Maps.newHashMap();
            item.put("id",categoryCustom.getId());
            item.put("pId",categoryCustom.getParentId());
            item.put("name",categoryCustom.getName());
            item.put("level","");
            item.put("open",true);
            data.add(item);
//
        }
        return ServerResponse.createBySuccess(data);
    }*/

//    @Override
//    public DataTableVo<CategoryCustom> queryObjsByPage(CategoryQueryVo categoryQueryVo) {
//        return super.queryObjsByPage(categoryQueryVo);
//    }
}

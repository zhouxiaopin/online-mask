package cn.sk.mask.business.mapper;

import cn.sk.mask.base.mapper.IBaseMapper;
import cn.sk.mask.business.pojo.CategoryCustom;
import cn.sk.mask.business.pojo.CategoryQueryVo;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper extends IBaseMapper<CategoryCustom, CategoryQueryVo> {
}
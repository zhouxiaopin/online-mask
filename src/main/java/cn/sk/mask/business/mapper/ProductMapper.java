package cn.sk.mask.business.mapper;

import cn.sk.mask.base.mapper.IBaseMapper;
import cn.sk.mask.business.pojo.ProductCustom;
import cn.sk.mask.business.pojo.ProductQueryVo;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductMapper extends IBaseMapper<ProductCustom, ProductQueryVo> {
}
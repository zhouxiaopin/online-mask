package cn.sk.mask.business.service.impl;

import cn.sk.mask.base.service.impl.BaseServiceImpl;
import cn.sk.mask.business.common.Const;
import cn.sk.mask.business.mapper.ProductMapper;
import cn.sk.mask.business.pojo.ProductCustom;
import cn.sk.mask.business.pojo.ProductQueryVo;
import cn.sk.mask.business.service.IProductService;
import cn.sk.mask.sys.vo.DataTableVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductCustom, ProductQueryVo>  implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Value("${sk.httpPrefix}")
    private String httpPrefix;
    @Autowired
    private HttpServletRequest request;



    @Override
    public DataTableVo<ProductCustom> queryObjsByPage(ProductQueryVo productQueryVo) {
        String contextPath = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+
                request.getServerPort()+contextPath+"/";
        //数据封装
        DataTableVo<ProductCustom> dataTableVo = super.queryObjsByPage(productQueryVo);
        List<ProductCustom> data = dataTableVo.getData();
        for(int i = 0,len = data.size(); i < len; i++) {
            ProductCustom productCustom = data.get(i);
            String mainImage = productCustom.getMainImage();
            if(StringUtils.isNotBlank(mainImage)){
                productCustom.setMainImage(basePath+ Const.UPLOAD_IMG_PATH+mainImage);
            }
//            productCustom.setMainImage(imgHttpPrefix+Const.UPLOAD_IMG_PATH+productCustom.getMainImage());
        }


        return dataTableVo;
    }
}

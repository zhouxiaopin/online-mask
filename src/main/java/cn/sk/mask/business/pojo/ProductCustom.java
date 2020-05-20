package cn.sk.mask.business.pojo;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCustom extends Product {
    private String categoryName;
    public ProductCustom(Integer id, Integer categoryId, String name, String nameEn, String mainImage, String subImages, Integer sort, String recordStatus, String subtitle, String subtitleEn, String productParam, String productParamEn, String productDetail, String productDetailEn, String expand1, String expand2, String expand3, String expand4, Date updateTime, Date createTime) {
        super(id, categoryId, name, nameEn, mainImage, subImages, sort, recordStatus, subtitle, subtitleEn, productParam, productParamEn, productDetail, productDetailEn, expand1, expand2, expand3, expand4, updateTime, createTime);
    }
}
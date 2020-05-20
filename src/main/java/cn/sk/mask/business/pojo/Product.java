package cn.sk.mask.business.pojo;

import java.util.Date;

public class Product {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String nameEn;

    private String mainImage;

    private String subImages;

    private Integer sort;

    private String recordStatus;

    private String subtitle;

    private String subtitleEn;

    private String productParam;

    private String productParamEn;

    private String productDetail;

    private String productDetailEn;

    private String expand1;

    private String expand2;

    private String expand3;

    private String expand4;

    private Date updateTime;

    private Date createTime;

    public Product(Integer id, Integer categoryId, String name, String nameEn, String mainImage, String subImages, Integer sort, String recordStatus, String subtitle, String subtitleEn, String productParam, String productParamEn, String productDetail, String productDetailEn, String expand1, String expand2, String expand3, String expand4, Date updateTime, Date createTime) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.nameEn = nameEn;
        this.mainImage = mainImage;
        this.subImages = subImages;
        this.sort = sort;
        this.recordStatus = recordStatus;
        this.subtitle = subtitle;
        this.subtitleEn = subtitleEn;
        this.productParam = productParam;
        this.productParamEn = productParamEn;
        this.productDetail = productDetail;
        this.productDetailEn = productDetailEn;
        this.expand1 = expand1;
        this.expand2 = expand2;
        this.expand3 = expand3;
        this.expand4 = expand4;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Product() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn == null ? null : nameEn.trim();
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage == null ? null : mainImage.trim();
    }

    public String getSubImages() {
        return subImages;
    }

    public void setSubImages(String subImages) {
        this.subImages = subImages == null ? null : subImages.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus == null ? null : recordStatus.trim();
    }

    public String getExpand1() {
        return expand1;
    }

    public void setExpand1(String expand1) {
        this.expand1 = expand1 == null ? null : expand1.trim();
    }

    public String getExpand2() {
        return expand2;
    }

    public void setExpand2(String expand2) {
        this.expand2 = expand2 == null ? null : expand2.trim();
    }

    public String getExpand3() {
        return expand3;
    }

    public void setExpand3(String expand3) {
        this.expand3 = expand3 == null ? null : expand3.trim();
    }

    public String getExpand4() {
        return expand4;
    }

    public void setExpand4(String expand4) {
        this.expand4 = expand4 == null ? null : expand4.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? null : subtitle.trim();
    }

    public String getSubtitleEn() {
        return subtitleEn;
    }

    public void setSubtitleEn(String subtitleEn) {
        this.subtitleEn = subtitleEn == null ? null : subtitleEn.trim();
    }

    public String getProductParam() {
        return productParam;
    }

    public void setProductParam(String productParam) {
        this.productParam = productParam == null ? null : productParam.trim();
    }

    public String getProductParamEn() {
        return productParamEn;
    }

    public void setProductParamEn(String productParamEn) {
        this.productParamEn = productParamEn == null ? null : productParamEn.trim();
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail == null ? null : productDetail.trim();
    }

    public String getProductDetailEn() {
        return productDetailEn;
    }

    public void setProductDetailEn(String productDetailEn) {
        this.productDetailEn = productDetailEn == null ? null : productDetailEn.trim();
    }
}
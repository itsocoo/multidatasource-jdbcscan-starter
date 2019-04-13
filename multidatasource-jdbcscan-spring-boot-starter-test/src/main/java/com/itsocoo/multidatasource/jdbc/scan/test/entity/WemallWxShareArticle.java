package com.itsocoo.multidatasource.jdbc.scan.test.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class WemallWxShareArticle implements Serializable {

    private static final long serialVersionUID = 3184283145156339652L;

    private Long id;
    private String brand;
    private String title;
    private String picUrl;
    private String linkUrl;
    private String tags;
    private String source;
    private String subTitle;
    private String marketingWords;
    private Integer sortBy;
    private Date createTime;

    public String toSqlString() {

        String sb = "(" +
                "'" + brand + "'" + "," +
                id + "," +
                "'" + brand + id + "'," +
                "'" + (StringUtils.isNotBlank(title) ? title.replaceAll("'", "") : title) + "'," +
                "'" + picUrl + "'," +
                "'" + linkUrl + "'," +
                "'" + tags + "'," +
                "'" + source + "'," +
                "'" + (StringUtils.isNotBlank(subTitle) ? subTitle.replaceAll("'", "") : subTitle) + "'," +
                "'" + marketingWords + "'," +
                sortBy + "," +
                "'" + createTime + "'" +
                ")";
        return sb;
    }

    public WemallWxShareArticle() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getTags() {
        return tags;
    }

    public Integer getSortBy() {
        return sortBy;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getMarketingWords() {
        return marketingWords;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setMarketingWords(String marketingWords) {
        this.marketingWords = marketingWords;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setSortBy(Integer sortBy) {
        this.sortBy = sortBy;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WemallWxShareArticle{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", title='" + title + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", tags='" + tags + '\'' +
                ", source='" + source + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", marketingWords='" + marketingWords + '\'' +
                ", sortBy=" + sortBy +
                ", createTime=" + createTime +
                '}';
    }
}

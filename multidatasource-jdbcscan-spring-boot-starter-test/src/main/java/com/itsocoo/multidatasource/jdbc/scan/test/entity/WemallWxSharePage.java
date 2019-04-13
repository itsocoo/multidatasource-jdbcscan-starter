package com.itsocoo.multidatasource.jdbc.scan.test.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class WemallWxSharePage implements Serializable {
    private static final long serialVersionUID = 3184283145156339652L;

    private String brand;
    // 主键ID
    private Long id;
    // 页面地址
    private String pageUrl;
    // 页面标题
    private String pageTitle;
    // 创建人的openId
    private String createByOpenid;
    // 头像
    private String portrait;
    private String status;
    private String platform;
    private String coverPic;
    private String region;
    private String type;
    private String templateId;
    private String wechatVersion;
    private String pictures;
    // 创建时间
    private Date createTime = new Date();
    // 分享次数
    private Long shareCount;
    // 打开次数
    private Long openCount;

    private String nickname;
    private String author;

    private Long shareMomentCount;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWechatVersion() {
        return wechatVersion;
    }

    public String getPictures() {
        return pictures;
    }

    public void setWechatVersion(String wechatVersion) {
        this.wechatVersion = wechatVersion;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public void setCreateByOpenid(String createByOpenid) {
        this.createByOpenid = createByOpenid;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getCreateByOpenid() {
        return createByOpenid;
    }

    public void setCreateByOpenId(String createByOpenid) {
        this.createByOpenid = createByOpenid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Long getOpenCount() {
        return openCount;
    }

    public void setOpenCount(Long openCount) {
        this.openCount = openCount;
    }

    public Long getShareMomentCount() {
        return shareMomentCount;
    }

    public void setShareMomentCount(Long shareMomentCount) {
        this.shareMomentCount = shareMomentCount;
    }

    public WemallWxSharePage() {
    }

    @Override
    public String toString() {
        return "WemallWxSharePage{" +
                "id=" + id +
                ", pageUrl='" + pageUrl + '\'' +
                ", pageTitle='" + pageTitle + '\'' +
                ", createByOpenid='" + createByOpenid + '\'' +
                ", portrait='" + portrait + '\'' +
                ", status='" + status + '\'' +
                ", platform='" + platform + '\'' +
                ", coverPic='" + coverPic + '\'' +
                ", region='" + region + '\'' +
                ", type='" + type + '\'' +
                ", templateId='" + templateId + '\'' +
                ", wechatVersion='" + wechatVersion + '\'' +
                ", pictures='" + pictures + '\'' +
                ", createTime=" + createTime +
                ", shareCount=" + shareCount +
                ", openCount=" + openCount +
                ", nickname='" + nickname + '\'' +
                ", author='" + author + '\'' +
                ", shareMomentCount=" + shareMomentCount +
                '}';
    }

    // page_url AS pageUrl,page_title AS ,create_by_openid AS ,create_time AS ,share_count AS ,open_count AS ,,,,cover_pic AS ,,,template_id AS ,,wechat_version AS ,

    public String toSqlString() {
        String sb = "(" +
                "'" + brand + "'" + "," +
                id + "," +
                "'" + brand + id + "'," +
                "'" + pageUrl + "'," +
                "'" + (StringUtils.isNotBlank(pageTitle) ? pageTitle.replaceAll("'", "") : pageTitle) + "'," +
                "'" + createByOpenid + "'," +
                "'" + createTime + "'," +
                shareCount + "," +
                openCount + "," +
                "'" + portrait + "'," +
                "'" + status + "'," +
                "'" + platform + "'," +
                "'" + coverPic + "'," +
                "'" + region + "'," +
                "'" + type + "'," +
                "'" + templateId + "'," +
                "'" + (StringUtils.isNotBlank(nickname) ? nickname.replaceAll("'", "") : nickname) + "'," +
                "'" + wechatVersion + "'," +
                "'" + pictures + "'" +
                ")";
        return sb;
    }

    public static void main(String[] args) {
        String s = "dddd'dddd'".replaceAll("'", "");
        System.out.println(s);
    }
}

package com.itsocoo.multidatasource.jdbc.scan.test.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 微信模板消息
 * @date 2018/8/15 14:34
 */
public class WxTempMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String brand;
    private String openId;
    private String tmp = "guideMiniTmp";
    private String itemName = "微商城";
    private String type = "活动&软文";
    private String linkValue;
    private String linkKey = "url";
    private String content = "分享给顾客，并分享到朋友圈";
    private LocalDateTime time;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public WxTempMessage() {
    }

    public String getBrand() {

        return brand;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }

    public String getLinkKey() {
        return linkKey;
    }

    public void setLinkKey(String linkKey) {
        this.linkKey = linkKey;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WxTempMessage that = (WxTempMessage) o;
        return Objects.equals(brand, that.brand) &&
                Objects.equals(openId, that.openId) &&
                Objects.equals(tmp, that.tmp);
    }

    @Override
    public String toString() {
        return "WxTempMessage{" +
                "brand='" + brand + '\'' +
                ", openId='" + openId + '\'' +
                ", tmp='" + tmp + '\'' +
                ", itemName='" + itemName + '\'' +
                ", type='" + type + '\'' +
                ", linkValue='" + linkValue + '\'' +
                ", linkKey='" + linkKey + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, openId, tmp);
    }

}

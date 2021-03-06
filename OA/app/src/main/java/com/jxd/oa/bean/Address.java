package com.jxd.oa.bean;

import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Table;
import com.yftools.db.annotation.Transient;

/**
 * *****************************************
 * Description ：位置采集
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
@Table(name = "t_address")
public class Address extends AbstractBean {
    private String id;
    private String name;//采集名称
    private Float accuracy;//精度
    private String address;//地址
    private String coorType;//坐标类型
    private Double latitude;//纬度
    private Double longitude;//经度
    /**
     * 状态
     * 1 待采集——后台创建时状态
     * 2 提交采集——手机采集提交状态
     * 3 完成采集——手机提交，后台审核通过后状态
     * 4 废弃采集——后台取消采集状态或者后台重新采集状态
     */
    private Integer status;
    private String collectUserId;//采集人
    @Transient
    private double distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoorType() {
        return coorType;
    }

    public void setCoorType(String coorType) {
        this.coorType = coorType;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCollectUserId() {
        return collectUserId;
    }

    public void setCollectUserId(String collectUserId) {
        this.collectUserId = collectUserId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}

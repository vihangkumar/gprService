package com.incomm.wmp.gprServices.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {
    private String id;
    private String ipAddress;
    private String ipGeo;
    private String mobile;
    private String proxy;
    private String country;
    private String region;
    private String geoLat;
    private String geoLong;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpGeo() {
        return ipGeo;
    }

    public void setIpGeo(String ipGeo) {
        this.ipGeo = ipGeo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(String geoLat) {
        this.geoLat = geoLat;
    }

    public String getGeoLong() {
        return geoLong;
    }

    public void setGeoLong(String geoLong) {
        this.geoLong = geoLong;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", ipGeo='" + ipGeo + '\'' +
                ", mobile='" + mobile + '\'' +
                ", proxy='" + proxy + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", geoLat='" + geoLat + '\'' +
                ", geoLong='" + geoLong + '\'' +
                '}';
    }

}

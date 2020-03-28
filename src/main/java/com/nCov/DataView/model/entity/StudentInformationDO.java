package com.nCov.DataView.model.entity;

public class StudentInformationDO {
    private Integer id;

    private String province;

    private String city;

    private String country;

    private String area;

    private String address;

    private String transportion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getTransportion() {
        return transportion;
    }

    public void setTransportion(String transportion) {
        this.transportion = transportion == null ? null : transportion.trim();
    }
}
package com.cst2335.teamproject;

import java.io.Serializable;

/**
 * CovidData class stores information from each specific item from a query
 * @author Kevin Ong
 */

public class CovidData implements Serializable {

    String country;
    String countryCode;
    String province;
    String city;
    String cityCode;
    double lat;
    double lon;
    int cases;
    String status;
    String date;


    public CovidData(String country, String countryCode, String province, String city, String cityCode, double lat,
                     double lon, int cases, String status, String date) {
        this.country = country;
        this.countryCode = countryCode;
        this.province = province;
        this.city = city;
        this.cityCode = cityCode;
        this.lat = lat;
        this.lon = lon;
        this.cases = cases;
        this.status = status;
        this.date = date;
    }

    public CovidData() {
        this.country = "Canada";
        this.countryCode = "CA";
        this.province = "";
        this.city = "";
        this.cityCode = "";
        this.lat = 0;
        this.lon = 0;
        this.cases = 0;
        this.status = "confirmed";
        this.date = "";
    }

    public CovidData(String country, String countryCode, double lat, double lon, int cases, String status, String date) {

        this.country = country;
        this.countryCode = countryCode;
        this.province = "";
        this.city = "";
        this.cityCode = "";
        this.lat = lat;
        this.lon = lon;
        this.cases = cases;
        this.status = status;
        this.date = date;
    }

    public String toString() {
        return String.format("On %s there were %d cases in %s", getDate(), cases, country);
    }

    /* SETTERS AND GETTERS -------------------------------------*/

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        String full = this.date;
        String[] parts = full.split("T");
        date = parts[0];
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

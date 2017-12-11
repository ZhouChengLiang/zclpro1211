package org.zclpro.db.userexpint.entity;

public class UserExpInt {
    private Integer userId;

    private Integer experience;

    private Integer integral;

    private Integer countyCode;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(Integer countyCode) {
        this.countyCode = countyCode;
    }
}
package org.zclpro.db.myh.entity;

import java.io.Serializable;

/**
 * @author zclwo
 * @date 2018年7月10日
 */
public class MyhDoctorDutyDto implements Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = -75335330147389194L;

  /**
   * 医生ID
   */
  private Integer doctorId;
  
  /**
   * 
   * 医生姓名
   */
  private String doctorName;
  
  /**
   * 医院ID
   */
  private Integer hospitalId;
  
  /**
   * 医院名称
   */
  private String hospitalName;
  
  /**
   * 科室ID
   */
  private Integer departmentId;
  
  /**
   * 科室名称
   */
  private String departmentName;
  
  /**
   * 值班类型
   */
  private Integer dutyType;
  
  /**
   * 周一值班情况
   */
  private String dutyMon;
  
  /**
   * 周二值班情况
   */
  private String dutyTues;
  
  /**
   * 
   * 周三值班情况
   */
  private String dutyWed;
  
  /**
   * 周四值班情况
   */
  private String dutyThurs;
  
  /**
   * 周五值班情况
   */
  private String dutyFri;
  
  /**
   * 周六值班情况
   */
  private String dutySat;
  
  /**
   * 周日值班情况
   */
  private String dutySun;
  
  /**
   * 区域编码 (330000)
   */
  private String areaCode;
  
  /**
   * 区域名称 zhejiangsheng
   */
  private String areaName;
  
  

  public Integer getHospitalId() {
    return hospitalId;
  }

  public void setHospitalId(Integer hospitalId) {
    this.hospitalId = hospitalId;
  }

  public String getHospitalName() {
    return hospitalName;
  }

  public void setHospitalName(String hospitalName) {
    this.hospitalName = hospitalName;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  public Integer getDoctorId() {
    return doctorId;
  }

  public void setDoctorId(Integer doctorId) {
    this.doctorId = doctorId;
  }

  public String getDoctorName() {
    return doctorName;
  }

  public void setDoctorName(String doctorName) {
    this.doctorName = doctorName;
  }

  public Integer getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Integer departmentId) {
    this.departmentId = departmentId;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public Integer getDutyType() {
    return dutyType;
  }

  public void setDutyType(Integer dutyType) {
    this.dutyType = dutyType;
  }

  public String getDutyMon() {
    return dutyMon;
  }

  public void setDutyMon(String dutyMon) {
    this.dutyMon = dutyMon;
  }

  public String getDutyTues() {
    return dutyTues;
  }

  public void setDutyTues(String dutyTues) {
    this.dutyTues = dutyTues;
  }

  public String getDutyWed() {
    return dutyWed;
  }

  public void setDutyWed(String dutyWed) {
    this.dutyWed = dutyWed;
  }

  public String getDutyThurs() {
    return dutyThurs;
  }

  public void setDutyThurs(String dutyThurs) {
    this.dutyThurs = dutyThurs;
  }

  public String getDutyFri() {
    return dutyFri;
  }

  public void setDutyFri(String dutyFri) {
    this.dutyFri = dutyFri;
  }

  public String getDutySat() {
    return dutySat;
  }

  public void setDutySat(String dutySat) {
    this.dutySat = dutySat;
  }

  public String getDutySun() {
    return dutySun;
  }

  public void setDutySun(String dutySun) {
    this.dutySun = dutySun;
  }
  
}

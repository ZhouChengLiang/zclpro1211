package org.zclpro.db.myh.entity;

import java.io.Serializable;

/**
 * @author zclwo
 * @date 2018年7月12日
 */
public class MyhHospitalDoctorDto implements Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = 2031134313510037493L;

  /**
   * 医生主键
   */
  private Integer doctorId;
  
  /**
   * 医生姓名
   */
  private String doctorName;
  
  /**
   * 医生头像
   */
  private String doctorImage;
  
  /**
   * 医生描述 (主治医师)
   */
  private String doctorTitles;
  
  /**
   * 医生擅长
   */
  private String doctorGoodAt;
  
  /**
   * 医生所在医院ID
   */
  private Integer hospitalId;
  
  /**
   * 医院名称
   */
  private String hospitalName;
  
  /**
   * 医生所在科室ID
   */
  private Integer departmentId;
  
  /**
   * 科室名称
   */
  private String departmentName;
  
  /**
   * 所在区域编码
   */
  private String areaCode;
  
  /**
   * 所在区域名称
   */
  private String areaName;
  
  /**
   * 医生排名
   */
  private Integer rank;

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

  public String getDoctorImage() {
    return doctorImage;
  }

  public void setDoctorImage(String doctorImage) {
    this.doctorImage = doctorImage;
  }

  public String getDoctorTitles() {
    return doctorTitles;
  }

  public void setDoctorTitles(String doctorTitles) {
    this.doctorTitles = doctorTitles;
  }

  public String getDoctorGoodAt() {
    return doctorGoodAt;
  }

  public void setDoctorGoodAt(String doctorGoodAt) {
    this.doctorGoodAt = doctorGoodAt;
  }

  public Integer getHospitalId() {
    return hospitalId;
  }

  public void setHospitalId(Integer hospitalId) {
    this.hospitalId = hospitalId;
  }

  public Integer getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Integer departmentId) {
    this.departmentId = departmentId;
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

  public String getHospitalName() {
    return hospitalName;
  }

  public void setHospitalName(String hospitalName) {
    this.hospitalName = hospitalName;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public Integer getRank() {
    return rank;
  }

  public void setRank(Integer rank) {
    this.rank = rank;
  }
  
  
}

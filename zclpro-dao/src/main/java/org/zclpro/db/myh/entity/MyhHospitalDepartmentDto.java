package org.zclpro.db.myh.entity;

import java.io.Serializable;

/**
 * @author zclwo
 * @date 2018年7月10日
 */
public class MyhHospitalDepartmentDto implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 7596343028763977663L;

  /**
   * 科室Id
   */
  private Integer departmentId;
  
  /**
   * 科室名称
   */
  private String departmentName;
  
  /**
   * 上级科室ID
   */
  private Integer parentDepartmentId;
  
  /**
   * 科室级别
   */
  private Integer departmentLevel;
  
  /**
   * 医院ID
   */
  private Integer hospitalId;
  
  /**
   * 区域编码 (330000)
   */
  private String areaCode;
  
  /**
   * 区域名称 zhejiangsheng
   */
  private String areaName;

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

  public Integer getParentDepartmentId() {
    return parentDepartmentId;
  }

  public void setParentDepartmentId(Integer parentDepartmentId) {
    this.parentDepartmentId = parentDepartmentId;
  }

  public Integer getDepartmentLevel() {
    return departmentLevel;
  }

  public void setDepartmentLevel(Integer departmentLevel) {
    this.departmentLevel = departmentLevel;
  }

  public Integer getHospitalId() {
    return hospitalId;
  }

  public void setHospitalId(Integer hospitalId) {
    this.hospitalId = hospitalId;
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
  
}

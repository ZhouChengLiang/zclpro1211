package org.zclpro.db.myh.entity;

import java.io.Serializable;

/**
 * @author zclwo
 * @date 2018年7月10日
 */
public class MyhHospitalInfoDto implements Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = -474540220825856861L;

  /**
   * 医院ID
   */
  private Integer hospitalId;
  
  /**
   * 医院名称
   */
  private String hospitalName;
  
  /**
   * 医院标签
   */
  private String hospitalTag;
  
  /**
   * 医院地址
   */
  private String hospitalAddress;
  
  /**
   * 区域编码 (330000)
   */
  private String areaCode;
  
  /**
   * 区域名称 zhejiangsheng
   */
  private String areaName;
  
  /**
   * 排名
   */
  private Integer rank;

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

  public String getHospitalTag() {
    return hospitalTag;
  }

  public void setHospitalTag(String hospitalTag) {
    this.hospitalTag = hospitalTag;
  }

  public String getHospitalAddress() {
    return hospitalAddress;
  }

  public void setHospitalAddress(String hospitalAddress) {
    this.hospitalAddress = hospitalAddress;
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

  public Integer getRank() {
    return rank;
  }

  public void setRank(Integer rank) {
    this.rank = rank;
  }
  
}

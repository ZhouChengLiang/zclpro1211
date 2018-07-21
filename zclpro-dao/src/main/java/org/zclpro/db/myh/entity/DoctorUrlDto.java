package org.zclpro.db.myh.entity;

import java.io.Serializable;

/**
 * @author zclwo
 * @date 2018年7月12日
 */
public class DoctorUrlDto implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 5812390797314723335L;

  private String url;
  
  private Integer rank;
  
  private String doctorTitles;
  
  private MyhEnum myhEnum;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public MyhEnum getMyhEnum() {
    return myhEnum;
  }

  public void setMyhEnum(MyhEnum myhEnum) {
    this.myhEnum = myhEnum;
  }

  public Integer getRank() {
    return rank;
  }

  public void setRank(Integer rank) {
    this.rank = rank;
  }

  public String getDoctorTitles() {
    return doctorTitles;
  }

  public void setDoctorTitles(String doctorTitles) {
    this.doctorTitles = doctorTitles;
  }

@Override
public String toString() {
	return "DoctorUrlDto [url=" + url + ", rank=" + rank + ", doctorTitles=" + doctorTitles + "]";
}
  
}

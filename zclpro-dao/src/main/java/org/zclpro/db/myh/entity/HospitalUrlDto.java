/**
 * 
 */
package org.zclpro.db.myh.entity;

import java.io.Serializable;

/**
 * @author zclwo
 * @date 2018年7月10日
 */
public class HospitalUrlDto implements Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = -3796713922766012991L;

  private String url;
  
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

  @Override
  public String toString() {
    return "HospitalUrl [url=" + url + "]";
  }
  
  
}

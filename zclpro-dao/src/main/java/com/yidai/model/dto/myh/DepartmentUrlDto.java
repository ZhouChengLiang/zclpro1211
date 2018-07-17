package com.yidai.model.dto.myh;

import java.io.Serializable;

import org.zclpro.db.myh.entity.MyhEnum;

/**
 * @author zclwo
 * @date 2018年7月10日
 */
public class DepartmentUrlDto implements Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = -7686034352898000200L;

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
    return "DepartmentUrl [url=" + url + ", myhEnum=" + myhEnum + "]";
  }

  
}

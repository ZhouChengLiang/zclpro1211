package org.zclpro.db.myh.entity;


/**
 * @author zclwo
 * @date 2018年7月10日
 */
public enum MyhEnum {

  zhejiang("330000", "zhejiangsheng");

  private String code;

  private String name;

  MyhEnum(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static MyhEnum getByName(String name) {
    if (name != null) {
      for (MyhEnum me : values()) {
        if (me.name.equals(name)) {
          return me;
        }
      }
    }
    return null;
  }

  public static MyhEnum getByCode(String code) {
    if (code != null) {
      for (MyhEnum me : values()) {
        if (me.code.equals(code)) {
          return me;
        }
      }
    }
    return null;
  }

}

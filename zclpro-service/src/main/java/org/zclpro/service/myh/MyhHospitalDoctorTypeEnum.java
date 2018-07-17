package org.zclpro.service.myh;


/**
 * @author zclwo
 * @date 2018年7月10日
 */
public enum MyhHospitalDoctorTypeEnum {

  AM(0, "上午"),
  PM(1, "下午"),
  NG(2, "夜间"),
  ;

  private Integer code;

  private String name;

  MyhHospitalDoctorTypeEnum(Integer code, String name) {
    this.code = code;
    this.name = name;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static MyhHospitalDoctorTypeEnum getByName(String name) {
    if (name != null) {
      for (MyhHospitalDoctorTypeEnum me : values()) {
        if (me.name.equals(name)) {
          return me;
        }
      }
    }
    return null;
  }

  public static MyhHospitalDoctorTypeEnum getByCode(Integer code) {
    if (code != null) {
      for (MyhHospitalDoctorTypeEnum me : values()) {
        if (me.code.equals(code)) {
          return me;
        }
      }
    }
    return null;
  }

}

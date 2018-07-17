package org.zclpro.service.myh;


/**
 * @author zclwo
 * @date 2018年7月10日
 */
public enum MyhHospitalDepartmentEnum {

  FIRST(1, "一级科室"),
  SECOND(2, "二级科室")
  ;

  private Integer code;

  private String name;

  MyhHospitalDepartmentEnum(Integer code, String name) {
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

  public static MyhHospitalDepartmentEnum getByName(String name) {
    if (name != null) {
      for (MyhHospitalDepartmentEnum me : values()) {
        if (me.name.equals(name)) {
          return me;
        }
      }
    }
    return null;
  }

  public static MyhHospitalDepartmentEnum getByCode(Integer code) {
    if (code != null) {
      for (MyhHospitalDepartmentEnum me : values()) {
        if (me.code.equals(code)) {
          return me;
        }
      }
    }
    return null;
  }

}

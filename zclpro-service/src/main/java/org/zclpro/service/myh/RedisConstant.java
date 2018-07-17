package org.zclpro.service.myh;

/**
 * redis缓存常量
 *
 * @author ZJN
 * @version $Id: RedisConstant.java, v 0.1 2017-3-17 上午11:24:01 ZJN Exp $
 */
public interface RedisConstant {

  /**
   * 名医汇医院数据 {地区编码}
   */
  String MYH_HOSPITAL_DATA = "yidai_myh_hospital_data_{0}";

  /**
   * 名医汇医院科室数据{地区编码}
   */
  String MYH_HOSPITAL_DEPARTMENT_DATA = "yidai_myh_hospital_dept_data_{0}";
  
  /**
   * 名医汇 医院医生数据{地区编码}
   */
  String MYH_HOSPITAL_DOCTOR_DATA = "yidai_myh_hospital_dct_data_{0}";
  
  
  /**
   * 名医汇医生信息URL 地址 缓存  {地区编码}
   */
  String MYH_HOSPITAL_DOCTOR_URLS_DATA = "yidai_myh_hospital_dct_urls_{0}";
  
  /**
   * 名医汇医生排班信息缓存  {地区编码}
   */
  String MYH_DOCTOR_DUTY_DATA = "yidai_myh_dct_duty_data_{0}";
}

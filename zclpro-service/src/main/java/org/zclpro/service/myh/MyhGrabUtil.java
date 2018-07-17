package org.zclpro.service.myh;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zclpro.db.myh.entity.DoctorUrlDto;
import org.zclpro.db.myh.entity.HospitalUrlDto;
import org.zclpro.db.myh.entity.MyhEnum;

/**
 * @author zclwo
 * @date 2018年7月10日
 */
public class MyhGrabUtil {

  public static final String MYH_MAIN_URL = "http://www.mingyihui.net";
  
  public static final String MYH_GUAHAO_URL = "http://www.mingyihui.net/guahao";

  public static final String MYH_HOSPITAL_URL = "http://www.mingyihui.net/{0}_hospital.html";
  
  public static final String MYH_HOSPITAL_WITH_PAGE_URL = "http://www.mingyihui.net/{0}_hospital_{1}.html";

  public static List<HospitalUrlDto> getHospitalUrls() {
    List<HospitalUrlDto> list = new ArrayList<>();
    for (MyhEnum me : MyhEnum.values()) {
      HospitalUrlDto url = new HospitalUrlDto();
      url.setMyhEnum(me);
      url.setUrl(StringUtil.replaceStr(MYH_HOSPITAL_URL, me.getName()));
      list.add(url);
    }
    return list;
  }
  
  public static List<HospitalUrlDto> getHospitalWithPageUrls(Elements eles,HospitalUrlDto hospitalUrl,List<HospitalUrlDto> hospitalWithPageUrls){
    int pages = 1;
    for(Element e :eles){
        if("末页".equals(e.text())){
            String href = e.attr("href");
            int beginIndex = href.lastIndexOf("_");
            int endIndex = href.lastIndexOf(".");
            pages = Integer.valueOf(href.substring(beginIndex+1, endIndex));
            break;
        }
    }
    for(int i = 1;i<=pages;i++){
      HospitalUrlDto url = new HospitalUrlDto();
      url.setMyhEnum(hospitalUrl.getMyhEnum());
      url.setUrl(StringUtil.replaceStr(MyhGrabUtil.MYH_HOSPITAL_WITH_PAGE_URL, hospitalUrl.getMyhEnum().getName(),i));
      hospitalWithPageUrls.add(url);
    }
    return hospitalWithPageUrls;
  }
  
  public static List<DoctorUrlDto> getDoctorWithPageUrls(Elements eles,DoctorUrlDto doctorUrlDto,List<DoctorUrlDto> doctorPageUrls){
    int pages = 1;
    for(Element e :eles){
      if("末页".equals(e.text())){
        String href = e.attr("href");
        int beginIndex = href.lastIndexOf("_");
        int endIndex = href.lastIndexOf(".");
        pages = Integer.valueOf(href.substring(beginIndex+3, endIndex));
        break;
      }
    }
    for(int i = 1;i<=pages;i++){
      DoctorUrlDto url = new DoctorUrlDto();
      url.setMyhEnum(doctorUrlDto.getMyhEnum());
      url.setUrl(doctorUrlDto.getUrl().replace(".html", "_pn"+i+".html"));
      doctorPageUrls.add(url);
    }
    return doctorPageUrls;
  }


}

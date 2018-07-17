package org.zclpro.service.myh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zclpro.db.myh.entity.DepartmentUrlDto;
import org.zclpro.db.myh.entity.DoctorUrlDto;
import org.zclpro.db.myh.entity.HospitalUrlDto;
import org.zclpro.db.myh.entity.MyhDoctorDutyDto;
import org.zclpro.db.myh.entity.MyhHospitalDepartmentDto;
import org.zclpro.db.myh.entity.MyhHospitalDoctorDto;
import org.zclpro.db.myh.entity.MyhHospitalInfoDto;

/**
 * @author zclwo
 * @date 2018年7月10日
 */
@Service
public class MyhTaskService {

	private static final Logger logger = LoggerFactory.getLogger(MyhTaskService.class);

	@Autowired
	private MyhTaskCache myhTaskCache;

	private final BlockingDeque<DepartmentUrlDto> departmentLevelFirstQueue = new LinkedBlockingDeque<>(20480);

	private final BlockingDeque<DepartmentUrlDto> departmentLevelSecondQueue = new LinkedBlockingDeque<>(40960);

	private final BlockingDeque<DoctorUrlDto> doctorPrepareUrlQueue = new LinkedBlockingDeque<>(81920);

	private final BlockingDeque<DoctorUrlDto> doctorRealUrlQueue = new LinkedBlockingDeque<>(81920);
	@Resource(name = "taskExecutor")
    private Executor taskExecutor;
	
	public void grabMyhData() {
		taskExecutor.execute(() -> grabHospitalDatas());
		taskExecutor.execute(() -> grabDepartmentLevelFirstDatas());
		taskExecutor.execute(() -> grabDepartmentLevelSecondDatas());
		taskExecutor.execute(() -> grabPrepareDoctorDatas());
		taskExecutor.execute(() -> grabRealDoctorDatas());
	}

	/**
	 * 抓取医院数据
	 * 
	 */
	public void grabHospitalDatas() {
		List<HospitalUrlDto> hospitalUrls = MyhGrabUtil.getHospitalUrls();
		List<HospitalUrlDto> hospitalWithPageUrls = new ArrayList<>();
		List<MyhHospitalInfoDto> dtolist = new ArrayList<>();
		try {
			for (HospitalUrlDto hospitalUrl : hospitalUrls) {
				Connection conn = Jsoup.connect(hospitalUrl.getUrl());
				connectionWrapper(conn);
				Document doc = conn.get();
				Elements eles = doc.select("div.H_tra.w900 a");
				Collections.reverse(eles);
				MyhGrabUtil.getHospitalWithPageUrls(eles, hospitalUrl, hospitalWithPageUrls);
				RandomUtil.sleepTime(6);
			}
			for (HospitalUrlDto url : hospitalWithPageUrls) {
				Connection conn = Jsoup.connect(url.getUrl());
				connectionWrapper(conn);
				Document doucments = conn.get();
				Elements elements = doucments.select("ul.H_main li.H_list");
				for (Element element : elements) {
					MyhHospitalInfoDto dto = new MyhHospitalInfoDto();
					String hospitalName = element.select("h3").text();
					String hospitalDetailUrl = MyhGrabUtil.MYH_MAIN_URL + element.select("h3 a").attr("href");
					String departmentOfUrl = MyhGrabUtil.MYH_GUAHAO_URL
							+ element.select("h3 a").attr("href").replace(".html", "/");
					Integer hospitalId = Integer.valueOf(hospitalDetailUrl
							.substring(hospitalDetailUrl.lastIndexOf("_") + 1, hospitalDetailUrl.lastIndexOf(".")));
					String key = StringUtil.replaceStr(RedisConstant.MYH_HOSPITAL_DATA, url.getMyhEnum().getCode());
					// if(Util.isNotNull(redisCacheFacade.hget(key,
					// hospitalId.toString()))){
					// logger.info("【"+url.getMyhEnum().getName()+"】下
					// "+hospitalName+"已录入过~~~");
					// continue;
					// }
					Integer rank = Integer.valueOf(element.select("div.H_no").text().replace("NO.", ""));
					DepartmentUrlDto departmentUrl = new DepartmentUrlDto();
					departmentUrl.setMyhEnum(url.getMyhEnum());
					departmentUrl.setUrl(departmentOfUrl);
					departmentLevelFirstQueue.offer(departmentUrl);
					Elements spanEles = element.select("span.ispublic");
					List<String> tags = new ArrayList<>();
					for (Element ele : spanEles) {
						tags.add(ele.text());
					}
					String hospitalTag = StringUtils.join(tags, ",");
					String hospitalAddress = "";
					if (Util.isNotNull(element.select("dl dd")) && element.select("dl dd").size() > 1) {
						hospitalAddress = element.select("dl dd").get(1).text();
					}
					dto.setHospitalId(hospitalId);
					dto.setHospitalName(hospitalName);
					dto.setHospitalAddress(hospitalAddress);
					dto.setHospitalTag(hospitalTag);
					dto.setAreaName(url.getMyhEnum().getName());
					dto.setAreaCode(url.getMyhEnum().getCode());
					dto.setRank(rank);
					dtolist.add(dto);
				}
				RandomUtil.sleepTime(6);
			}
			if (Util.isNotNull(dtolist)) {
				logger.info("开始入库名医汇医院信息到数据库~~~~~~~~~~~");
				myhTaskCache.batchInsertHospitalInfo(dtolist);
			}
		} catch (Exception e) {
			logger.error("爬取名医汇的医院信息时出错", e);
		}
	}

	/**
	 * 包裹Connection 添加头信息
	 *
	 */
	private void connectionWrapper(Connection conn) {
		conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.header("Accept-Encoding", "gzip, deflate, sdch");
		conn.header("Accept-Language", "zh-CN,zh;q=0.8");
		conn.header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	}

	/**
	 * 抓取一级科室数据 并准备二级科室链接数据
	 */
	public void grabDepartmentLevelFirstDatas() {
		logger.info("开始抓取一级科室数据~~~~~~~~~~~~~~~~~~~~~~~");
		while (true) {
			try {
				DepartmentUrlDto departmentUrl = departmentLevelFirstQueue.poll(5, TimeUnit.MINUTES);
				if (departmentUrl == null) {
					break;
				}
				Connection conn = Jsoup.connect(departmentUrl.getUrl());
				connectionWrapper(conn);
				Document doc = conn.get();
				Elements elements = doc.select("div.illness_list.clearfix.H_link > ul > li:not(:first-child)");
				List<MyhHospitalDepartmentDto> list = new ArrayList<>();
				for (Element ele : elements) {
					String href = ele.select("a").attr("href");
					if (StringUtils.isEmpty(ele.select("a").text())) {
						continue;
					}
					DepartmentUrlDto secondLevelDepartmentUrl = new DepartmentUrlDto();
					secondLevelDepartmentUrl.setMyhEnum(departmentUrl.getMyhEnum());
					secondLevelDepartmentUrl.setUrl(href);
					logger.info("grabDepartmentLevelFirstDatas href >>> " + href);
					departmentLevelSecondQueue.offer(secondLevelDepartmentUrl);
					Integer hospitalId = Integer
							.valueOf(href.substring(href.lastIndexOf("_") + 1, href.lastIndexOf("/")));
					Integer departmentId = Integer
							.valueOf(href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf(".")));
					String departmentName = ele.select("a").text();
					MyhHospitalDepartmentDto dto = new MyhHospitalDepartmentDto();
					dto.setDepartmentId(departmentId);
					dto.setDepartmentName(departmentName);
					dto.setDepartmentLevel(MyhHospitalDepartmentEnum.FIRST.getCode());
					dto.setHospitalId(hospitalId);
					dto.setAreaCode(departmentUrl.getMyhEnum().getCode());
					dto.setAreaName(departmentUrl.getMyhEnum().getName());
					list.add(dto);
				}
				if (Util.isNotNull(list)) {
					myhTaskCache.batchInsertHospitalDepartment(list);
				}
				TimeUnit.SECONDS.sleep(6);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("一级科室数据抓取完毕~~~~~~~~~~~~~~~~~~~~~~~");
	}

	/**
	 * 抓取二级科室数据 并且准备好医生的链接地址
	 */
	public void grabDepartmentLevelSecondDatas() {
		logger.info("开始抓取二级科室数据~~~~~~~~~~~~~~~~~~~~~~~");
		while (true) {
			try {
				DepartmentUrlDto departmentUrl = departmentLevelSecondQueue.poll(5, TimeUnit.MINUTES);
				if (departmentUrl == null) {
					break;
				}
				Connection conn = Jsoup.connect(departmentUrl.getUrl());
				connectionWrapper(conn);
				Document doc = conn.get();
				Elements elements = doc
						.select("div.illness_classify.border > div:nth-child(3) > ul > li:not(:first-child)");
				List<MyhHospitalDepartmentDto> list = new ArrayList<>();
				for (Element ele : elements) {
					String href = ele.select("a").attr("href");
					if (StringUtils.isEmpty(ele.select("a").text())) {
						continue;
					}
					logger.info("grabDepartmentLevelSecondDatas href >>> " + href);
					DoctorUrlDto doctorUrlDto = new DoctorUrlDto();
					doctorUrlDto.setUrl(href);
					doctorUrlDto.setMyhEnum(departmentUrl.getMyhEnum());
					doctorPrepareUrlQueue.offer(doctorUrlDto);
					String part1 = href.substring(0, href.lastIndexOf("/"));
					String part2 = href.substring(href.lastIndexOf("/") + 1);
					Integer hospitalId = Integer.valueOf(part1.substring(part1.lastIndexOf("_") + 1));
					Integer parentDepartmentId = Integer.valueOf(part2.substring(0, part2.lastIndexOf("_")));
					Integer departmentId = Integer
							.valueOf(part2.substring(part2.lastIndexOf("_") + 1, part2.lastIndexOf(".")));
					String departmentName = ele.select("a").text();
					MyhHospitalDepartmentDto dto = new MyhHospitalDepartmentDto();
					dto.setDepartmentId(departmentId);
					dto.setDepartmentName(departmentName);
					dto.setDepartmentLevel(MyhHospitalDepartmentEnum.SECOND.getCode());
					dto.setHospitalId(hospitalId);
					dto.setParentDepartmentId(parentDepartmentId);
					dto.setAreaCode(departmentUrl.getMyhEnum().getCode());
					dto.setAreaName(departmentUrl.getMyhEnum().getName());
					list.add(dto);
				}
				if (Util.isNotNull(list)) {
					myhTaskCache.batchInsertHospitalDepartment(list);
				}
				TimeUnit.SECONDS.sleep(6);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("二级科室数据抓取完毕~~~~~~~~~~~~~~~~~~~~~~~");
	}

	/**
	 * 抓取医生数据准备工作
	 */
	public void grabPrepareDoctorDatas() {
		logger.info("开始准备抓取科室下的医生数据~~~~~~~~~~~~~~~~~~~~~~~");
		while (true) {
			try {
				DoctorUrlDto doctorUrlDto = doctorPrepareUrlQueue.poll(5, TimeUnit.MINUTES);
				List<DoctorUrlDto> doctorWithPageUrls = new ArrayList<>();
				if (doctorUrlDto == null) {
					break;
				}
				Connection conn = Jsoup.connect(doctorUrlDto.getUrl());
				connectionWrapper(conn);
				Document doc = conn.get();
				Elements eles = doc.select("div.H_tra.w1200 a");
				Collections.reverse(eles);
				MyhGrabUtil.getDoctorWithPageUrls(eles, doctorUrlDto, doctorWithPageUrls);
				TimeUnit.SECONDS.sleep(6);
				for (DoctorUrlDto dto : doctorWithPageUrls) {
					Connection innerConn = Jsoup.connect(dto.getUrl());
					connectionWrapper(innerConn);
					Elements innerEles = doc.select("div.H_doc_con > ul > li");
					for (Element ele : innerEles) {
						String url = ele.select("a").get(0).absUrl("href");
						String doctorTitles = ele.select("span.fl.doctor_titles").text();
						Integer rank = Integer.valueOf(ele.select("a > div").text().replace("NO.", ""));
						DoctorUrlDto realDto = new DoctorUrlDto();
						realDto.setDoctorTitles(doctorTitles);
						realDto.setRank(rank);
						realDto.setUrl(url);
						realDto.setMyhEnum(dto.getMyhEnum());
						doctorRealUrlQueue.offer(realDto);
					}
					TimeUnit.SECONDS.sleep(6);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("科室下准备抓取医生数据完毕~~~~~~~~~~~~~~~~~~~~~~~");
	}

	/**
	 * 开始抓取医生数据工作 并将医生值班信息数据也入库
	 */
	public void grabRealDoctorDatas() {
		logger.info("开始抓取二级科室下的医生数据~~~~~~~~~~~~~~~~~~~~~~~");
		while (true) {
			try {
				DoctorUrlDto doctorUrlDto = doctorRealUrlQueue.poll(5, TimeUnit.MINUTES);
				if (doctorUrlDto == null) {
					break;
				}
				String url = doctorUrlDto.getUrl();
				Connection conn = Jsoup.connect(url);
				connectionWrapper(conn);
				Document doc = conn.get();
				String doctorGoodAt = doc.select("div.details_contant.fr > div:nth-child(2) > p").text();
				Integer doctorId = Integer.valueOf(url.substring(url.lastIndexOf("_") + 1, url.lastIndexOf(".")));
				String doctorName = doc.select(" div.crumbs.w1200 > a:nth-child(4)").text();
				String hospitalIdStr = doc.select("div.crumbs.w1200 > a:nth-child(2)").attr("href");
				Integer hospitalId = Integer.valueOf(
						hospitalIdStr.substring(hospitalIdStr.lastIndexOf("_") + 1, hospitalIdStr.lastIndexOf(".")));
				String hospitalName = doc.select("div.crumbs.w1200 > a:nth-child(2)").text();
				String departmentIdStr = doc.select("div.crumbs.w1200 > a:nth-child(3)").attr("href");
				Integer departmentId = Integer.valueOf(departmentIdStr.substring(departmentIdStr.lastIndexOf("_") + 1,
						departmentIdStr.lastIndexOf(".")));
				String departmentName = doc.select("div.crumbs.w1200 > a:nth-child(3)").text();
				MyhHospitalDoctorDto dto = new MyhHospitalDoctorDto();
				dto.setDoctorId(doctorId);
				dto.setDoctorName(doctorName);
				dto.setDoctorTitles(doctorUrlDto.getDoctorTitles());
				dto.setRank(doctorUrlDto.getRank());
				dto.setDepartmentId(departmentId);
				dto.setDepartmentName(departmentName);
				dto.setDoctorGoodAt(doctorGoodAt);
				dto.setHospitalName(hospitalName);
				dto.setHospitalId(hospitalId);
				dto.setAreaCode(doctorUrlDto.getMyhEnum().getCode());
				dto.setAreaName(doctorUrlDto.getMyhEnum().getName());
				String doctorImage = StringUtil.replaceStr("image/system/doctor_head_pic_{0}.png",
						NumberUtils.getRandomCode(2));
				dto.setDoctorImage(doctorImage);
				myhTaskCache.singleInsertHospitalDoctor(dto);
				List<MyhDoctorDutyDto> myhDoctorDutyDtos = new ArrayList<>();
				Elements elementAM = doc.select("div.out_call > table > tbody > tr:nth-child(2) > td");
				addMyhDoctorDutyDto(elementAM, myhDoctorDutyDtos, dto, MyhHospitalDoctorTypeEnum.AM);
				Elements elementPM = doc.select("div.out_call > table > tbody > tr:nth-child(3) > td");
				addMyhDoctorDutyDto(elementPM, myhDoctorDutyDtos, dto, MyhHospitalDoctorTypeEnum.PM);
				Elements elementNG = doc.select("div.out_call > table > tbody > tr:nth-child(4) > td");
				addMyhDoctorDutyDto(elementNG, myhDoctorDutyDtos, dto, MyhHospitalDoctorTypeEnum.NG);
				if (Util.isNotNull(myhDoctorDutyDtos)) {
					logger.info("开始入库值班医生数据~~~~~~~~~~~~~~~~~~~~~~~");
					myhTaskCache.batchSaveOrUpdateDoctorDuty(myhDoctorDutyDtos);
				}
				TimeUnit.SECONDS.sleep(6);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("二级科室下抓取医生数据完毕~~~~~~~~~~~~~~~~~~~~~~~");
	}

	private void addMyhDoctorDutyDto(Elements elementAM, List<MyhDoctorDutyDto> myhDoctorDutyDtos,
			MyhHospitalDoctorDto source, MyhHospitalDoctorTypeEnum mdEnum) {
		MyhDoctorDutyDto dto = new MyhDoctorDutyDto();
		dto.setDoctorId(source.getDoctorId());
		dto.setDoctorName(source.getDoctorName());
		dto.setHospitalId(source.getHospitalId());
		dto.setHospitalName(source.getHospitalName());
		dto.setDepartmentId(source.getDepartmentId());
		dto.setDepartmentName(source.getDepartmentName());
		dto.setAreaCode(source.getAreaCode());
		dto.setAreaName(source.getAreaName());
		dto.setDutyType(mdEnum.getCode());
		for (int i = 1; i < elementAM.size(); i++) {
			if (i == 1) {
				dto.setDutyMon(elementAM.get(i).text());
				continue;
			}
			if (i == 2) {
				dto.setDutyTues(elementAM.get(i).text());
				continue;
			}
			if (i == 3) {
				dto.setDutyWed(elementAM.get(i).text());
				continue;
			}
			if (i == 4) {
				dto.setDutyThurs(elementAM.get(i).text());
				continue;
			}
			if (i == 5) {
				dto.setDutyFri(elementAM.get(i).text());
				continue;
			}
			if (i == 6) {
				dto.setDutySat(elementAM.get(i).text());
				continue;
			}
			if (i == 7) {
				dto.setDutySun(elementAM.get(i).text());
				continue;
			}
		}
		myhDoctorDutyDtos.add(dto);
	}

	/**
	 * 从缓存URL地址中抓取医生值班信息入库
	 */
	public void grabDoctorVisitDatas() {
		/*
		 * for(MyhEnum myhEnum :MyhEnum.values()){ String key =
		 * StringUtil.replaceStr(RedisConstant.MYH_HOSPITAL_DOCTOR_URLS_DATA,
		 * myhEnum.getCode()); List<String> urls = redisCacheFacade.hvals(key);
		 * for(String url :urls){ Connection conn = Jsoup.connect(url);
		 * connectionWrapper(conn); try { Document doc = conn.get(); Integer
		 * doctorId = Integer.valueOf(url.substring(url.lastIndexOf("_")+1,
		 * url.lastIndexOf("."))); String doctorName = doc.select(
		 * " div.crumbs.w1200 > a:nth-child(4)").text(); String hospitalIdStr =
		 * doc.select("div.crumbs.w1200 > a:nth-child(2)").attr("href"); Integer
		 * hospitalId =
		 * Integer.valueOf(hospitalIdStr.substring(hospitalIdStr.lastIndexOf("_"
		 * )+1, hospitalIdStr.lastIndexOf("."))); String hospitalName =
		 * doc.select("div.crumbs.w1200 > a:nth-child(2)").text(); String
		 * departmentIdStr = doc.select("div.crumbs.w1200 > a:nth-child(3)"
		 * ).attr("href"); Integer departmentId =
		 * Integer.valueOf(departmentIdStr.substring(departmentIdStr.lastIndexOf
		 * ("_")+1, departmentIdStr.lastIndexOf("."))); String departmentName =
		 * doc.select("div.crumbs.w1200 > a:nth-child(3)").text();
		 * MyhHospitalDoctorDto dto = new MyhHospitalDoctorDto();
		 * dto.setDoctorId(doctorId); dto.setDoctorName(doctorName);
		 * dto.setDepartmentId(departmentId);
		 * dto.setDepartmentName(departmentName);
		 * dto.setHospitalName(hospitalName); dto.setHospitalId(hospitalId);
		 * dto.setAreaCode(myhEnum.getCode());
		 * dto.setAreaName(myhEnum.getName()); List<MyhDoctorDutyDto>
		 * myhDoctorDutyDtos = new ArrayList<>(); Elements elementAM =
		 * doc.select("div.out_call > table > tbody > tr:nth-child(2) > td");
		 * addMyhDoctorDutyDto(elementAM, myhDoctorDutyDtos, dto,
		 * MyhHospitalDoctorTypeEnum.AM); Elements elementPM = doc.select(
		 * "div.out_call > table > tbody > tr:nth-child(3) > td");
		 * addMyhDoctorDutyDto(elementPM, myhDoctorDutyDtos, dto,
		 * MyhHospitalDoctorTypeEnum.PM); Elements elementNG = doc.select(
		 * "div.out_call > table > tbody > tr:nth-child(4) > td");
		 * addMyhDoctorDutyDto(elementNG, myhDoctorDutyDtos, dto,
		 * MyhHospitalDoctorTypeEnum.NG); if(Util.isNotNull(myhDoctorDutyDtos)){
		 * logger.info("开始入库值班医生数据~~~~~~~~~~~~~~~~~~~~~~~");
		 * //myhGrabDataFacade.batchSaveOrUpdateDoctorDuty(myhDoctorDutyDtos); }
		 * TimeUnit.SECONDS.sleep(6); } catch (Exception e) { logger.error(
		 * "从缓存URL地址中抓取医生值班信息入库 出错~~~~~~",e); }
		 * 
		 * } }
		 */}

}

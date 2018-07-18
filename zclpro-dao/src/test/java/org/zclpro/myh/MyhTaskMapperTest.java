package org.zclpro.myh;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zclpro.db.myh.entity.MyhDoctorDutyDto;
import org.zclpro.db.myh.entity.MyhHospitalDepartmentDto;
import org.zclpro.db.myh.entity.MyhHospitalDoctorDto;
import org.zclpro.db.myh.entity.MyhHospitalInfoDto;
import org.zclpro.db.myh.impl.MyhTaskMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-dao.xml"})
@ActiveProfiles("dev")
public class MyhTaskMapperTest {
	
	@Autowired
	private MyhTaskMapper myhTaskMapper;
	
	/**
	 * 测试保存医院信息
	 */
	@Test
	public void testBatchInsertHospitalInfo(){
		List<MyhHospitalInfoDto> dtolist = new ArrayList<>();
		MyhHospitalInfoDto dto = new MyhHospitalInfoDto();
		dto.setAreaCode("330000");
		dto.setAreaName("浙江");
		dto.setHospitalAddress("杭州市西湖区");
		dto.setHospitalId(1);
		dto.setHospitalName("最好的医院");
		dto.setHospitalTag("公立");
		dto.setRank(1);
		dtolist.add(dto);
		myhTaskMapper.batchInsertHospitalInfo(dtolist);
	}
	
	/**
	 * 测试保存科室信息
	 */
	@Test
	public void testBatchInsertHospitalDepartment(){
		List<MyhHospitalDepartmentDto> list = new ArrayList<>();
		MyhHospitalDepartmentDto dto = new MyhHospitalDepartmentDto();
		dto.setAreaCode("330000");
		dto.setAreaName("zhejiangsheng");
		dto.setDepartmentId(1111111);
		dto.setDepartmentLevel(1);
		dto.setDepartmentName("部门1");
		dto.setHospitalId(391);
		dto.setParentDepartmentId(null);
		list.add(dto);
		myhTaskMapper.batchInsertHospitalDepartment(list);
	}
	
	/**
	 * 保存医生信息
	 */
	@Test
	public void testSingleInsertHospitalDoctor(){
		MyhHospitalDoctorDto dto = new MyhHospitalDoctorDto();
		dto.setAreaCode("330000");
		dto.setAreaName("杭州市");
		dto.setDepartmentId(1);
		dto.setDepartmentName("");
		dto.setDoctorGoodAt("");
		dto.setDoctorId(1);
		dto.setDoctorImage("");
		dto.setDoctorTitles("");
		dto.setHospitalId(1);
		dto.setHospitalName("杭州市第二医院");
		dto.setRank(1);
		myhTaskMapper.singleInsertHospitalDoctor(dto);
	}
	
	/**
	 * 保存值班信息
	 */
	@Test
	public void testBatchSaveOrUpdateDoctorDuty(){
		List<MyhDoctorDutyDto> myhDoctorDutyDtos = new ArrayList<>();
		MyhDoctorDutyDto dto = new MyhDoctorDutyDto();
		dto.setAreaCode("330000");
		dto.setAreaName("杭州市");
		dto.setDepartmentId(1111111111);
		dto.setDepartmentName("科室1");
		dto.setDoctorId(1);
		dto.setDoctorName("医生1");
		dto.setDutyFri("");
		dto.setDutyMon("");
		dto.setDutySat("");
		dto.setDutySun("");
		dto.setDutyThurs("");
		dto.setDutyTues("");
		dto.setDutyType(0);
		dto.setDutyWed("");
		dto.setHospitalId(391);
		dto.setHospitalName("杭州第一医院");
		myhDoctorDutyDtos.add(dto);
		myhTaskMapper.batchSaveOrUpdateDoctorDuty(myhDoctorDutyDtos);
	}
}

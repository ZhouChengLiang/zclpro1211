package org.zclpro.service.myh;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zclpro.db.myh.entity.MyhDoctorDutyDto;
import org.zclpro.db.myh.entity.MyhHospitalDepartmentDto;
import org.zclpro.db.myh.entity.MyhHospitalDoctorDto;
import org.zclpro.db.myh.entity.MyhHospitalInfoDto;
import org.zclpro.db.myh.impl.MyhTaskMapper;
import org.zclpro.service.common.BaseCache;
@Service
public class MyhTaskCache extends BaseCache{
	
	
	@Autowired
	private MyhTaskMapper myhTaskMapper;
	
	
	
	@Override
	public int getExpire() {
		return 0;
	}



	public void batchInsertHospitalInfo(List<MyhHospitalInfoDto> dtolist) {
		myhTaskMapper.batchInsertHospitalInfo(dtolist);
		
	}



	public void batchInsertHospitalDepartment(List<MyhHospitalDepartmentDto> list) {
		myhTaskMapper.batchInsertHospitalDepartment(list);
		
	}



	public void singleInsertHospitalDoctor(MyhHospitalDoctorDto dto) {
		myhTaskMapper.singleInsertHospitalDoctor(dto);
		
	}



	public void batchSaveOrUpdateDoctorDuty(List<MyhDoctorDutyDto> myhDoctorDutyDtos) {
		myhTaskMapper.batchSaveOrUpdateDoctorDuty(myhDoctorDutyDtos);
		
	}

}

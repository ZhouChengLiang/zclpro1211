package org.zclpro.db.myh.impl;

import java.util.List;

import org.zclpro.db.myh.entity.MyhDoctorDutyDto;
import org.zclpro.db.myh.entity.MyhHospitalDepartmentDto;
import org.zclpro.db.myh.entity.MyhHospitalDoctorDto;
import org.zclpro.db.myh.entity.MyhHospitalInfoDto;

public interface MyhTaskMapper {

	void batchInsertHospitalInfo(List<MyhHospitalInfoDto> dtolist);

	void batchInsertHospitalDepartment(List<MyhHospitalDepartmentDto> list);

	void singleInsertHospitalDoctor(MyhHospitalDoctorDto dto);

	void batchSaveOrUpdateDoctorDuty(List<MyhDoctorDutyDto> myhDoctorDutyDtos);

}

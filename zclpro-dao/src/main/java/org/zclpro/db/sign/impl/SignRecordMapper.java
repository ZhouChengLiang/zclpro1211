package org.zclpro.db.sign.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zclpro.db.sign.entity.SignRecord;

public interface SignRecordMapper {
	int insert(SignRecord record);

    int insertSelective(SignRecord record);
    
    @Select("SELECT t.userid FROM sign_record t where t.county_code = #{countyCode} and t.lastdate = #{lastDate} ")
    List<Integer> listSignedUser(@Param("countyCode") Integer countyCode,@Param("lastDate") String lastDate);
    
    SignRecord selectRecordBy(SignRecord record);
    
    int saveOrUpdate(SignRecord record);
}
package org.zclpro.db.grade.entity;

import org.zclpro.common.redistool.RedisHashIgnore;
import org.zclpro.db.common.IPOJOCache;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GradeInfo extends IPOJOCache{
    private Integer id;

    private Integer grade;

    private Integer requiredExperience;

    private String gradeName;

    private String gradeIcon;

    private Integer currentUsers;

    private Integer countyCode;
    
    @RedisHashIgnore
    public transient static final String NEXTGRADEINFO = "NEXTGRADEINFO";
    
    @RedisHashIgnore
    public transient static final String CURGRADEINFO = "CURGRADEINFO";

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GradeInfo other = (GradeInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
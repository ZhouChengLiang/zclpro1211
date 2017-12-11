package org.zclpro.db.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class IPOJOCache extends SearchDate {

	 public Integer getKey(){
		 return getId();
	 }
	 public abstract Integer getId();
	 
	 private String dynamicSql;
}
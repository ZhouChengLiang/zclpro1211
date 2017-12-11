package org.zclpro.db.common;

import lombok.Data;

@Data
public class KeySortNum{
	
	
	private int id;
	private long sortNum;
	
	public KeySortNum(int id, long sortNum) {
		super();
		this.id = id;
		this.sortNum = sortNum;
	}

	public KeySortNum() {
		super();
	}
	
	
}
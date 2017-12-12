package org.zclpro.db.constellatory.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ConstellatoryWeek implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4945668769857102403L;

	private String year;

    private Integer weekth;

    private String name;

    private Integer code;

    private String date;

    private String health;

    private String job;

    private String love;

    private String money;

    private String work;

}
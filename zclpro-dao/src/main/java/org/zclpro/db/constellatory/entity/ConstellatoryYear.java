package org.zclpro.db.constellatory.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ConstellatoryYear implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4874806573410706581L;

	private Integer year;

    private String date;

    private String name;

    private Integer code;

    private String shortInfo;

    private String detailInfo;

    private String career;

    private String love;

    private String health;

    private String finance;

    private String luckeyStone;

    private String future;

}
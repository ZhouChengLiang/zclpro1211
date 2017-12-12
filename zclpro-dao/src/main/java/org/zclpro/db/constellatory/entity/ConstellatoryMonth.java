package org.zclpro.db.constellatory.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ConstellatoryMonth implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6639974545674979825L;

	private String year;

    private Integer month;

    private String date;

    private String name;

    private Integer code;

    private String alls;

    private String health;

    private String love;

    private String money;

    private String work;

    private String happyMagic;

}
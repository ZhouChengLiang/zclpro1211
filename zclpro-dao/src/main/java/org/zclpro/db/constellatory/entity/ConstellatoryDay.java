package org.zclpro.db.constellatory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConstellatoryDay implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8886842847984946849L;

	private String dateId;

    private String date;

    private Integer code;

    private String name;

    private String datetimeStr;

    private String alls;

    private String work;

    private String health;

    private String love;

    private String money;

    private String color;

    private String number;

    private String friend;

    private String summary;

}
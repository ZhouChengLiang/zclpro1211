package org.zclpro.db.joke.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class JokeText {
    private String hashId;

    private String content;

    private Integer unixtime;

    private Date updatetime;

}
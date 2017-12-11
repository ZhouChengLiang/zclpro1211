package org.zclpro.db.joke.entity;

import java.util.Date;

public class JokeText {
    private String hashId;

    private String content;

    private Integer unixtime;

    private Date updatetime;

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(Integer unixtime) {
        this.unixtime = unixtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
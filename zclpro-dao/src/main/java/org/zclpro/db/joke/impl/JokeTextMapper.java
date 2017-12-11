package org.zclpro.db.joke.impl;

import org.zclpro.db.joke.entity.JokeText;

public interface JokeTextMapper {
    int insert(JokeText record);

    int insertSelective(JokeText record);
}
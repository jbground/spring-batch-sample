package com.jbground.batch.tasklet.chunk.reader;

import com.jbground.batch.model.User;
import com.jbground.batch.utils.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import java.util.ArrayList;
import java.util.List;

public class CreateUserReader<T> implements ItemReader<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int runCount = 1;
    private String group;

    public CreateUserReader(String group) {
        this.group = group;
    }

    @Override
    public T read(){



        User user = RandomGenerator.nextUser();
        user.setNum(runCount);
        user.setGroup(group);

        if(runCount++ > 100)
            return null;

        logger.info("create {} : {}",group, user);
        return (T) user;
    }
}

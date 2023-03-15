package com.jbground.batch.tasklet.chunk.processor;

import com.jbground.batch.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor<I, O> implements ItemProcessor<I, O> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String group;

    public UserProcessor(String group) {
        this.group = group;
    }

    @Override
    public O process(I item) throws Exception {
        User user = (User) item;
        user.setGroup(group);

        return (O) user;
    }

}

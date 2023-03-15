package com.jbground.batch.tasklet.chunk.reader;

import com.jbground.batch.model.JbgroundNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;

public class CompositeSampleReader<T> implements ItemReader<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int count = 1;
    @Override
    public T read() {

        JbgroundNumber number = new JbgroundNumber();
        number.setA(count++);

        if(count <= 11)
            return (T) number;
        else
            return null;

    }
}

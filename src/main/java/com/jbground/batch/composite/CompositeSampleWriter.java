package com.jbground.batch.composite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CompositeSampleWriter<T> implements ItemWriter<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            JbgroundNumber number = (JbgroundNumber) item;
            logger.info("A : {}, B : {}, result : {}", number.getA(), number.getB(), number.getResult());
        }
    }
}

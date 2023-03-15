package com.jbground.batch.tasklet.chunk.writer;

import com.jbground.batch.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class AsyncAccountItemWriter<T> implements ItemWriter<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            Account account = (Account) item;
            logger.info("{}", account);
        }
    }
}

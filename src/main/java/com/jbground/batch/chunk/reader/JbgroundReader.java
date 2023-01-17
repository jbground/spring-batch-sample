package com.jbground.batch.chunk.reader;

import org.springframework.batch.item.*;

public class JbgroundReader<T> implements ItemReader<T> {
    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}

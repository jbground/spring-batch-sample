package com.jbground.batch.tasklet.chunk.processor;

import org.springframework.batch.item.ItemProcessor;

public class JbgroundProcessor<I, O> implements ItemProcessor<I, O> {
    @Override
    public O process(I item) throws Exception {
        return null;
    }
}

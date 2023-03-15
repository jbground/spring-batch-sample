package com.jbground.batch.tasklet.chunk.processor;

import com.jbground.batch.model.ChunkSample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ChunkSampleProcessor<I, O> implements ItemProcessor<I, O> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private int playTime = 1;

    @Override
    public O process(I item) throws Exception {
//        logger.info("{}번째 실행", playTime++);
        ChunkSample sample = (ChunkSample) item;
        sample.setVal(playTime++);

        return (O) sample;
    }
}

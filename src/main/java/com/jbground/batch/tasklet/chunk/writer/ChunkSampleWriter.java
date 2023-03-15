package com.jbground.batch.tasklet.chunk.writer;

import com.jbground.batch.model.ChunkSample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ChunkSampleWriter<T> implements ItemWriter<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private int playTime = 1;

    @Override
    public void write(List<? extends T> items) throws Exception {

        for (T item : items) {
            ChunkSample sample = (ChunkSample) item;
            logger.info("{}번째 실행 - ChunkSample {}: ", playTime, sample.getVal());
        }
        playTime++;
    }
}

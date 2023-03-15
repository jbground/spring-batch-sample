package com.jbground.batch.tasklet.chunk.reader;

import com.jbground.batch.model.ChunkSample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

public class ChunkSampleReader<T> implements ItemReader<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int count = 1;
    @Override
    public T read() {

        if(count++ <= 200)
            return (T) new ChunkSample();
        else
            return null;

    }
}

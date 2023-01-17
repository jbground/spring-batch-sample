package com.jbground.batch.chunk.writer;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class JbgroundWriter<T> implements ItemWriter<T> {
    @Override
    public void write(List<? extends T> items) throws Exception {

    }
}

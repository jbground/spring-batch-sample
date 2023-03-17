package com.jbground.batch.tasklet.chunk.reader;

import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.LinkedList;
import java.util.List;

public class CustomItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> {

    private volatile int page = 1;
    private volatile int row = 100;

    private volatile boolean initialized = false;
    protected volatile List<T> results = new LinkedList<>();
    protected final Object lock = new Object();

    public CustomItemReader() {
        setSaveState(false);
        setName(ClassUtils.getShortName(this.getClass()));
    }

    @Override
    protected T doRead() throws Exception {
        synchronized (lock) {
            //results 값이 없어도 종료되면 안됨 오직 null일 때만 종료
            if (results == null) {
                return null;
            } else if (results.size() > 0) {
                return results.remove(0);
            } else {

                doReadPage();
                page++;

                return doRead();
            }
        }
    }

    public void doReadPage() {

    }

    @Override
    protected void doOpen() throws Exception {
        Assert.state(!initialized, "Cannot open an already opened ItemReader, call close first");
        initialized = true;

    }

    @Override
    protected void doClose() throws Exception {
        synchronized (lock) {
            initialized = false;
            results = null;
        }
    }
}

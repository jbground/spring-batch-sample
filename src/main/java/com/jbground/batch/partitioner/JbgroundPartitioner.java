package com.jbground.batch.partitioner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class JbgroundPartitioner implements Partitioner {

    private static final Logger logger = LoggerFactory.getLogger(JbgroundPartitioner.class);


    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partition = new HashMap<>();

        ExecutionContext executionContext1 = new ExecutionContext();
        executionContext1.put("group", "groupA");

        partition.put("groupA", executionContext1);

        ExecutionContext executionContext2 = new ExecutionContext();
        executionContext2.put("group", "groupB");

        partition.put("groupB", executionContext2);

        ExecutionContext executionContext3 = new ExecutionContext();
        executionContext3.put("group", "groupC");

        partition.put("groupC", executionContext3);

        return partition;

    }
}

package com.jbground.batch.partitioner;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class CustomPartitioner implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        HashMap<String, ExecutionContext> partition = new HashMap<>();

        int start = 1;
        int end = 10;

        for (int i = 0; i < gridSize; i++) {

            // 값 셋팅
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.put("start", start);
            executionContext.put("end", end);

            // 파티셔너에 넣기
            partition.put(String.valueOf(i), executionContext);

            // 다음 값을 셋팅하기
            start = end + 1;
            end = end + 10;

        }

        return partition;
    }
}

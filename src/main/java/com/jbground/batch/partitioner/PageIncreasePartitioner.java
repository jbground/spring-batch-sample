package com.jbground.batch.partitioner;

import kr.go.culture.batch.archive.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageIncreasePartitioner implements Partitioner {

    private static final Logger logger = LoggerFactory.getLogger(PageIncreasePartitioner.class);

    private final UrlRepository urlRepository;

    public PageIncreasePartitioner(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partition = new HashMap<>();

        try {
            List<Integer> indexList = urlRepository.findPageIncreaseIndexList();
            indexList = new ArrayList<>();
            indexList.add(543);
            indexList.add(667);

            logger.info("페이지 증가 방식 시작 : {}", indexList.size());

            for (int i : indexList) {
                ExecutionContext executionContext = new ExecutionContext();
                executionContext.put("index", i);
                executionContext.put("type", "page");

                partition.put(String.valueOf(i), executionContext);
            }

            return partition;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.jbground.batch.job;

import com.jbground.batch.model.Account;
import com.jbground.batch.partitioner.CustomPartitioner;
import com.jbground.batch.partitioner.PageIncreasePartitioner;
import com.jbground.batch.tasklet.chunk.processor.AsyncAccountItemProcessor;
import com.jbground.batch.tasklet.chunk.reader.PagingAccountItemReader;
import com.jbground.batch.tasklet.chunk.writer.AsyncAccountItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by jsjeong on 2023. 01. 18.
 * <pre>
 * spring batch job 생성 예제
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class PartitioningJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public PartitioningJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Async-Thread");
        executor.setMaxPoolSize(40);
        executor.setCorePoolSize(20);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }

    @Bean
    public Job jbground() throws Exception {
        return jobBuilderFactory.get("partitioning")
                .incrementer(new RunIdIncrementer())
                .preventRestart() //중단 시 재시작 방지
                .start(accountStep())
                .build();
    }

    public Step partitionerStep(){
        return stepBuilderFactory.get("collectPageIncreaseListStep")
                .partitioner(collectStep().getName(), new PageIncreasePartitioner(urlRepository))
                .step(collectStep())
                .gridSize(4)
                .taskExecutor(threadPoolTaskExecutor())
                .build();
    }

    @Bean
    public Step accountStep() throws Exception {
        return stepBuilderFactory.get("collectStep")
                .<ArchiveProcessor, ArchiveRecode>chunk(1)
                .reader(itemReader(null, null))  // 인풋타입의 아이템을 하나씩 반환
                .processor(itemProcessor())                 // 인풋타입을 받아서 아웃풋타입으로 리턴
                .writer(itemWriter())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .taskExecutor(threadPoolTaskExecutor())
                .build();
    }


    @Bean
    public AbstractPagingItemReader reader() throws Exception {
        PagingAccountItemReader<Account> reader = new PagingAccountItemReader<>();
        reader.setPageSize(100);
        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    public AsyncItemProcessor processor() throws Exception {
        AsyncItemProcessor<Account, Account> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(new AsyncAccountItemProcessor<>());
        asyncItemProcessor.setTaskExecutor(threadPoolTaskExecutor());
        asyncItemProcessor.afterPropertiesSet();
        return asyncItemProcessor;
    }


    @Bean
    public AsyncItemWriter writer() throws Exception {
        AsyncItemWriter<Account> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(new AsyncAccountItemWriter<>());
        asyncItemWriter.afterPropertiesSet();
        return asyncItemWriter;
    }

}

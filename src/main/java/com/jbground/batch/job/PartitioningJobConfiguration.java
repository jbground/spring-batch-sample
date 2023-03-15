package com.jbground.batch.job;

import com.jbground.batch.partitioner.JbgroundPartitioner;
import com.jbground.batch.tasklet.chunk.processor.UserProcessor;
import com.jbground.batch.tasklet.chunk.reader.CreateUserReader;
import com.jbground.batch.tasklet.chunk.writer.JbgroundWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by jsjeong on 2023. 01. 18.
 * <pre>
 * spring batch 파티셔닝 방식 생성 예제
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class PartitioningJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public PartitioningJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Part-Thread");
        executor.setMaxPoolSize(40);
        executor.setCorePoolSize(20);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }

    @Bean
    public Job jbground() throws Exception {
        return jobBuilderFactory.get("partitioningJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart() //중단 시 재시작 방지
                .start(partitionerStep())
                .build();
    }

    public Step partitionerStep() throws Exception {
        return stepBuilderFactory.get("partitionerStep")
                .partitioner("partitioner", new JbgroundPartitioner())
                .step(slaveStep())
                .gridSize(3)
                .taskExecutor(threadPoolTaskExecutor())
                .build();
    }

    @Bean
    public Step slaveStep() throws Exception {
        return stepBuilderFactory.get("userStep")
                .chunk(20)
                .reader(reader(null))  // 인풋타입의 아이템을 하나씩 반환
                .processor(processor(null))
                .writer(new JbgroundWriter<>())
                .build();
    }

    @Bean
    @StepScope
    public CreateUserReader reader(@Value("#{stepExecutionContext['group']}") String group) throws Exception {
        return new CreateUserReader<>(group);
    }

    @Bean
    @StepScope
    public UserProcessor processor(@Value("#{stepExecutionContext['group']}") String group){
        return new UserProcessor<>(group);
    }
}

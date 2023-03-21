package com.jbground.batch.job;

import com.jbground.batch.listener.JbgroundChunkListener;
import com.jbground.batch.model.Employee;
import com.jbground.batch.tasklet.chunk.reader.CustomItemReader;
import com.jbground.batch.tasklet.chunk.writer.JbgroundWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by jsjeong on 2023. 01. 18.
 * <pre>
 * spring batch Flow 방식 생성 예제
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class ListenerAndSkipJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;



    public ListenerAndSkipJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("ThreadPool");
        executor.setMaxPoolSize(10);
        executor.setCorePoolSize(5);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }

    @Bean
    public Job skipJob() throws Exception {
        return jobBuilderFactory.get("skipJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart() //중단 시 재시작 방지
                .start(sampleStep1())
                .next(sampleStep2())
                .next(sampleStep3())
                .build();
    }

    public Step sampleStep1() throws Exception {
        return stepBuilderFactory.get("sampleStep1")
                .chunk(100)
                .reader(new CustomItemReader<>())  // 인풋타입의 아이템을 하나씩 반환
                .writer(writer())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .faultTolerant()
                .skip(IllegalArgumentException.class).skipLimit(3)
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .retryPolicy(new AlwaysRetryPolicy())
                .build();
    }

    public Step sampleStep2() throws Exception {
        return stepBuilderFactory.get("sampleStep2")
                .chunk(100)
                .reader(new CustomItemReader<>())  // 인풋타입의 아이템을 하나씩 반환
                .writer(writer())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .faultTolerant()
                .build();
    }

    public Step sampleStep3() throws Exception {
        return stepBuilderFactory.get("sampleStep3")
                .chunk(100)
                .reader(new CustomItemReader<>())  // 인풋타입의 아이템을 하나씩 반환
                .writer(writer())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .faultTolerant()
                .skipLimit(5)
                .skip(Exception.class)
                .listener(new JbgroundChunkListener())
                .build();
    }

    public ItemWriter writer() {
        return new JbgroundWriter<>();
    }
}

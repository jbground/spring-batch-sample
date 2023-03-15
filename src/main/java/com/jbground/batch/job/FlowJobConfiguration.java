package com.jbground.batch.job;

import com.jbground.batch.tasklet.chunk.processor.UserProcessor;
import com.jbground.batch.tasklet.chunk.reader.CreateUserReader;
import com.jbground.batch.tasklet.chunk.writer.JbgroundWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by jsjeong on 2023. 01. 18.
 * <pre>
 * spring batch Flow 방식 생성 예제
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class FlowJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public FlowJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Flow-Thread");
        executor.setMaxPoolSize(10);
        executor.setCorePoolSize(5);
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }

    @Bean
    public Job flowJob() throws Exception {
        return jobBuilderFactory.get("flowJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart() //중단 시 재시작 방지
                .start(flowStep())
                .build().build();
    }

    public Flow flowStep() throws Exception {
        return new FlowBuilder<SimpleFlow>("default-flow")
                .split(threadPoolTaskExecutor()) //step 동시 실행
                .add(flow1(), flow2(), flow3())
                .build();
    }

    public Flow flow1() throws Exception {
        return new FlowBuilder<Flow>("flow-1")
                .start(userStep("flow-1"))
                .build();
    }

    public Flow flow2() throws Exception {
        return new FlowBuilder<Flow>("flow-2")
                .start(userStep("flow-2"))
                .build();
    }

    public Flow flow3() throws Exception {
        return new FlowBuilder<Flow>("flow-3")
                .start(userStep("flow-3"))
                .build();
    }

    public Step userStep(String value) throws Exception {
        return stepBuilderFactory.get("userStep")
                .chunk(100)
                .reader(new CreateUserReader<>(value))  // 인풋타입의 아이템을 하나씩 반환
                .processor(new UserProcessor<>(value))
                .writer(new JbgroundWriter<>())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .build();
    }

}

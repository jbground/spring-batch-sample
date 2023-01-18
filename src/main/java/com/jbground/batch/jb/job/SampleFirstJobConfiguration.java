package com.jbground.batch.jb.job;


import com.jbground.batch.jb.chunk.processor.JbgroundProcessor;
import com.jbground.batch.composite.CompositeSampleReader;
import com.jbground.batch.composite.CompositeSampleWriter;
import com.jbground.batch.config.InMemoryBatchConfigurer;
import com.jbground.batch.jb.listener.JbgroundChunkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsjeong on 2023. 1. 10.
 * <pre>
 * sample job configuration
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class SampleFirstJobConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SampleFirstJobConfiguration.class);
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    public SampleFirstJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public BatchConfigurer batchConfigurer() {
        return new InMemoryBatchConfigurer();
    }

    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Thread-Pool");
        executor.setMaxPoolSize(20);
        executor.setCorePoolSize(20);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(30);
        executor.initialize();
        return executor;
    }

    @Bean
    public Job firstJob() throws Exception {
        return jobBuilderFactory.get("first")
                .incrementer(new RunIdIncrementer())
                .preventRestart() //중단 시 재시작 방지
                .start(createDefaultFlow())
                .build().build();
    }

    @Bean
    @JobScope
    public Flow createDefaultFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("default-flow")
                .split(threadPoolTaskExecutor()) //step 동시 실행
                .add(createExecutingList())
                .build();
    }

    public Flow[] createExecutingList() throws Exception {
        List<Flow> flowList = new ArrayList<>();

        Flow flow = new FlowBuilder<Flow>("flow-" + 111)
                .start(defaultStep(111))
                .build();
        flowList.add(flow);

        return flowList.toArray(new Flow[]{});
    }

    public Step defaultStep(int index) throws Exception {

        return stepBuilderFactory.get(String.valueOf(index))
                .chunk(2)
                .reader(new CompositeSampleReader<>())
                .processor(new JbgroundProcessor<>())// 인풋타입의 아이템을 하나씩 반환
                .writer(new CompositeSampleWriter<>())
                .faultTolerant()
                .skipLimit(5)
                .skip(Exception.class)
                .listener(new JbgroundChunkListener())
                .taskExecutor(threadPoolTaskExecutor())// 리스트방식으로 반환 chunkSize 일괄처리
                .build();
    }

//    public AsyncItemProcessor defaultProcessor(WebAddress webAddress) throws Exception {
//        AsyncItemProcessor<Map<String, String>, ArchiveRecode> asyncItemProcessor = new AsyncItemProcessor<>();
//        asyncItemProcessor.setDelegate(new DefaultArchiveProcessor<>(webAddress));
//        asyncItemProcessor.setTaskExecutor(threadPoolTaskExecutor());
//        asyncItemProcessor.afterPropertiesSet();
//        return asyncItemProcessor;
//    }

//    public AsyncItemWriter defaultWriter() throws Exception {
//        AsyncItemWriter<ArchiveRecode> asyncItemWriter = new AsyncItemWriter<>();
//        asyncItemWriter.setDelegate(new DefaultArchiveWriter<>(archiveRepository));
//        asyncItemWriter.afterPropertiesSet();
//        return asyncItemWriter;
//    }


//    public Step uploadImageStep(int index) throws Exception {
//        return stepBuilderFactory.get("uploadImageStep-" + index)
//                .chunk(1)
//                .reader(uploadImageReader(index))
//                .processor(new UploadImageProcessor<>(uploadDirectory))// 인풋타입의 아이템을 하나씩 반환
//                .writer(new UploadImageWriter<>(archiveRepository))
//                .faultTolerant()
//                .skip(FileNotFoundException.class)
//                .skip(IOException.class)
//                .skip(SocketException.class)
//                .skipLimit(10000000)
//                .taskExecutor(threadPoolTaskExecutor())// 리스트방식으로 반환 chunkSize 일괄처리
//                .build();
//    }

}

package com.jbground.batch.job;

import com.jbground.batch.model.JbgroundNumber;
import com.jbground.batch.tasklet.SampleTasklet;
import com.jbground.batch.tasklet.chunk.processor.AddItemProcessor;
import com.jbground.batch.tasklet.chunk.processor.ChunkSampleProcessor;
import com.jbground.batch.tasklet.chunk.processor.SumItemProcessor;
import com.jbground.batch.tasklet.chunk.reader.ChunkSampleReader;
import com.jbground.batch.tasklet.chunk.reader.CompositeSampleReader;
import com.jbground.batch.tasklet.chunk.writer.ChunkSampleWriter;
import com.jbground.batch.tasklet.chunk.writer.CompositeSampleWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by jsjeong on 2023. 01. 18.
 * <pre>
 * spring batch Job 생성 기본 예제
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class JbgroundSampleJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public JbgroundSampleJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job jbgroundJob() throws Exception {
        return jobBuilderFactory.get("jbgroundJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart() //중단 시 재시작 방지
                .start(chunkStep())
//                .next(compositeStep())
//                .start(taskletStep())
                .build();
    }

    @Bean
    public Step taskletStep() {
        return stepBuilderFactory.get("jbground-tasklet")
                .tasklet(new SampleTasklet())
                .build();
    }

    @Bean
    public Step chunkStep() throws Exception {
        return stepBuilderFactory.get("jbground-chunk")
                .<JbgroundNumber, JbgroundNumber>chunk(10)
                .reader(new ChunkSampleReader<>())
                .processor(new ChunkSampleProcessor<>())
                .writer(new ChunkSampleWriter<>())
                .build();
    }

    @Bean
    public Step compositeStep() throws Exception {
        return stepBuilderFactory.get("jbground-composite")
                .<JbgroundNumber, JbgroundNumber>chunk(100)
                .reader(new CompositeSampleReader<>())
                .processor(createProcessor())
                .writer(new CompositeSampleWriter<>())
                .build();
    }



    public CompositeItemProcessor<JbgroundNumber, JbgroundNumber> createProcessor() throws Exception {
        CompositeItemProcessor<JbgroundNumber, JbgroundNumber> compositeItemProcessor = new CompositeItemProcessor<>();

        //방법 1
        compositeItemProcessor.setDelegates(Arrays.asList(new AddItemProcessor<>(), new SumItemProcessor<>()));
        compositeItemProcessor.afterPropertiesSet();

        //방법 2
//        List<ItemProcessor<JbgroundNumber, JbgroundNumber>> list = new ArrayList<>();
//        list.add(new AddItemProcessor<>());
//        list.add(new SumItemProcessor<>());
//        compositeItemProcessor.setDelegates(list);
//        compositeItemProcessor.afterPropertiesSet();

        return compositeItemProcessor;
    }


}

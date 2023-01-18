package com.jbground.batch.composite.job;

import com.jbground.batch.composite.*;
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
 * CompositeItemProcessor를 적용한 job
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class CompositeSampleJobConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    public CompositeSampleJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job composite() throws Exception {
        return jobBuilderFactory.get("composite")
                .incrementer(new RunIdIncrementer())
                .preventRestart() //중단 시 재시작 방지
                .start(compositeStep())
                .build();
    }

    public Step compositeStep() throws Exception {
        return stepBuilderFactory.get("composite")
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

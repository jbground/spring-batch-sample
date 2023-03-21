package com.jbground.batch.job;

import com.jbground.batch.model.Account;
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
 * 비동기(Async)방식 실행 예제
 *
 * ItemReader 에선 1000개의 계좌를 chunk단위로 생성
 * ItemProcessor 청크단위의 계좌에서 부과세 공제 및 부과세 공제 여부 등록
 * ItemWriter 1000개의 계좌 출력
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class AsyncJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public AsyncJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
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
    public Job asyncJob() throws Exception {
        return jobBuilderFactory.get("asyncJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart() //중단 시 재시작 방지
                .start(accountStep())
                .build();
    }

    public Step accountStep() throws Exception {
        return stepBuilderFactory.get("accountStep")
                .chunk(10)
                .reader(reader())
                .processor(processor())// 인풋타입의 아이템을 하나씩 반환
                .writer(writer())
                .taskExecutor(threadPoolTaskExecutor())// 리스트방식으로 반환 chunkSize 일괄처리
                .build();
    }

    //    public Step accountStep(int index) throws Exception {
//        return stepBuilderFactory.get("accountStep-")
//                .chunk(1)
//                .reader(reader(index))
//                .processor(processor())// 인풋타입의 아이템을 하나씩 반환
//                .writer(writer())
//                .faultTolerant()
//                .skip(FileNotFoundException.class)
//                .skip(IOException.class)
//                .skip(SocketException.class)
//                .skipLimit(10)
//                .taskExecutor(threadPoolTaskExecutor())// 리스트방식으로 반환 chunkSize 일괄처리
//                .build();
//    }


    public AbstractPagingItemReader reader() throws Exception {
        PagingAccountItemReader<Account> reader = new PagingAccountItemReader<>();
        reader.setPageSize(100);
        reader.afterPropertiesSet();
        return reader;
    }

    public AsyncItemProcessor processor() throws Exception {
        AsyncItemProcessor<Account, Account> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(new AsyncAccountItemProcessor<>());
        asyncItemProcessor.setTaskExecutor(threadPoolTaskExecutor());
        asyncItemProcessor.afterPropertiesSet();
        return asyncItemProcessor;
    }


    public AsyncItemWriter writer() throws Exception {
        AsyncItemWriter<Account> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(new AsyncAccountItemWriter<>());
        asyncItemWriter.afterPropertiesSet();
        return asyncItemWriter;
    }

}

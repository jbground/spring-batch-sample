package com.jbground.batch.job;

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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

/**
 * Created by jsjeong on 2023. 01. 18.
 * <pre>
 * spring batch Flow 방식 생성 예제
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class ExampleItemReaderJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private DataSource dataSource;

    public ExampleItemReaderJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
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
                .start(jdbcPagingStep())
                .next(jdbcCursorStep())
                .next(jpaPagingStep())
                .next(jpaCursorStep())
                .next(customStep())
                .build();
    }


    public Step jdbcPagingStep() throws Exception {
        return stepBuilderFactory.get("jdbcPagingStep")
                .chunk(100)
                .reader(jdbcPagingReader())  // 인풋타입의 아이템을 하나씩 반환
                .writer(writer())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .build();
    }

    public Step jdbcCursorStep() throws Exception {
        return stepBuilderFactory.get("jdbcCursorStep")
                .chunk(100)
                .reader(jdbcCursorReader())  // 인풋타입의 아이템을 하나씩 반환
                .writer(writer())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .build();
    }

    public Step jpaPagingStep() throws Exception {
        return stepBuilderFactory.get("jpaPagingStep")
                .chunk(100)
                .reader(jpaPagingReader())  // 인풋타입의 아이템을 하나씩 반환
                .writer(writer())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .build();
    }

  public Step jpaCursorStep() throws Exception {
        return stepBuilderFactory.get("jpaCursorStep")
                .chunk(100)
                .reader(jpaCursorReader())  // 인풋타입의 아이템을 하나씩 반환
                .writer(writer())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .build();
    }

    public Step customStep() throws Exception {
        return stepBuilderFactory.get("customStep")
                .chunk(100)
                .reader(customReader())  // 인풋타입의 아이템을 하나씩 반환
                .writer(writer())                       // 리스트방식으로 반환 chunkSize 일괄처리
                .build();
    }


    public JdbcPagingItemReader<Employee> jdbcPagingReader() {
        return new JdbcPagingItemReaderBuilder<Employee>()
                .name("jdbcPagingReader")
                .fetchSize(20)
                .dataSource(dataSource)
                .beanRowMapper(Employee.class)
                .build();
    }

    public JdbcCursorItemReader<Employee> jdbcCursorReader() {
        return new JdbcCursorItemReaderBuilder<Employee>()
                .name("jdbcPagingReader")
                .fetchSize(20)
                .dataSource(dataSource)
                .beanRowMapper(Employee.class)
                .sql("select * from emp")
                .maxItemCount(20)
                .currentItemCount(0)
                .maxRows(100)
                .build();
    }

    public JpaPagingItemReader<Employee> jpaPagingReader() {
        return new JpaPagingItemReaderBuilder<Employee>()
                .queryString("select * from emp")
                .pageSize(20)
                .currentItemCount(0)
                .maxItemCount(100)
                .build();
    }

    public JpaCursorItemReader<Employee> jpaCursorReader() {
        return new JpaCursorItemReaderBuilder<Employee>()
                .queryString("select * from emp")
                .currentItemCount(0)
                .maxItemCount(100)
                .build();
    }

    public CustomItemReader<Employee> customReader() {
        return new CustomItemReader<Employee>();
    }

    public ItemWriter writer() {
        return new JbgroundWriter<>();
    }
}

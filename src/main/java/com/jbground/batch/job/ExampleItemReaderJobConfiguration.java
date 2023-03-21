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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jsjeong on 2023. 01. 18.
 * <pre>
 * spring batch 다양한 ItemReader 사용 예제
 * </pre>
 */
@Configuration
@EnableBatchProcessing
public class ExampleItemReaderJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Resource(name = "hikari_oracle")
    private DataSource dataSource;

    @Autowired
    private EntityManagerFactory emf;


    public ExampleItemReaderJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
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
    public Job itemReaderJob() throws Exception {
        return jobBuilderFactory.get("itemReaderJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart() //중단 시 재시작 방지
//                .start(jdbcPagingStep())
//                .start(jdbcCursorStep())
//                .start(jpaPagingStep())
                .start(jpaCursorStep())
//                .next(customStep())
                .build();
    }

    public Step jdbcPagingStep() throws Exception {
        return stepBuilderFactory.get("jdbcPagingStep")
                .chunk(1)
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

    public JdbcPagingItemReader<Employee> jdbcPagingReader() throws Exception {
        JdbcPagingItemReader<Employee> jdbcPagingReader = new JdbcPagingItemReaderBuilder<Employee>()
                .name("jdbcPagingReader")
                .pageSize(3) //페이지의 사이즈를 설정해준다. (쿼리 당 요청할 레코드 수)
                .fetchSize(20)
                .dataSource(dataSource)
                .beanRowMapper(Employee.class) //객체 클래스를 넣으면 자동으로 DB 데이터가 객체에 맵핑됨.
                .maxItemCount(10)//한번에 조회할 최대 item 수 설정
                .currentItemCount(0)//조회 Item의 시작 지점 설정
                .queryProvider(createQueryProvider())
                .build();

        jdbcPagingReader.afterPropertiesSet();
        return jdbcPagingReader;
    }

    public JdbcCursorItemReader<Employee> jdbcCursorReader() {
        return new JdbcCursorItemReaderBuilder<Employee>()
                .name("jdbcCursorReader")
                .fetchSize(20)
                .dataSource(dataSource)
                .beanRowMapper(Employee.class)
                .sql("select empno, ename, job, hiredate, sal, deptno from emp")
                .maxItemCount(20)
                .currentItemCount(0)
                .build();
    }

    public JpaPagingItemReader<Employee> jpaPagingReader() {
        return new JpaPagingItemReaderBuilder<Employee>()
                .name("jpaPagingReader")
                .entityManagerFactory(emf)
                .queryString("select e.empNo, e.eName, e.job, e.hireDate, e.sal, e.deptNo from Employee e")
                .pageSize(5)
                .build();
    }

    public JpaCursorItemReader<Employee> jpaCursorReader() {
        return new JpaCursorItemReaderBuilder<Employee>()
                .name("jpaCursorItemReader")
                .entityManagerFactory(emf)
                .queryString("select e.empNo, e.eName, e.job, e.hireDate, e.sal, e.deptNo from Employee e")
                .currentItemCount(0) //조회 item의 시작지점
                .maxItemCount(100) //조회할 최대 item 수
                .build();
    }

    public CustomItemReader<Employee> customReader() {
        return new CustomItemReader<Employee>();
    }

    public ItemWriter<? super Object> writer() {
        return new JbgroundWriter<>();
    }

    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        queryProviderFactoryBean.setDataSource(dataSource);
        queryProviderFactoryBean.setSelectClause("empno, ename, job, hiredate, sal, deptno");
        queryProviderFactoryBean.setFromClause("from emp");

        Map<String, Order> sortKey = new HashMap<>();
        sortKey.put("empno", Order.ASCENDING);

        queryProviderFactoryBean.setSortKeys(sortKey);

        return queryProviderFactoryBean.getObject();

    }
}

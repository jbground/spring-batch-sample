package com.jbground.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;

/**
 * Created by jsjeong on 2022. 9. 20.
 * <pre>
 * Spring Batch의 관리를 Database가 아닌 Memory에서 수행할 수 있도록 설정
 * </pre>
 */
@Configuration
public class InMemoryBatchConfigurer implements BatchConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryBatchConfigurer.class);
    private PlatformTransactionManager transactionManager;
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private JobExplorer jobExplorer;

    @Override
    public PlatformTransactionManager getTransactionManager(){
        return transactionManager;
    }
    @Override
    public JobRepository getJobRepository(){
        return jobRepository;
    }
    @Override
    public JobLauncher getJobLauncher(){
        return jobLauncher;
    }
    @Override
    public JobExplorer getJobExplorer(){
        return jobExplorer;
    }

    @PostConstruct
    public void initialize() {
        if (this.transactionManager == null)
            logger.debug("Using a ResourcelessTransactionManager");
            this.transactionManager = new ResourcelessTransactionManager();

        try {
            MapJobRepositoryFactoryBean jobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(this.transactionManager);
            jobRepositoryFactoryBean.afterPropertiesSet();
            this.jobRepository = jobRepositoryFactoryBean.getObject();

            MapJobExplorerFactoryBean jobExplorerFactoryBean = new MapJobExplorerFactoryBean(jobRepositoryFactoryBean);
            jobExplorerFactoryBean.afterPropertiesSet();
            this.jobExplorer = jobExplorerFactoryBean.getObject();

            this.jobLauncher = createJobLauncher();

        } catch (Exception e) {
            throw new BatchConfigurationException(e);
        }

    }

    protected JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}

package com.jbground.batch.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;


@Configuration
@PropertySource(value = {"classpath:/datasource.properties"})
public class DataSourceConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Autowired
    private Environment env;


    @Bean(name = "oracle_config")
    @Primary
    public HikariConfig oracleConfig() throws Exception{
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getProperty("jbground.batch.oracle.driver-class-name"));
        hikariConfig.setJdbcUrl(env.getProperty("jbground.batch.oracle.url"));
        hikariConfig.setUsername(env.getProperty("jbground.batch.oracle.username"));
        hikariConfig.setPassword(env.getProperty("jbground.batch.oracle.password"));
        hikariConfig.setPoolName(env.getProperty("jbground.batch.oracle.pool-name"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(env.getProperty("jbground.batch.oracle.maximum-pool-size")));
        hikariConfig.setMinimumIdle(Integer.parseInt(env.getProperty("jbground.batch.oracle.minimum-idle")));
        hikariConfig.setMaxLifetime(Long.parseLong(env.getProperty("jbground.batch.oracle.max-life-time")));
        hikariConfig.setConnectionTimeout(Long.parseLong(env.getProperty("jbground.batch.oracle.connection-timeout")));
        hikariConfig.setValidationTimeout(Long.parseLong(env.getProperty("jbground.batch.oracle.validation-timeout")));
        hikariConfig.setIdleTimeout(Long.parseLong(env.getProperty("jbground.batch.oracle.idle-timeout")));
        return hikariConfig;
    }

    @Bean(name = "hikari_oracle")
    @Primary
    public DataSource oracle(@Qualifier("oracle_config") HikariConfig hikariConfig) {
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        logger.info("create oracle hikari datasource");
        return hikariDataSource;
    }

}

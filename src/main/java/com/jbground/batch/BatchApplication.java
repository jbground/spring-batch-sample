package com.jbground.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by jsjeong on 2023. 01. 18.
 * <pre>
 * spring에 탑재된 job.name 옵션을 이용하여 Program Arguments로 인자를 전달받아 실행하는 방법
 * Program Arguments
 * --job.name=jbground
 * </pre>
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}
package com.jbground.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

//@SpringBootTest
class BatchApplicationTest {

//    @Autowired
//    ApplicationContext context;
//
//    @Resource(name = "partitioningJob")
//    Job job;
//
//    @Resource
//    JobLauncher launcher;

    @DisplayName("JbgroundJob")
    @Test
    void test1() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        System.out.println("test");
//
//        launcher.run(job, new JobParameters());
    }

    @DisplayName("FlowJob")
    @Test
    void test2() throws Exception {
        CommandLineJobRunner.main(new String[]{"com.jbground.batch.job.PartitioningJobConfiguration", "flowJob"});
    }

    @DisplayName("PartitioningJob")
    @Test
    void test3() throws Exception {
        CommandLineJobRunner.main(new String[]{"com.jbground.batch.job.PartitioningJobConfiguration", "partitioningJob"});
    }

    @DisplayName("Compositejob")
    @Test
    void test4() {

    }

    @DisplayName("AysncJob")
    @Test
    void test5() {

    }

    @DisplayName("MultiThreadJob")
    @Test
    void test6() {

    }

}

//    엔터프라이즈 스케쥴러로부터 작업을 수행시키기를 원하는 유저들은 커맨드 라인이 주요한 인터페이스이다.
//
//        대부분의 스케쥴러들이 OS의 쉘 스크립트를 수행하여 작업을 수행하기 때문이다.
//
//        또한 shell script, Perl, Ruby 혹은 maven, ant와 같은 빌드툴에서도 자바 프로세스를 구동하는 방법을 제공한다.
//
//        이러한 이유에서 Spring Batch에서는 CommandLineJobRunner을 제공한다.
//
//        다음은 CommandLineJobRunner의 주요기능이다.
//        - 적절한 ApplicationContext 로딩
//
//        - 커맨드 라인 arguments를 파싱하여 JobParameters로 변환
//
//        - Arguments에 지정한 적절한 작업 수행
//
//        - 작업 수행을 위해 ApplicationContext에서 제공한 적절한 JobLauncher 사용
//        커맨드라인에서 작업을 수행하기 위해서 다음과 같은 arguments를 전달해야 한다.
//        첫번째는 작업이 정의된 xml의 jobPath이며 두번째는 jobName이다. 이후의 모든 arguments는 JobParameters로 고려되며 'name=value' 포맷이다.
//        bash$ java CommandLineJobRunner endOfDayJob.xml endOfDay schedule.date(date)=2007/05/05
//        커맨드라인으로 부터 작업이 구동되면 엔터프라이즈 스케쥴러는 종종 ExitCodes를 이용한다.
//        대부분의 스케쥴러들은 프로세스 레벨에서만 동작한다.
//        이 의미는 그들이 실행한 쉘스크립트의 시스템 프로세스만 알고 있다는 것이다.
//        이러한 시나리오에서 작업의 성공 및 실패 여부를 전달할 수 있는 유일한 방법이 코드를 리턴하는 것이다.
//        리턴 코드는 숫자이며 스케쥴러는 프로세스로 부터 그 숫자를 전달받고, 실행의 결과를 인지한다.
//        가장 단순한 케이스는 0은 성공이며 1은 실패이다. 그러나 좀 더 복잡한 케이스를 위해 ExitCodeMapper 인터페이스가 제공된다.
//        기본적으로 SimpleJVMExitCodeMapper를 제공한다.
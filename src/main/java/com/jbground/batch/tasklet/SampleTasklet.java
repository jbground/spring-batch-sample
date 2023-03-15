package com.jbground.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class SampleTasklet implements Tasklet {

    private int num = 1;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){

        //num이 5보다 크면 종료
        if(num > 5)
            return RepeatStatus.FINISHED;

        System.out.println("SampleTasklet "+ num++ +"번 째 실행");

        return RepeatStatus.CONTINUABLE;
    }

}

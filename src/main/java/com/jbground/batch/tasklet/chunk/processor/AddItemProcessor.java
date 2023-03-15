package com.jbground.batch.tasklet.chunk.processor;

import com.jbground.batch.model.JbgroundNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Random;

public class AddItemProcessor<I, O> implements ItemProcessor<I, O> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Random random = new Random();

    @Override
    public O process(I item){
        JbgroundNumber number = (JbgroundNumber) item;

        int rInt = random.nextInt(10);
        number.setB(rInt);
        logger.info("B 랜덤값 추가 - A : {}, B : {}", number.getA(), number.getB());

        return (O) number;
    }
}

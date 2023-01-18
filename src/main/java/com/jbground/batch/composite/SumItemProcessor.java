package com.jbground.batch.composite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class SumItemProcessor<I, O> implements ItemProcessor<I, O> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public O process(I item) throws Exception {
        JbgroundNumber number = (JbgroundNumber) item;
        int a = number.getA();
        int b = number.getB();
        number.setResult(a + b);
        logger.info("더하기 과정 {} + {} = {}", number.getA(), number.getB(), number.getResult());

        return (O) number;
    }
}
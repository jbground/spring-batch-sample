package com.jbground.batch.tasklet.chunk.processor;

import com.jbground.batch.model.Account;
import org.springframework.batch.item.ItemProcessor;

public class AsyncAccountItemProcessor<I, O>  implements ItemProcessor<I, O> {

    @Override
    public O process(I item) throws Exception {
        Account account = (Account) item;

        try{
            long bal = account.getBalance();
            account.setBefore(bal);
            double tax = account.getTax();


            double v = bal - (bal * tax);

            account.setAfter((long) v);
            account.setCharge("Y");
        }catch (Exception e){
            account.setCharge("N");

        }

        return null;
    }
}

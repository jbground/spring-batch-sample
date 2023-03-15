package com.jbground.batch.tasklet.chunk.reader;

import com.jbground.batch.model.Account;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

public class AsyncAccountItemReader<T> implements ItemReader<T> {


    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        int max = 10000000;
        int min = 1000000;

        int start = 1* 100;
        int end = (1+1) * 100;

        List<Account> list = new ArrayList<>();

        for(int i = start; i<end; i++){
            Account account = new Account();
            account.seteName("Name["+i+"]");
            long bal = (long) (Math.random() * (max - min +1) + min);
            account.setBalance(bal);
            account.setTax(0.3);

            list.add(account);
        }
        return null;
    }
}

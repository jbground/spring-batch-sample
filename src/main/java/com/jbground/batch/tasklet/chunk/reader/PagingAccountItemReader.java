package com.jbground.batch.tasklet.chunk.reader;

import com.jbground.batch.model.Account;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PagingAccountItemReader<T> extends AbstractPagingItemReader<T> {

    //Math.random() * (최댓값-최소값+1) + 최소값
    @Override
    protected void doReadPage() {

        int max = 10000000;
        int min = 1000000;

        int start = getPage() * 100;
        int end = (getPage()+1) * 100;

        List<Account> list = new ArrayList<>();

        for(int i = start; i<end; i++){
            Account account = new Account();
            account.seteName("Name["+i+"]");
            long bal = (long) (Math.random() * (max - min +1) + min);
            account.setBalance(bal);
            account.setTax(0.3);

            list.add(account);
        }

        results.addAll((Collection<? extends T>) list);

    }

    @Override
    protected void doJumpToPage(int itemIndex) {

    }
}

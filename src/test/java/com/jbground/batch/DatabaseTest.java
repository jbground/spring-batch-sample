package com.jbground.batch;

import com.jbground.batch.model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseTest {


    List list = new ArrayList();

    @Test
    void test() {
        for(Iterator iterator = list.iterator(); iterator.hasNext();){
            User user = (User) iterator.next();
        }


        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
            User user = (User) iterator.next();
        }


        Iterator iterator2 = list.iterator(); //제일 병신같은 코드
        for(int i = 0 ; i<1;){
            if(!iterator2.hasNext()){
                break;
            }

            User user = (User) iterator2.next();
        }
    }
}

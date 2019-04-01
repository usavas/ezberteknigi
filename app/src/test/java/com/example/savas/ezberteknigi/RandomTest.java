package com.example.savas.ezberteknigi;

import org.junit.Test;

import java.util.Date;

public class RandomTest {

    @Test
    public void DateLongConvert(){
        long tmp = 1346524199000L;

        Date d = new Date(tmp);
        System.out.println(d);

        long dt = new Date().getTime();
        System.out.println(dt);


        System.out.println(new Date(dt));
    }
}

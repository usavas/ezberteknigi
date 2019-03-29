package com.example.savas.ezberteknigi;

import org.junit.Test;

public class RandomTest {

    @Test
    void getSourceDirOfApplication(){
        System.out.println(AppStarter.getContext().getApplicationInfo().sourceDir);
    }
}

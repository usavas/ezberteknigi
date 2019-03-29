package com.example.savas.ezberteknigi.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BufferedReaderHelper {

    public static String readFromInputStream(InputStream fis) throws IOException {

        InputStreamReader reader = new InputStreamReader(fis);
        BufferedReader bis = new BufferedReader(reader);

        String line = "";
        StringBuilder buffer = new StringBuilder();
        while ((line = bis.readLine()) != null){
            buffer.append(line);
        }

        return buffer.toString();
    }
}

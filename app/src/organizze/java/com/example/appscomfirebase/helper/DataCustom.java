package com.example.appscomfirebase.helper;

import java.text.SimpleDateFormat;

public class DataCustom {

    public static String dataAtual(){

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("dd/MM/yyyy");
        String dataStrig = simpleDateFormat.format(data);
        return dataStrig;
    }
}

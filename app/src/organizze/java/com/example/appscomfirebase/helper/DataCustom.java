package com.example.appscomfirebase.helper;

import java.text.SimpleDateFormat;

public class DataCustom {

    public static String dataAtual(){

        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("dd/MM/yyyy");
        String dataStrig = simpleDateFormat.format(data);
        return dataStrig;
    }

    public static String dataEscolhida(String data){

        String retornoData[] = data.split("/");

        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        String mesAno = mes + ano;

        return mesAno;

    }
}

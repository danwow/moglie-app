package com.example.moglie.helper;

import java.text.SimpleDateFormat;

public class DateCustom {
    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;

    }

    public static  String mesAnoDataEscolhida(String data){

        String retornoDATA[] = data.split("/");
        String dia = retornoDATA[0];
        String mes = retornoDATA[1];
        String ano = retornoDATA[2];

        String mesAno = mes + ano;
        return mesAno;
    }

    public static String dataGrafico(float value){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
        String dataString = simpleDateFormat.format(value);
        return dataString;
    }

    public static String horaGrafico(float value){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
        String horaString = simpleDateFormat.format(value);
        return horaString;
    }




}

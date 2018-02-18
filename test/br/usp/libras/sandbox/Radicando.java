package br.usp.libras.sandbox;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Radicando {

    public static void main(String[] args) {
//        float raio = 216.0f;
//        float x = -198.0f;
//        float y = -45.0f;
//        float centerX = 10.0f;
//        float centerY = 10.0f;
        float raio = 216.10182f;
        float x = -198.92691f;
        float y = -45.222668f;
        float centerX = 10.0f;
        float centerY = 10.0f;
        //        double radicando = Math.pow(Math.round(raio), 2) - Math.pow(Math.round(x) - Math.round(centerX), 2) - Math.pow(Math.round(y) - Math.round(centerY), 2);
        BigDecimal bigRaio = new BigDecimal(raio).setScale(1, RoundingMode.FLOOR);
        BigDecimal bigX = new BigDecimal(x).setScale(1, RoundingMode.FLOOR);
        BigDecimal bigY = new BigDecimal(y).setScale(1, RoundingMode.FLOOR);
        BigDecimal bigCenterX = new BigDecimal(centerX).setScale(1, RoundingMode.FLOOR);
        BigDecimal bigCenterY = new BigDecimal(centerY).setScale(1, RoundingMode.FLOOR);
        System.out.println(bigRaio + " " + bigX + " " + bigCenterX + " " + bigY + " " + bigCenterY);
        BigDecimal radicando = bigRaio.pow(2).setScale(1, RoundingMode.FLOOR).subtract(bigX.subtract(bigCenterX).pow(2).setScale(1, RoundingMode.FLOOR)).subtract(bigY.subtract(bigCenterY).pow(2).setScale(1, RoundingMode.FLOOR));
        System.out.println(radicando);
    }

}

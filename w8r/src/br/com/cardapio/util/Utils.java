package br.com.cardapio.util;

public class Utils {
    public static String split(String s, int t) {
        return s != null && s.length() > t ? s.substring(0, t) : s;
    }
}

package com.geicon.blue.framework.util;

/**
 * Utilitário para Strings
 *
 * @author Gabriel Duarte
 */
public class Strings {
    /**
     * Gerar senha
     *
     * @return Senha gerada
     */
    public static String createPassword() {
        char chars[] = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

        StringBuilder sb = new StringBuilder(6);

        for (int x = 0; x < 6; x++) {
            sb.append(chars[(int) (Math.random() * chars.length)]);
        }

        return sb.toString();
    }

    /**
     * Construtor
     */
    private Strings() {
    }
}

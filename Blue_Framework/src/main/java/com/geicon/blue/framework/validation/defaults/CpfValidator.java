package com.geicon.blue.framework.validation.defaults;

import java.util.InputMismatchException;

/**
 * Classe para validação de CPF
 *
 * @author Gabriel Duarte
 */
public class CpfValidator extends DefaultValidator {
    /**
     * Verifica se o valor digitado é mesmo um CPF válido
     *
     * @param cpf CPF a ser validado
     * @return true se sim / false se não
     */
    private static boolean isCpf(String cpf) {
        if (cpf.equals("00000000000") || cpf.equals("11111111111")
                || cpf.equals("22222222222") || cpf.equals("33333333333")
                || cpf.equals("44444444444") || cpf.equals("55555555555")
                || cpf.equals("66666666666") || cpf.equals("77777777777")
                || cpf.equals("88888888888") || cpf.equals("99999999999")
                || (cpf.length() != 11)) {
            return (false);
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = cpf.charAt(i) - 48;
                sm += (num * peso);
                peso -= 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            }
            else {
                dig10 = (char) (r + 48);
            }

            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = cpf.charAt(i) - 48;
                sm += (num * peso);
                peso -= 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            }
            else {
                dig11 = (char) (r + 48);
            }

            if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))) {
                return (true);
            }
            else {
                return (false);
            }
        }
        catch (InputMismatchException erro) {
            return (false);
        }
    }

    /**
     * Construtor
     *
     * @param cpf CPF a ser validado
     * @param message Mensagem resultante
     */
    public CpfValidator(String cpf, String message) {
        if (cpf != null) {
            cpf = cpf.replaceAll("[^0-9]+", "");

            add(new DefaultValidationItem(isCpf(cpf), message));
        }
    }
}
